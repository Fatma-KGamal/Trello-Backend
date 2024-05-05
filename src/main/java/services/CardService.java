package services;

import java.util.ArrayList;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


import models.Card;
import models.CardList;
import models.User;

@Stateless
public class CardService {
	Card card;
	
	@PersistenceContext (name="trello")
	private EntityManager entityManager;
	
	
	public Card createCard(long listId,String name, String description,ArrayList<String> comment) {
		if (!User.isCurrentUser()) {
	        throw new IllegalArgumentException("User not logged in");
	    }
	   
	    CardList cardList = entityManager.find(CardList.class, listId);
	    if (cardList == null) {
	        throw new IllegalArgumentException("List not found");
	    }
	    
	     card = new Card();
	     card.setCardName(name);
	     card.setDescription(description);
	     card.setComment(comment);
	     card.setCardList(cardList);
	     entityManager.persist(card);
	     cardList.getCard().add(card);
	     entityManager.merge(cardList);
	
	    return card;
	}   
	
	

}
