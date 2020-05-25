package br.com.tardelli.model;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "movie")
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class Movie implements Serializable {

  @NotEmpty
  @Id
  private String movieName;

  @NotNull
  private LocalDate date;

  private CensureLevel censureLevel;

  @Valid
  private Director director;

  @Valid
  @Type(type = "jsonb")
  @Column(columnDefinition = "jsonb")
  @Size(min = 1, max = 10)
  private Set<Actor> actors = Collections.emptySet();

  private OffsetDateTime createdAt = OffsetDateTime.now();

  public String getMovieName() {
    return movieName;
  }

  public void setMovieName(String movieName) {
    this.movieName = movieName;
  }

  public CensureLevel getCensureLevel() {
    return censureLevel;
  }

  public void setCensureLevel(CensureLevel censureLevel) {
    this.censureLevel = censureLevel;
  }

  public Director getDirector() {
    return director;
  }

  public void setDirector(Director director) {
    this.director = director;
  }

  public Set<Actor> getActors() {
    return actors;
  }

  public void setActors(Set<Actor> actors) {
    this.actors = actors;
  }

  public OffsetDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(OffsetDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public LocalDate getDate() {
    return date;
  }

  public void setDate(LocalDate date) {
    this.date = date;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Movie movie = (Movie) o;

    if (!Objects.equals(movieName, movie.movieName)) return false;
    if (!Objects.equals(date, movie.date)) return false;
    if (censureLevel != movie.censureLevel) return false;
    if (!Objects.equals(director, movie.director)) return false;
    return Objects.equals(actors, movie.actors);
  }

  @Override
  public int hashCode() {
    int result = movieName != null ? movieName.hashCode() : 0;
    result = 31 * result + (date != null ? date.hashCode() : 0);
    result = 31 * result + (censureLevel != null ? censureLevel.hashCode() : 0);
    result = 31 * result + (director != null ? director.hashCode() : 0);
    result = 31 * result + (actors != null ? actors.hashCode() : 0);
    return result;
  }
}
