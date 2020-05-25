package br.com.tardelli.controller;

import br.com.tardelli.exception.MovieNotFoundException;
import br.com.tardelli.model.CensureLevel;
import br.com.tardelli.model.Movie;
import br.com.tardelli.model.utils.PageableMovies;
import br.com.tardelli.repository.MovieRepository;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.ParameterIn;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jboss.resteasy.annotations.jaxrs.PathParam;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.validation.*;
import javax.validation.executable.ValidateOnExecution;
import javax.ws.rs.*;
import javax.ws.rs.Path;
import javax.ws.rs.container.ResourceContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.Optional;
import java.util.logging.Logger;

import static javax.ws.rs.core.Response.*;

@Path("/movies")
@RequestScoped
@ValidateOnExecution
public class MovieResource {
  private final static Logger LOGGER = Logger.getLogger(MovieResource.class.getName());

  private final MovieRepository repository;

  @Context
  ResourceContext resourceContext;
  @Context
  UriInfo uriInfo;

  @Inject
  public MovieResource(MovieRepository repository) {
    this.repository = repository;
  }

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Tag(name = "movies")
  @Operation(
      summary = "Create Movie",
      description = "Create movie"
  )
  @APIResponses(value = {
      @APIResponse(responseCode = "201", description = "Created"),
      @APIResponse(responseCode = "400", description = "Bad Request"),
      @APIResponse(responseCode = "409", description = "Movie already exists."),
      @APIResponse(responseCode = "500", description = "There was an internal server error.")
  }
  )
  public Response postMovie(@Valid Movie movie) {
    LOGGER.info("POST movie");
    Movie saved = this.repository.save(movie);
    return created(
        uriInfo.getBaseUriBuilder()
            .path("/movies/{name}")
            .build(saved.getMovieName())
    ).build();
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Operation(
      summary = "Get all Movies",
      description = "Get all movies"
  )
  @Tag(name = "movies")
  @APIResponses(value = {
      @APIResponse(responseCode = "200", name = "Movies list",
          content = @Content(
              mediaType = "application/json",
              schema = @Schema(
                  type = SchemaType.ARRAY,
                  implementation = PageableMovies.class
              )
          )
      ),
      @APIResponse(responseCode = "500", description = "There was an internal server error.")
  })
  public Response getAllMovies(
      @Parameter(name = "censure", required = true, in = ParameterIn.QUERY, description = "censured or no",
          schema = @Schema(
              type = SchemaType.STRING,
              implementation = CensureLevel.class
          )
      )
      @QueryParam("censure") String censure,
      @Parameter(name = "offset", in = ParameterIn.QUERY, description = "pagination offset")
      @QueryParam("offset") @DefaultValue("0") int offset,
      @Parameter(name = "limit", in = ParameterIn.QUERY, description = "pagination limit")
      @QueryParam("limit") @DefaultValue("10") int limit
  ) {
    LOGGER.info("GET movies");
    return ok(this.repository.findByCensured(CensureLevel.valueOf(censure), offset, limit)).build();
  }

  @Path("{name}")
  @GET
  @Operation(
      summary = "Get Movie",
      description = "Get movie by name"
  )
  @Tag(name = "movies")
  @APIResponses(value = {
      @APIResponse(responseCode = "200", name = "Movie",
          content = @Content(
              mediaType = "application/json",
              schema = @Schema(
                  type = SchemaType.OBJECT,
                  implementation = Movie.class
              )
          )
      ),
      @APIResponse(responseCode = "500", description = "There was an internal server error.")
  })
  @Produces(MediaType.APPLICATION_JSON)
  public Response getMovieByName(@PathParam("name") final String name) {
    LOGGER.info("GET movie by name");
    Optional<Movie> movie = this.repository.getByName(name);
    if (movie.isEmpty()) {
      throw new MovieNotFoundException(name);
    }
    return ok(movie).build();
  }

  @Path("{name}")
  @DELETE
  @Operation(
      summary = "Delete Movie",
      description = "Delete movie by name"
  )
  @Tag(name = "movies")
  @APIResponses(value = {
      @APIResponse(responseCode = "204", name = "Movie removed"),
      @APIResponse(responseCode = "500", description = "There was an internal server error.")
  })
  public Response deleteMovie(@PathParam("name") final String name) {
    LOGGER.info("DELETE movie by name");
    this.repository.deleteByName(name);
    return noContent().build();
  }
}
