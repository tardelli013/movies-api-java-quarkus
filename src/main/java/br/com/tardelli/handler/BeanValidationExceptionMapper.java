package br.com.tardelli.handler;

import javax.json.Json;
import javax.json.bind.JsonbBuilder;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

import static javax.ws.rs.core.Response.status;

@Provider
public class BeanValidationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {
  @Override
  public Response toResponse(ConstraintViolationException exception) {
  Map<String, String> errors = new HashMap<>();
    exception.getConstraintViolations()
        .forEach(v -> errors.put(lastFieldName(v.getPropertyPath().iterator()), v.getMessage()));

    return status(Response.Status.BAD_REQUEST).entity(Json.createValue(toJson(errors))).build();
  }

  private String toJson(Map<String, String> errors){
    return JsonbBuilder.create().toJson(errors);
  }

  public static String lastFieldName(Iterator<Path.Node> nodes) {
    Path.Node last = null;
    while (nodes.hasNext()) {
      last = nodes.next();
    }
    return Objects.requireNonNull(last).getName();
  }
}