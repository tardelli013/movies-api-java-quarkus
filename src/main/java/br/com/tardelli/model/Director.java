package br.com.tardelli.model;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.Objects;

public class Director implements Serializable {

  @NotEmpty
  private String directorName;

  public Director() {
  }

  public Director(@NotEmpty String directorName) {
    this.directorName = directorName;
  }

  public String getDirectorName() {
    return directorName;
  }

  public void setDirectorName(String directorName) {
    this.directorName = directorName;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Director director = (Director) o;

    return Objects.equals(directorName, director.directorName);
  }

  @Override
  public int hashCode() {
    return directorName != null ? directorName.hashCode() : 0;
  }
}
