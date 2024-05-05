package services;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import models.Board;
import models.CardList;
import models.User;

@Stateless
public class ListService {
	
	CardList cardList;
	
	Board board;
	
	@PersistenceContext (name="trello")
	private EntityManager entityManager;
	
	//Users can create lists within a board to categorize tasks in a specific board.
	
	public CardList createList(long boardId , String categoryName) {
		if (User.isCurrentUser() == false) {
			throw new IllegalArgumentException("User not logged in");
		}
		else {
		 board = entityManager.find(Board.class, boardId);
		    if (board != null) {
		        cardList = new CardList();
		        cardList.setCategory(categoryName);
		        cardList.setBoard(board);
		        entityManager.persist(cardList);
		        board.getCardList().add(cardList);
		        entityManager.merge(board);
		        }
		    else
		    {
		    	throw new IllegalArgumentException("Board not found");
		    }
        return cardList;
		}
   }
	
	//Users can delete a list.
	public void deleteList(long boardId , long listId) {
		if (User.isCurrentUser() == false) {
			throw new IllegalArgumentException("User not logged in");
		}
		else {
		 board = entityManager.find(Board.class, boardId);
		    if (board != null) {
		        cardList = entityManager.find(CardList.class, listId);
		        if (cardList != null) {
		            board.getCardList().remove(cardList);
		            entityManager.merge(board);
		            //cardList = entityManager.merge(cardList);
		            entityManager.remove(cardList);
		        }
		    }
		}
	}
}
