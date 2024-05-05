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
	@Path("/create/{boardId}")
	public Response createList(@PathParam ("boardId") long boardId ,String name) {
		try {
			listService.createList(boardId,name);
			return Response.status(Response.Status.CREATED).entity("List created successfully ").build();
		} catch (IllegalArgumentException e) {
			return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}
	
	//Users can delete a list.
	@DELETE
	@Path("/delete/{boardId}/{listId}")
	public Response deleteList(@PathParam ("boardId") long boardId , @PathParam ("listId") long listId) {
		try {
			listService.deleteList(boardId,listId);
			return Response.status(Response.Status.OK).entity("List deleted successfully").build();
		} catch (IllegalArgumentException e) {
			return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}
}
