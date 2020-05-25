package br.com.tardelli.exception;

public class MovieNotFoundException extends RuntimeException {
  public MovieNotFoundException(String name) {
    super("Movie: " + name + " was not found!");
  }
}
