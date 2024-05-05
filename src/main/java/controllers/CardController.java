package controllers;

import java.util.ArrayList;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

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
	public Response createBoard(@PathParam("listId")long listId, String name, String description,ArrayList<String> comment) {
		try {
	    cardService.createCard(listId,name,description,comment);
        return Response.status(Response.Status.CREATED).entity("Card created successfully \n" ).build();
    } catch (IllegalArgumentException e) {
        return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
    }
}

}
