package br.com.tardelli.repository;

import br.com.tardelli.exception.MovieAlreadyExistsException;
import br.com.tardelli.model.CensureLevel;
import br.com.tardelli.model.Movie;
import br.com.tardelli.model.utils.PageableMovies;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.hibernate.orm.panache.runtime.JpaOperations;
import io.quarkus.panache.common.Sort;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.Optional;
import java.util.logging.Logger;

@ApplicationScoped
public class MovieRepository implements PanacheRepositoryBase<Movie, String> {
  private final static Logger LOGGER = Logger.getLogger(MovieRepository.class.getName());

  public PageableMovies findByCensured(CensureLevel censureLevel, int offset, int size) {
    PanacheQuery<Movie> queryPage = this.find("censureLevel = ?1 ", Sort.descending("createdAt"), censureLevel)
        .page(offset / size, size);

    return PageableMovies.of(queryPage.count(), queryPage.pageCount(), offset, queryPage.list());
  }

  public Optional<Movie> getByName(String name) {
    Movie movie = this.findById(name);
    return Optional.ofNullable(movie);
  }

  @Transactional
  public Movie save(Movie movie) {
    if (getByName(movie.getMovieName()).isEmpty()) {
      EntityManager em = JpaOperations.getEntityManager();
      em.persist(movie);
      return movie;
    } else {
      LOGGER.info("Movie = '" + movie.getMovieName() + "' already exists");
      throw new MovieAlreadyExistsException(movie.getMovieName());
    }
  }

  @Transactional
  public void deleteByName(String name) {
    this.delete("movieName=?1", name);
  }

}
