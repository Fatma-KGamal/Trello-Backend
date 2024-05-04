package models;

import java.util.ArrayList;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class CardList {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int listId;
	private String category;
	private ArrayList <Card> cards;
	
	public int getId() {
		return listId;
	}
	public void setId(int id) {
		this.listId = id;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public ArrayList<Card> getCards() {
		return cards;
	}
	public void setCards(ArrayList<Card> cards) {
		this.cards = cards;
	}
	
	@ManyToOne 
	@JoinColumn(name="boardId")
	private Board board;

	public Board getBoard() {
		return board;
	}
	public void setBoard(Board board) {
		this.board = board;
	}
}
