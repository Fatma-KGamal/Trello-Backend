package services;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.ws.rs.core.Response;

import messaging.JMSClient;
import models.Board;
import models.CardList;
import models.User;

@Stateless
public class ListService {

	CardList cardList;

	Board board;

	@PersistenceContext(name = "trello")
	private EntityManager entityManager;

	@Inject
	JMSClient jmsClient;

	// Users can create lists within a board to categorize tasks in a specific
	// board.

	public Response createList(long userId, long boardId, String categoryName) {
		// check if the user is logged in
		if (User.isCurrentUser() == false) {
			return Response.status(Response.Status.BAD_REQUEST).entity("User is not logged in").build();
		} else {
			User user = entityManager.find(User.class, userId);
			if (user.isAdmin() == false) {
				return Response.status(Response.Status.BAD_REQUEST).entity("User is not Team Leader").build();

			} else {
				board = entityManager.find(Board.class, boardId);
				if (board != null) {
					cardList = new CardList();
					cardList.setCategory(categoryName);
					cardList.setBoard(board);
					entityManager.persist(cardList);
					board.getCardList().add(cardList);
					entityManager.merge(board);
					// notify users when a list is created
					notifyList("List created: " + categoryName);
					return Response.status(Response.Status.CREATED).entity("list created successfully \n").build();
				} else {
					return Response.status(Response.Status.BAD_REQUEST).entity("Board not found").build();
				}
			}
		}
	}

	// Users can delete a list.
	public Response deleteList(long userId, long boardId, long listId) {
		// check if the user is logged in
		if (User.isCurrentUser() == false) {
			return Response.status(Response.Status.BAD_REQUEST).entity("User is not logged in").build();
		} else {
			User user = entityManager.find(User.class, userId);
			if (user.isAdmin() == false) {
				return Response.status(Response.Status.BAD_REQUEST).entity("User is not Team Leader").build();
			} else {
				board = entityManager.find(Board.class, boardId);
				if (board != null) {
					cardList = entityManager.find(CardList.class, listId);
					if (cardList != null) {
						board.getCardList().remove(cardList);
						cardList.setBoard(null);
						entityManager.merge(board);
//		            cardList = entityManager.merge(cardList);
						entityManager.remove(cardList);
						entityManager.flush();

					} else {
						return Response.status(Response.Status.BAD_REQUEST).entity("List not found").build();
					}
				} else {
					return Response.status(Response.Status.BAD_REQUEST).entity("Board not found").build();
				}
			}
		}
		// notify users when a list is deleted
		notifyList("List deleted: " + cardList.getCategory());
		return Response.status(Response.Status.OK).entity("List deleted successfully").build();
	}
	
	public List<CardList> getAllLists() {
		TypedQuery<CardList> query = entityManager.createQuery("SELECT c from CardList c", CardList.class);
		return query.getResultList();
	}

	// notify users when a list is created
	public void notifyList(String message) {
		jmsClient.sendMessage(message);
	}

}
