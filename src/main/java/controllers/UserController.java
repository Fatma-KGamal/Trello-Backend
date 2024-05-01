package controllers;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.PathParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
//import javax.ws.rs.FormParam;

import models.User;
import services.UserService;

@Stateless
@Path("user")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserController {
	
	@Inject
    private UserService userService;
	
	@PersistenceContext
    private EntityManager entityManager;
	
	@POST
	@Path("/register")
	public Response register(User user) {
		try {
			userService.register(user);
			return Response.status(Response.Status.CREATED).entity("User registered successfully").build();
			} catch (IllegalArgumentException e) {
				return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
				}
		}
	
	
	@POST
    @Path("/login")
    public Response login(@QueryParam("email") String email, @QueryParam("password") String password) {
        try {
            userService.login(email, password);
            return Response.status(Response.Status.OK).entity("Login successful").build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.UNAUTHORIZED).entity(e.getMessage()).build();
        }
    }
	
	@PUT
	@Path("/editprofile/{id}")
	public Response editProfile(@PathParam("id") long userId, @QueryParam("username") String newUsername, @QueryParam("email") String newEmail, @QueryParam("password") String newPassword) {
	    try {
	        userService.editProfile(userId, newUsername, newEmail, newPassword);
	        return Response.status(Response.Status.OK).entity("User profile updated successfully").build();
	    } catch (IllegalArgumentException e) {
	        return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
	    }
	}
	
}
