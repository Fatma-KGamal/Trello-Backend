package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.persistence.JoinColumn;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"boardName"}))
public class Board {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long boardId;
	public long getBoard_id() {
		return boardId;
	}
	public void setBoard_id(long board_id) {
		this.boardId = board_id;
	}

	private String boardName;
	public String getBoardName() {
		return boardName;
	}
	public void setBoardName(String boardName) {
		this.boardName = boardName;
	}
	
	@ManyToMany
	@JoinTable(name = "user_board", joinColumns = @JoinColumn(name = "board_id"), inverseJoinColumns = @JoinColumn(name = "id"))
	private List <User> users;
	
	public List<User> getUsers() {
		return users;
	}
	public void setUsers(List<User> users) {
		this.users = users;
	}
	
	@OneToMany (mappedBy="board")
	private List <CardList> cardList;
	
	public List<CardList> getCardList() {
		return cardList;
	}
	public void setCardList(List<CardList> cardList) {
		this.cardList = cardList;
	}
	
	@Override
	public String toString() {
		return " , Board [board_id=" + boardId + ", boardName=" + boardName + "]";
	}
}
