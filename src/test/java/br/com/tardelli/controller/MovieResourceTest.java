package br.com.tardelli.controller;

import br.com.tardelli.mocks.MovieMocks;
import br.com.tardelli.model.CensureLevel;
import br.com.tardelli.model.Movie;
import br.com.tardelli.model.utils.PageableMovies;
import br.com.tardelli.repository.MovieRepository;
import br.com.tardelli.utils.RandomUtils;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.h2.H2DatabaseTestResource;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import javax.json.bind.JsonbBuilder;
import javax.transaction.Transactional;
import java.util.stream.IntStream;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTestResource(H2DatabaseTestResource.class)
@QuarkusTest
public class MovieResourceTest {

  @Inject
  private MovieRepository repositoryTest;

  @BeforeEach
  @Transactional
  void before() {
    repositoryTest.deleteAll();
  }

  @Test
  public void shouldPostMovieWith10Actors() {
    Integer numberOfActors = 10;
    Movie movieToPost = MovieMocks.movieMockWithActors(numberOfActors, CensureLevel.CENSURED);

    given()
        .when().contentType("application/json")
        .body(JsonbBuilder.create().toJson(movieToPost))
        .post("/movies")
        .then()
        .statusCode(201);

    Movie movieResponse = getMovie(movieToPost.getMovieName());
    assertEquals(movieResponse, movieToPost);
  }

  @Test
  public void shouldPostMovieWithMore10Actors() {
    final Integer numberOfActorsTest1 = 11;
    final Movie movieTest1 = MovieMocks.movieMockWithActors(numberOfActorsTest1, CensureLevel.CENSURED);

    given()
        .when().contentType("application/json")
        .body(JsonbBuilder.create().toJson(movieTest1))
        .post("/movies")
        .then()
        .statusCode(400)
        .body(containsString("actors"));

    final Integer numberOfActorsTest2 = 100;
    final Movie movieTest2 = MovieMocks.movieMockWithActors(numberOfActorsTest2, CensureLevel.NOT_CENSURED);

    given()
        .when().contentType("application/json")
        .body(JsonbBuilder.create().toJson(movieTest2))
        .post("/movies")
        .then()
        .statusCode(400)
        .body(containsString("actors"));

  }

  @Test
  public void shouldPostMovieWithEmptyRequiredParams() {
    final Movie movie = MovieMocks.movieMockWithEmptyRequiredParams();

    given()
        .when().contentType("application/json")
        .body(JsonbBuilder.create().toJson(movie))
        .post("/movies")
        .then()
        .statusCode(400)
        .body(containsString("actorName"),
            containsString("directorName"),
            containsString("movieName"),
            containsString("date"));

  }

  @Test
  public void shouldPostMovieAlreadyExists() {
    final Integer numberOfActors = 8;
    final Movie movieToPost = MovieMocks.movieMockWithActors(numberOfActors, CensureLevel.NOT_CENSURED);

    given()
        .when().contentType("application/json")
        .body(JsonbBuilder.create().toJson(movieToPost))
        .post("/movies")
        .then()
        .statusCode(201);


    given()
        .when().contentType("application/json")
        .body(JsonbBuilder.create().toJson(movieToPost))
        .post("/movies")
        .then()
        .statusCode(409)
        .body(is("Movie: '" + movieToPost.getMovieName() + "' already exists!"));
  }

  @Test
  public void shouldGetMovieNotFound() {
    final String movieName = RandomUtils.generateRandomString(80);

    given()
        .pathParam("name", movieName)
        .when().get("/movies/{name}")
        .then()
        .statusCode(404)
        .body(is("Movie: " + movieName + " was not found!"));

  }

  @Test
  public void shouldGetMovie() {
    Movie movieCreated = createAndPostMovie(CensureLevel.CENSURED);

    Movie movieResponse = given()
        .when().contentType("application/json")
        .get("/movies/" + movieCreated.getMovieName())
        .thenReturn().body().as(Movie.class);

    assertEquals(movieResponse, movieCreated);
  }

  @Test
  public void shouldDeleteMovie() {
    Movie movieCreated = createAndPostMovie(CensureLevel.CENSURED);

    given()
        .pathParam("name", movieCreated.getMovieName())
        .when().delete("/movies/{name}")
        .then()
        .statusCode(204);

    given()
        .pathParam("name", movieCreated.getMovieName())
        .when().contentType("application/json")
        .get("/movies/{name}")
        .then()
        .statusCode(404);
  }

  @Test
  public void shouldGetAllMoviesWithQueryParamsAndPaginatedByCensured() {
    final double paginationLimitDefault = 10;
    createAndPostMovies(RandomUtils.getRandomNumberInRange(1, 50).intValue(), CensureLevel.NOT_CENSURED);

    final int numberOfMoviesCensured = RandomUtils.getRandomNumberInRange(1, 50).intValue();
    createAndPostMovies(numberOfMoviesCensured, CensureLevel.CENSURED);

    PageableMovies pageableMoviesCensured =
        given()
            .param("censure", CensureLevel.CENSURED)
            .when().contentType("application/json")
            .get("/movies")
            .thenReturn().body().as(PageableMovies.class);

    assertEquals(numberOfMoviesCensured, pageableMoviesCensured.getTotalResults());
    assertEquals(Math.ceil(numberOfMoviesCensured / paginationLimitDefault), pageableMoviesCensured.getTotalPages());

  }

  @Test
  public void shouldGetAllMoviesWithQueryParamsAndPaginatedByNotCensured() {
    final double paginationLimitDefault = 10;
    createAndPostMovies(RandomUtils.getRandomNumberInRange(1, 50).intValue(), CensureLevel.CENSURED);

    final int numberOfMoviesNotCensured = RandomUtils.getRandomNumberInRange(1, 50).intValue();
    createAndPostMovies(numberOfMoviesNotCensured, CensureLevel.NOT_CENSURED);

    PageableMovies pageableMoviesNotCensured =
        given()
            .param("censure", CensureLevel.NOT_CENSURED)
            .when().contentType("application/json")
            .get("/movies")
            .thenReturn().body().as(PageableMovies.class);

    assertEquals(numberOfMoviesNotCensured, pageableMoviesNotCensured.getTotalResults());
    assertEquals(Math.ceil(numberOfMoviesNotCensured / paginationLimitDefault), pageableMoviesNotCensured.getTotalPages());
  }

  private void createAndPostMovies(Integer numberOfMovies, CensureLevel censureLevel) {
    IntStream.range(0, numberOfMovies).forEach(n -> createAndPostMovie(censureLevel));
  }

  private Movie createAndPostMovie(CensureLevel censureLevel) {
    Integer numberOfActors = 10;
    Movie movieToPost = MovieMocks.movieMockWithActors(numberOfActors, censureLevel);

    given()
        .when().contentType("application/json")
        .body(JsonbBuilder.create().toJson(movieToPost))
        .post("/movies")
        .then()
        .statusCode(201);

    return movieToPost;
  }

  private Movie getMovie(String name) {
    return given()
        .when().contentType("application/json")
        .get("/movies/" + name)
        .thenReturn().body().as(Movie.class);
  }
}
