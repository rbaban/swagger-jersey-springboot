package demo.api.rest;

import java.net.URI;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

import demo.model.Artist;
import demo.service.ArtistService;

@Api(value = "/artists", description = "Endpoint for artist listing")
@Component
@Path("/artists")
@Produces(MediaType.APPLICATION_JSON)
public class ArtistRestEndpoint {

	@Autowired
	private ArtistService service;

	@Context
	private UriInfo uriInfo;

	@GET
	@ApiOperation(value = "Returns all artists", notes = "Returns a complete list of artists.", responseContainer = "array", response = Artist.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Successful retrieval of all artists", response = Artist.class),
			@ApiResponse(code = 500, message = "Internal server error") })
	public Response findAll() {
		Iterable<Artist> artists = service.findAll();
		return Response.ok(artists).build();
	}

	@GET
	@Path("{id}")
	@ApiOperation(value = "Returns artist given id", notes = "Returns an artist.", response = Artist.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Successful retrieval of the artist", response = Artist.class),
			@ApiResponse(code = 204, message = "Artist not found"),
			@ApiResponse(code = 500, message = "Internal server error") })
	public Response findOne(@PathParam("id") String id) {
		Artist artist = service.findOne(id);
		int status = artist == null ? 204 :200;
		return Response.status(status).entity(artist).build();
	}

	@DELETE
	@Path("{id}")
	@ApiOperation(value = "Removes artist given id", notes = "Removes an artist.", response = Artist.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Successful removal of the artist", response = Artist.class),
			@ApiResponse(code = 500, message = "Internal server error") })	
	public Response delete(@PathParam("id") String id) {
		service.delete(id);
		return Response.accepted().build();
	}

	@POST
	@ApiOperation(value = "Saves a new artist", notes = "Saves an artist.")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Successful saving of the artist"),
			@ApiResponse(code = 500, message = "Internal server error") })	
	public Response save(Artist artist) {
		service.save(artist);
		URI location = uriInfo.getAbsolutePathBuilder().path("{id}").resolveTemplate("id", artist.getId()).build();
		return Response.created(location).build();
	}

	@PUT
	@Path("{id}")
	@ApiOperation(value = "Updates an artist", notes = "Updates an artist.", response = Artist.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Successful update of the artist", response = Artist.class),
			@ApiResponse(code = 500, message = "Internal server error") })	
	public Response update(@PathParam("id") String id, Artist artist) {
		artist.setId(id);
		service.save(artist);
		return Response.ok(artist).build();
	}

}
