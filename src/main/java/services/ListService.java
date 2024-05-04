package services;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import models.CardList;

@Stateless
public class ListService {
	
	CardList cardList;
	
	@PersistenceContext (name="trello")
	private EntityManager entityManager;
	
	//Users can create lists within a board to categorize tasks.
	public CardList createList(String name) {
		cardList = new CardList();
		cardList.setCategory(name);
		entityManager.persist(cardList);
		return cardList;
	}
	
	//Users can delete a list.
	public void deleteList(String name) {
		cardList = entityManager.find(CardList.class, name);
		entityManager.remove(cardList);
	}
}
