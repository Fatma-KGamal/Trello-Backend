package controllers;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import models.Board;
import services.BoardService;

@Stateless
@Path("board")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BoardController {
	
	@Inject
	private BoardService boardService;
	@PersistenceContext (name="trello")
	private EntityManager entityManager;
	
	//Users can create a new board with a unique name.
	@POST
	@Path("/create/{id}")
	public Response createBoard(@PathParam("id") long userId, String name) {
		return boardService.createBoard(userId, name);
    }
	
	//Users can delete a board.
	@DELETE
	@Path("/delete")
	public Response deleteBoard(@QueryParam("userId") long userId, @QueryParam("boardId") long boardId) {
		try {
			boardService.deleteBoard(userId, boardId);
			return Response.status(Response.Status.OK).entity("Board deleted successfully").build();
		} catch (IllegalArgumentException e) {
			return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}
	
	//Users can view all boards they have access to.
	@GET
	@Path("/getBoards/{teamLeaderId}/{userId}")
	public Response getBoards(@PathParam("teamLeaderId") long teamLeaderId,@PathParam("userId") long userId)
	{
		return boardService.getBoards(teamLeaderId,userId);
	}
	
	//Users can invite other users to collaborate on a board.
	@PUT
	@Path("/inviteUser/{userId}/{boardId}")
	public Response inviteUser( @PathParam("userId") long userId,@PathParam("boardId") long boardId) {
		return boardService.inviteUser( userId, boardId);
	}
	
	//Users can view all boards
	@GET
	@Path("/boards")
	public List<Board> getAllBoards() {
		return boardService.getAllBoards();
	}

}
