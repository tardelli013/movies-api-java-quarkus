package br.com.tardelli.model.utils;

import br.com.tardelli.model.Movie;

import java.util.Collections;
import java.util.List;

public class PageableMovies {

  private Long totalResults;
  private int totalPages;
  private int actualPage;
  private List<Movie> movies = Collections.emptyList();

  public static PageableMovies of(Long totalResults, int totalPages, int actualPage, List<Movie> movies) {
    PageableMovies pageableMovies = new PageableMovies();
    pageableMovies.setTotalResults(totalResults);
    pageableMovies.setActualPage(actualPage);
    pageableMovies.setMovies(movies);
    pageableMovies.setTotalPages(totalPages);

    return pageableMovies;
  }

  public Long getTotalResults() {
    return totalResults;
  }

  public void setTotalResults(Long totalResults) {
    this.totalResults = totalResults;
  }

  public int getActualPage() {
    return actualPage;
  }

  public void setActualPage(int actualPage) {
    this.actualPage = actualPage;
  }

  public List<Movie> getMovies() {
    return movies;
  }

  public void setMovies(List<Movie> movies) {
    this.movies = movies;
  }

  public int getTotalPages() {
    return totalPages;
  }

  public void setTotalPages(int totalPages) {
    this.totalPages = totalPages;
  }
}
