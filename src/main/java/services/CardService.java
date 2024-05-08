package services;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import messaging.JMSClient;
import models.Card;
import models.CardList;
import models.User;

@Stateless
public class CardService {
	Card card;
	CardList cardList;
	
	@PersistenceContext (name="trello")
	private EntityManager entityManager;
	
	
	
	public Card createCard(long listId,Card currentCard) {
		if (User.isCurrentUser() == false) {
			throw new IllegalArgumentException("User not logged in");
		}
	    cardList = entityManager.find(CardList.class, listId);
	    if (cardList == null) {
	        throw new IllegalArgumentException("List not found");
	    }
	    
	     card = new Card();
	     card.setCardName(currentCard.getCardName());
	     card.setDescription(currentCard.getDescription());
	     card.setComment(currentCard.getComment());
	     card.setCardList(cardList);
	     entityManager.persist(card);
	     cardList.getCard().add(card);
	     entityManager.merge(cardList);
	    return card;
	}   
	
	 public void addComment(long cardId,Card commentCard)
	 {
		 
		card = entityManager.find(Card.class, cardId);
		ArrayList <String> comment = card.getComment();
		if(comment==null) {
			card.setComment(commentCard.getComment());
			entityManager.merge(card);
			return ;
		}
		comment.addAll(commentCard.getComment());
		card.setComment(comment);
		entityManager.merge(card);
	 }
	 
	 public void addDescription(long cardId,Card descriptionCard)
	 {
		card = entityManager.find(Card.class, cardId);
		card.setDescription(descriptionCard.getDescription());
		entityManager.merge(card);
	 }
	
	
	 public void assignCardToUser(long cardId, long userId) {
		 User user ;
		 	if (User.isCurrentUser() == false) {
				throw new IllegalArgumentException("User not logged in");
			}
		 	else {
		 		 
	        card = entityManager.find(Card.class, cardId);
	        user = entityManager.find(User.class, userId); 
	        
	        boolean isUserInvited = card.getCardList().getBoard().getUsers().contains(user);
	 	    if (isUserInvited == false) {
	 	        throw new IllegalArgumentException("User is not invited to the board");
	 	    }
	 	    else {
	        user.getUserCards().add(card);
	        card.getAssignedUsers().add(user);
	        entityManager.merge(card);
	        entityManager.merge(user);
		 	}
		 	}
	    }
	 
	 public void moveCardToList(long cardId, long newListId) {
	        if (User.isCurrentUser() == false) {
	            throw new IllegalArgumentException("User not logged in");
	        } else {
	            card = entityManager.find(Card.class, cardId);
	            cardList = entityManager.find(CardList.class, newListId);

	            if (cardList == null) {
	                throw new IllegalArgumentException("List not found");
	            }

	            CardList oldList = card.getCardList();
	            oldList.getCard().remove(card);

	            card.setCardList(cardList);
	            cardList.getCard().add(card);

	            entityManager.merge(card);
	            entityManager.merge(oldList);
	            entityManager.merge(cardList);
	        }
	    }
	 public List<Card> getAllCards() {
			if (User.isCurrentUser() == false) {
				throw new IllegalArgumentException("User not logged in");
			}
			else {
			TypedQuery<Card> query = entityManager.createQuery("SELECT c from Card c LEFT JOIN FETCH c.cardList ", Card.class);
			return query.getResultList();
			}
		}
}
