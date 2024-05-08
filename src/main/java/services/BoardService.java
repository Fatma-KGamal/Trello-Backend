package services;

import java.util.List;


import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import models.Board;
import models.User;

@Stateless
public class BoardService {
	
	Board board;
	
	@PersistenceContext (name="trello")
	private EntityManager entityManager;
	
	//Users can create a new board with a unique name.
	// user should send id in the parameter to get the user who is creating the board
	public Board createBoard(String name) {
		//check if the user is logged in
		if (User.isCurrentUser() == false) {
			throw new IllegalArgumentException("User not logged in");
		}
		else {
			// create a new board
			board = new Board();
			board.setBoardName(name);
			entityManager.persist(board);
			//User.getCurrentUser().getUserBoards().add(board);
			return board;
		}
	}
	

	//Users can delete a board.
	public void deleteBoard(long id) {
		//check if the user is logged in
		if (User.isCurrentUser() == false) {
			throw new IllegalArgumentException("User not logged in");
		}
		else {
		board = entityManager.find(Board.class, id);
		entityManager.remove(board);
		}
	}
	
	//Users can view all boards they have access to.
	public List<Board> getBoards(long id) {
		//check if the user is logged in
		if (User.isCurrentUser() == false) {
			throw new IllegalArgumentException("User not logged in");
		}
		else {
//		User user = entityManager.find(User.class, id);
//		return user.getUserBoards();
			TypedQuery<Board> query = entityManager.createQuery("SELECT b from Board b LEFT JOIN FETCH b.users u WHERE u.id = :id", Board.class);
			query.setParameter("id", id);
			return query.getResultList();
		}
	}
	
	//Users can invite other users to collaborate on a board.
	public void inviteUser(long boardId, long userId) {
		//check if the user is logged in
		if (User.isCurrentUser() == false) {
			throw new IllegalArgumentException("User not logged in");
		}
		else {
		board = entityManager.find(Board.class, boardId);
		User user = entityManager.find(User.class, userId);
		user.getUserBoards().add(board);
		board.getUsers().add(user);
		entityManager.merge(board);
		entityManager.merge(user);
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
