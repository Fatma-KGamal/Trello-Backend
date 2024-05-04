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
	public Board createBoard(String name) {
		//check if the user is logged in
//		if (User.getCurrentUser() == null) {
//			throw new IllegalArgumentException("User not logged in");
//		}
		board = new Board();
		board.setBoardName(name);
		entityManager.persist(board);
		return board;
	}
	
	//Users can delete a board.
	public void deleteBoard(long id) {
		board = entityManager.find(Board.class, id);
		entityManager.remove(board);
	}
	
	//Users can view all boards they have access to.
	public List<Board> getBoards(long id) {
		TypedQuery<User> userBoardList = entityManager.createQuery("SELECT u from User u LEFT JOIN FETCH u.userBoards WHERE u.id = :id", User.class);
		userBoardList.setParameter("id", id);
		return userBoardList.getResultList().get(0).getUserBoards();
	}
	
	//Users can invite other users to collaborate on a board.
	public void inviteUser(long boardId, long userId) {
		board = entityManager.find(Board.class, boardId);
		User user = entityManager.find(User.class, userId);
		user.getUserBoards().add(board);
		entityManager.merge(user);
	}
	

}
