package controllers;

import javax.ejb.Stateless;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import services.ListService;

@Stateless
@Path("list")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ListController {
	
	@Inject
	private ListService listService;
	
	@PersistenceContext (name="trello")
	private EntityManager entityManager;
	
	//Users can create lists within a board to categorize tasks.
	@POST
	@Path("/create/{userId}/{boardId}")
	public Response createList(@PathParam ("userId") long userId , @PathParam ("boardId") long boardId ,String name) {
		return listService.createList(userId,boardId,name);
	}
	
	//Users can delete a list.
	@DELETE
	@Path("/delete/{userId}/{boardId}/{listId}")
	public Response deleteList(@PathParam ("userId") long userId, @PathParam ("boardId") long boardId , @PathParam ("listId") long listId) {
		return listService.deleteList(userId,boardId,listId);
	}
}
