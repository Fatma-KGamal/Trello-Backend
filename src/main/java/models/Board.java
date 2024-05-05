package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonManagedReference;

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
	
	@ManyToMany (fetch = FetchType.LAZY)
	@JoinTable(name = "user_board", joinColumns = @JoinColumn(name = "board_id"), inverseJoinColumns = @JoinColumn(name = "id"))
	private List <User> users;
	
	public List<User> getUsers() {
		return users;
	}
	public void setUsers(List<User> users) {
		this.users = users;
	}
	
	@OneToMany (mappedBy="board" , fetch = FetchType.EAGER , cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonManagedReference
	private List <CardList> cardList = new ArrayList<CardList>();
	
	public List<CardList> getCardList() {
		return cardList;
	}
	public void setCardList(List<CardList> cardList) {
		this.cardList = cardList;
	}
	
	@Override
	public String toString() {
		return "Board [boardId=" + boardId + ", boardName=" + boardName + ", users=" + users + ", cardList=" + cardList + "]";
	}
	
}
