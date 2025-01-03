package model;

/**
 * Represents an attack value in the game of ThreeTrio. Attack values are from 1 to A where
 * A represents 10.
 * In the game of ThreeTrio, a playable card will have four of these attack values.
 */
public enum Attack {
  ONE(1), TWO(2), THREE(3), FOUR(4), FIVE(5), SIX(6), SEVEN(7), EIGHT(8), NINE(9), A(10), APLUS(11);

  private final int value;

  Attack(int value) {
    this.value = value;
  }

  /**
   * Returns the attack value corresponding to the given integer value.
   *
   * @param value the integer value of the attack
   * @return the attack enum corresponding to the given integer value
   */
  public static Attack fromValue(int value) {
    if (value == 11) {
      System.out.println("APLUESS");
    }
    for (Attack attack : Attack.values()) {
      if (attack.getValue() == value) {
        return attack;
      }
    }
    throw new IllegalArgumentException("Invalid attack value: " + value);
  }

  /**
   * Returns the value of this attack.
   */
  public int getValue() {
    return this.value;
  }
}
