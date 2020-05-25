package br.com.tardelli.exception;

public class MovieAlreadyExistsException extends RuntimeException {
  public MovieAlreadyExistsException(String name) {
    super("Movie: '" + name + "' already exists!");
  }
}
