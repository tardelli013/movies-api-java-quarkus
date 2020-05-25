package br.com.tardelli.mocks;

import br.com.tardelli.model.Actor;
import br.com.tardelli.model.CensureLevel;
import br.com.tardelli.model.Director;
import br.com.tardelli.model.Movie;
import br.com.tardelli.utils.RandomUtils;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.IntStream;

public class MovieMocks {

  public static Movie movieMockWithEmptyRequiredParams() {
    final Integer numberOfActors = 3;
    final Movie movie = movieMockWithActors(numberOfActors, CensureLevel.CENSURED);
    movie.getActors().forEach(actor -> actor.setActorName(""));
    movie.getDirector().setDirectorName("");
    movie.setMovieName("");
    movie.setDate(null);
    return movie;
  }

  public static Movie movieMockWithActors(Integer numberOfActors, CensureLevel censureLevel) {
    Movie movie = new Movie();
    movie.setMovieName(RandomUtils.generateRandomString(50));
    movie.setDate(LocalDate.now().minusWeeks(RandomUtils.getRandomNumberInRange(0, 30)));
    movie.setActors(actorsMock(numberOfActors));
    movie.setCensureLevel(censureLevel);
    movie.setDirector(new Director(RandomUtils.generateRandomString(12)));

    return movie;
  }

  public static Set<Actor> actorsMock(Integer numberOfActors) {
    Set<Actor> actors = new HashSet<>();
    IntStream.range(0, numberOfActors).forEach(n -> actors.add(new Actor(RandomUtils.generateRandomString(10))));
    return actors;
  }

}
