package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonBackReference;


@Entity
public class Card {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String cardName;
	private String description;
	private ArrayList<String> comment;
	
	 
	@Override
	public String toString() {
		return "Card [id=" + id + ", cardName=" + cardName + ", description=" + description + ", comment=" + comment
				+ ", cardList=" + cardList.getCategory() + ", assignedUsers=" + assignedUsers + "]";
	}
	public long getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getCardName() {
		return cardName;
	}
	public void setCardName(String cardName) {
		this.cardName = cardName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public ArrayList<String> getComment() {
		return comment;
	}
	public void setComment(ArrayList<String> comment) {
		this.comment = comment;
	}
	
	@ManyToOne
	@JoinColumn(name="listId")
	@JsonBackReference
	private CardList cardList;
	
	public CardList getCardList()
	{
		return cardList;
	}
	
	public void setCardList(CardList cardList)
	{
		this.cardList = cardList;
	}
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "user_card", joinColumns = @JoinColumn(name = "card_id"), inverseJoinColumns = @JoinColumn(name = "id"))
	private List <User> assignedUsers = new ArrayList<User>();
	

	public List<User> getAssignedUsers() {
		return assignedUsers;
	}
	public void setAssignedUsers(List<User> user) {
		this.assignedUsers = user;
	}
	
}
