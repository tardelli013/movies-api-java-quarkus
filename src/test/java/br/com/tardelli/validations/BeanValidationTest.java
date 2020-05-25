package br.com.tardelli.validations;

import br.com.tardelli.handler.BeanValidationExceptionMapper;
import br.com.tardelli.mocks.MovieMocks;
import br.com.tardelli.model.CensureLevel;
import br.com.tardelli.model.Movie;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@QuarkusTest
public class BeanValidationTest {

  @Inject
  Validator validator;

  @Test
  public void shouldBeanValidateRequiredParams() {
    Movie movie = MovieMocks.movieMockWithEmptyRequiredParams();

    Set<ConstraintViolation<Movie>> violations = validator.validate(movie);

    Map<String, String> errors = new HashMap<>();
    violations.forEach(v -> errors.put(BeanValidationExceptionMapper.lastFieldName(v.getPropertyPath().iterator()), v.getMessage()));

    assertTrue(errors.containsKey("movieName"));
    assertTrue(errors.containsKey("date"));
    assertTrue(errors.containsKey("directorName"));
    assertTrue(errors.containsKey("actorName"));

  }

  @Test
  public void shouldBeanValidateMovieWith11Actors() {
    Movie movie = MovieMocks.movieMockWithActors(11, CensureLevel.CENSURED);

    Set<ConstraintViolation<Movie>> violations = validator.validate(movie);

    Map<String, String> errors = new HashMap<>();
    violations.forEach(v -> errors.put(BeanValidationExceptionMapper.lastFieldName(v.getPropertyPath().iterator()), v.getMessage()));

    assertTrue(errors.containsKey("actors"));
  }

  @Test
  public void shouldBeanValidateMovieWith10Actors() {
    Movie movie = MovieMocks.movieMockWithActors(10, CensureLevel.CENSURED);
    Set<ConstraintViolation<Movie>> violations = validator.validate(movie);

    assertTrue(violations.isEmpty());
  }
}
