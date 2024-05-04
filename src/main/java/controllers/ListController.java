package controllers;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
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
	@Path("/create")
	public Response createList(String name) {
		try {
			listService.createList(name);
			return Response.status(Response.Status.CREATED).entity("List created successfully").build();
		} catch (IllegalArgumentException e) {
			return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}
	
	//Users can delete a list.
	@DELETE
	@Path("/delete")
	public Response deleteList(String name) {
		try {
			listService.deleteList(name);
			return Response.status(Response.Status.OK).entity("List deleted successfully").build();
		} catch (IllegalArgumentException e) {
			return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}
}
