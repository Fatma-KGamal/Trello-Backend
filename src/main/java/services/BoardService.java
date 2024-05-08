package services;

import java.util.ArrayList;
import java.util.List;


import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.ws.rs.core.Response;

import models.Board;
import models.User;

@Stateless
public class BoardService {
	
	Board board;
	
	@PersistenceContext (name="trello")
	private EntityManager entityManager;
	
	//Users can create a new board with a unique name.
	public Response createBoard(long userId , String name) {
		//check if the user is logged in
		if (User.isCurrentUser() == false) {
			return Response.status(Response.Status.BAD_REQUEST).entity("User is not logged in").build();
		}
		else {
			User user = entityManager.find(User.class, userId);
			if (user.isAdmin() == false) {
				return Response.status(Response.Status.BAD_REQUEST).entity("User is not Team Leader").build();
			}
			else {
			// create a new board
			board = new Board();
			board.setBoardName(name);
			entityManager.persist(board);
			// add the board to the user
			List<User> users = new ArrayList<User>();
			users.add(user);
			board.setUsers(users);
			List<Board> boards = user.getUserBoards();
			boards.add(board);
			user.setUserBoards(boards);
			entityManager.merge(user);
			return Response.status(Response.Status.CREATED).entity("Board created successfully \n" + board).build();
			}
		}
	}


	//Users can delete a board.
	public Response deleteBoard(long userId, long boardId) {
		//check if the user is logged in
		if (User.isCurrentUser() == false) {
			return Response.status(Response.Status.BAD_REQUEST).entity("User is not logged in").build();
		}
		else {
			User user = entityManager.find(User.class, userId);
			if (user.isAdmin() == false) {
				return Response.status(Response.Status.BAD_REQUEST).entity("User is not Team Leader").build();
			}
			else {
				try{
//					board = entityManager.find(Board.class, boardId);
//					user.getUserBoards().remove(board);
//	                entityManager.merge(user);
					TypedQuery<Board> query = entityManager.createQuery("SELECT b from Board b WHERE b.boardId = :id", Board.class);
					query.setParameter("id", boardId);
					System.out.println("Hi :" + boardId);
					board = query.getSingleResult();
					System.out.println("Hi :" + board);
					entityManager.remove(board);
//					entityManager.flush();
					return Response.status(Response.Status.OK).entity("Board deleted successfully").build();
				}
				catch (IllegalArgumentException e) {
					return Response.status(Response.Status.BAD_REQUEST).entity("no boards").build();
				}
		}
	}
	}
	
	//Users can view all boards they have access to.
	public Response getBoards(long teamLeaderId ,long userId) {
		//check if the user is logged in
		if (User.isCurrentUser() == false) {
			return Response.status(Response.Status.BAD_REQUEST).entity("User is not logged in").build();
		}
		else {
			User teamLeader = entityManager.find(User.class, teamLeaderId);
			if (teamLeader.isAdmin() == false) {
				return Response.status(Response.Status.BAD_REQUEST).entity("User is not Team Leader").build();
			}
			else {
			TypedQuery<Board> query = entityManager.createQuery("SELECT b from Board b LEFT JOIN FETCH b.users u WHERE u.id = :id", Board.class);
			query.setParameter("id", userId);
			return Response.status(Response.Status.OK).entity(query.getResultList()).build();
		}
		}
	}
	
	//Users can invite other users to collaborate on a board.
	public Response inviteUser(long teamLeaderId,long userId,long boardId) {
		//check if the user is logged in
		if (User.isCurrentUser() == false) {
			return Response.status(Response.Status.BAD_REQUEST).entity("User is not logged in").build();
		}
		else {
			User teamLeader = entityManager.find(User.class, teamLeaderId);
			User user = entityManager.find(User.class, userId);
			if (teamLeader.isAdmin() == false) {
				return Response.status(Response.Status.BAD_REQUEST).entity("User is not Team Leader").build();
			}
			else {
            	board = entityManager.find(Board.class, boardId);
            	//	User user = entityManager.find(User.class, userId);
            	user.getUserBoards().add(board);
            	board.getUsers().add(user);
            	entityManager.merge(board);
            	entityManager.merge(user);
            	return Response.status(Response.Status.OK).entity("User invited Successfully").build();
		}
		}
	}
	
	//Users can view all boards
	public List<Board> getAllBoards() {
		//check if the user is logged in
		if (User.isCurrentUser() == false) {
			throw new IllegalArgumentException("User not logged in");
		}
		else {
		TypedQuery<Board> query = entityManager.createQuery("SELECT DISTINCT b from Board b LEFT JOIN FETCH b.users ", Board.class);
		return query.getResultList();
		}
	}
	

}
