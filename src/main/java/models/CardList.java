package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
public class CardList {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long listId;
	private String categoryName;
	//private ArrayList <Card> cards;
	
	public long getId() {
		return listId;
	}
	public void setId(long listId) {
		this.listId = listId;
	}
	public String getCategory() {
		return categoryName;
	}
	public void setCategory(String category) {
		this.categoryName = category;
	}
//	public ArrayList<Card> getCards() {
//		return cards;
//	}
//	public void setCards(ArrayList<Card> cards) {
//		this.cards = cards;
//	}
//	public CardList() {
//        this.cards = new ArrayList<>();
//    }
	
	@ManyToOne 
	@JoinColumn(name="boardId")
	@JsonBackReference
	private Board board;

	public Board getBoard() {
		return board;
	}
	public void setBoard(Board board) {
		this.board = board;
	}
	
	@OneToMany(mappedBy="cardList")
	private List <Card> card = new ArrayList<Card>();

	public List<Card> getCard() {
		return card;
	}
	public void setCard(List<Card> card) {
		this.card = card;
	}
	
}
