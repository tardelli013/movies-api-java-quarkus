package br.com.tardelli.model;

public enum CensureLevel {

  CENSURED("CENSURED"),
  NOT_CENSURED("NOT_CENSURED");

  private final String level;

  CensureLevel(String level) {
    this.level = level;
  }

  public String getLevel() {
    return level;
  }

}
