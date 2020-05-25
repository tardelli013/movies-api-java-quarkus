package br.com.tardelli.utils;

import java.security.SecureRandom;
import java.util.Random;

public class RandomUtils {
  private static final String CHAR_LOWER = "abcdefghijklmnopqrstuvwxyz";
  private static final String CHAR_UPPER = CHAR_LOWER.toUpperCase();
  private static final String NUMBER = "0123456789";

  private static final String DATA_FOR_RANDOM_STRING = CHAR_LOWER + CHAR_UPPER + NUMBER;
  private static final SecureRandom random = new SecureRandom();

  public static String generateRandomString(int length) {
    if (length < 1) throw new IllegalArgumentException();
    StringBuilder sb = new StringBuilder(length);
    for (int i = 0; i < length; i++) {
      int rndCharAt = random.nextInt(DATA_FOR_RANDOM_STRING.length());
      char rndChar = DATA_FOR_RANDOM_STRING.charAt(rndCharAt);

      sb.append(rndChar);
    }
    return sb.toString();
  }

  public static Long getRandomNumberInRange(int min, int max) {
    if (min >= max) {
      throw new IllegalArgumentException("max must be greater than min");
    }
    Random r = new Random();
    return (long) (r.nextInt((max - min) + 1) + min);
  }
}
