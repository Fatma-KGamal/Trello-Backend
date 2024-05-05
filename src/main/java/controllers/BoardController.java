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
	@Path("/create")
	public Response createBoard(String name) {
			try {
            Board newBoard= boardService.createBoard(name);
            return Response.status(Response.Status.CREATED).entity("Board created successfully \n" + newBoard).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }
	
	//Users can delete a board.
	@DELETE
	@Path("/delete/{id}")
	public Response deleteBoard(@PathParam("id") long id) {
		try {
			boardService.deleteBoard(id);
			return Response.status(Response.Status.OK).entity("Board deleted successfully").build();
		} catch (IllegalArgumentException e) {
			return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}
	
	//Users can view all boards they have access to.
	@GET
	@Path("/getBoards/{id}")
	public List<Board> getBoards(@PathParam("id") long id)
	{
		return boardService.getBoards(id);
	}
	
	//Users can invite other users to collaborate on a board.
	@PUT
	@Path("/inviteUser/{boardId}/{userId}")
	public Response inviteUser(@PathParam("boardId") long boardId, @PathParam("userId") long userId) {
		try {
			boardService.inviteUser(boardId, userId);
			return Response.status(Response.Status.OK).entity("User invited successfully").build();
		} catch (IllegalArgumentException e) {
			return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}
	
	//Users can view all boards
	@GET
	@Path("/boards")
	public List<Board> getAllBoards() {
		return boardService.getAllBoards();
	}

}
