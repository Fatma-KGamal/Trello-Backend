package services;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.ws.rs.core.Response;

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
	
	

	public Response createCard(long listId,Card currentCard) {

		if (User.isCurrentUser() == false) {
			return Response.status(Response.Status.BAD_REQUEST).entity("User is not logged in").build();
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
	     return Response.status(Response.Status.CREATED).entity("Card created successfully \n" +card ).build();
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
	
	
	 public Response assignCardToUser(long cardId, long userId) {
		 User user ;
		 	if (User.isCurrentUser() == false) {
		 		return Response.status(Response.Status.BAD_REQUEST).entity("User is not logged in").build();
		 		}
		 	else {
		 		 
	        card = entityManager.find(Card.class, cardId);
	        user = entityManager.find(User.class, userId); 
	        
	        boolean isUserInvited = card.getCardList().getBoard().getUsers().contains(user);
	 	    if (isUserInvited == false) {
	 	    	return Response.status(Response.Status.BAD_REQUEST).entity("User is not invited in the board ").build();
	 	    }
	 	    else {
	        user.getUserCards().add(card);
	        card.getAssignedUsers().add(user);
	        entityManager.merge(card);
	        entityManager.merge(user);
	        return Response.status(Response.Status.CREATED).entity(" User assigned to " + card.getCardName() + " card successfully \n" ).build();
		 	}
		 	}
	    }
	 
	 public Response moveCardToList(long cardId, long newListId) {
	        if (User.isCurrentUser() == false) {
	        	return Response.status(Response.Status.BAD_REQUEST).entity("User is not logged in").build();
		 		} else {
	            card = entityManager.find(Card.class, cardId);
	            cardList = entityManager.find(CardList.class, newListId);

	            if (cardList == null) {
	            	return Response.status(Response.Status.BAD_REQUEST).entity("List not found").build();
			 		}

	            CardList oldList = card.getCardList();
	            oldList.getCard().remove(card);

	            card.setCardList(cardList);
	            cardList.getCard().add(card);

	            entityManager.merge(card);
	            entityManager.merge(oldList);
	            entityManager.merge(cardList);
	            return Response.status(Response.Status.OK).entity("Card moved to "+ cardList.getCategory() + " successfully ").build();
	    		
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
