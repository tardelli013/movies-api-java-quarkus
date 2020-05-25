package br.com.tardelli.model;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.Objects;

public class Actor implements Serializable {

  @NotEmpty
  private String actorName;

  public Actor() {
  }

  public Actor(@NotEmpty String actorName) {
    this.actorName = actorName;
  }

  public String getActorName() {
    return actorName;
  }

  public void setActorName(String actorName) {
    this.actorName = actorName;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Actor actor = (Actor) o;

    return Objects.equals(actorName, actor.actorName);
  }

  @Override
  public int hashCode() {
    return actorName != null ? actorName.hashCode() : 0;
  }
}
