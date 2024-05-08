package controllers;




import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import models.Card;
import services.CardService;

@Stateless
@Path("card")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CardController {
	
	@Inject
	private CardService cardService;
	
	@PersistenceContext (name="trello")
	private EntityManager entityManager;
	
	@POST
	@Path("create/{listId}")
	public Response createCard(@PathParam("listId")long listId,Card card) {
		try {
	    cardService.createCard(listId,card);
        return Response.status(Response.Status.CREATED).entity("Card created successfully \n" ).build();
    } 
		catch (IllegalArgumentException e) 
		{
        return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
	}
	
	@PUT
	@Path("addComment/{cardId}")
	public Response addComment(@PathParam("cardId")long cardId,Card card) {
		try {
	    cardService.addComment(cardId,card);
        return Response.status(Response.Status.CREATED).entity("Comment added successfully \n" ).build();
    } 
		catch (IllegalArgumentException e) 
		{
        return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
	}
	
	@PUT
	@Path("addDescription/{cardId}")
	public Response addDescription(@PathParam("cardId")long cardId,Card card) {
		try {
	    cardService.addDescription(cardId,card);
        return Response.status(Response.Status.CREATED).entity("Description added successfully \n" ).build();
    } 
		catch (IllegalArgumentException e) 
		{
        return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
	}
	
	@PUT
	@Path("assignUser/{cardId}/{userId}")
	public Response assignUser(@PathParam("cardId") long cardId, @PathParam("userId") long userId) {
		try {
			cardService.assignCardToUser(cardId, userId);
			return Response.status(Response.Status.OK).entity("User assigned successfully").build();
		} catch (IllegalArgumentException e) {
			return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}
	
	@PUT
	@Path("moveCard/{cardId}/{listId}")
	public Response moveToList(@PathParam("cardId") long cardId, @PathParam("listId") long newListId) {
		try {
			cardService.moveCardToList(cardId, newListId);
			return Response.status(Response.Status.OK).entity("Card moved successfully").build();
		} catch (IllegalArgumentException e) {
			return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}
	
	@GET
	@Path("cards")
	public List<Card> getAllCards() {
		return cardService.getAllCards();
	}
}
