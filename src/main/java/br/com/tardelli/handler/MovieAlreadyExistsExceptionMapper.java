package br.com.tardelli.handler;

import br.com.tardelli.exception.MovieAlreadyExistsException;
import br.com.tardelli.exception.MovieNotFoundException;

import javax.json.Json;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import static javax.ws.rs.core.Response.status;

@Provider
public class MovieAlreadyExistsExceptionMapper implements ExceptionMapper<MovieAlreadyExistsException> {
  @Override
  public Response toResponse(MovieAlreadyExistsException exception) {
    return status(Response.Status.CONFLICT).entity(exception.getMessage()).build();
  }
}
