package models;

import java.util.ArrayList;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Board {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private String boardName;
	private ArrayList <CardList> cardList;
	private ArrayList <User> users;
	
	public String getBoardName() {
		return boardName;
	}
	public void setBoardName(String boardName) {
		this.boardName = boardName;
	}
	public ArrayList<CardList> getCardList() {
		return cardList;
	}
	public void setCardList(ArrayList<CardList> cardList) {
		this.cardList = cardList;
	}
	public ArrayList<User> getUsers() {
		return users;
	}
	public void setUsers(ArrayList<User> users) {
		this.users = users;
	}

	
	
}
