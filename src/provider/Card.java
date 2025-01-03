package provider;

/**
 * This interface represents a card with name and attack values in different directions.
 */
public interface Card {

  /**
   * Get the name of the card.
   *
   * @return the name of the card
   */
  String getName();

  /**
   * Get the attack value facing north.
   *
   * @return the north attack value
   */
  int getNorthValue();

  /**
   * Get the attack value facing south.
   *
   * @return the south attack value
   */
  int getSouthValue();

  /**
   * Get the attack value facing east.
   *
   * @return the east attack value
   */
  int getEastValue();

  /**
   * Get the attack value facing west.
   *
   * @return the west attack value
   */
  int getWestValue();

  /**
   * Returns the attack value for a given direction.
   *
   * @param direction the direction to get the attack value for
   * @return the attack value in the specified direction
   * @throws IllegalArgumentException if an invalid direction is provided
   */
  int getAttackValue(Direction direction);

  /**
   * Converts this card to a string representation.
   *
   * @return a string representing this card with its name and attack values
   */
  String toString();

  /**
   * Checks if two cards are equal based on their name and attack values.
   *
   * @param obj the object to compare to this card
   * @return true if the cards are equal, false otherwise
   */
  boolean equals(Object obj);

  /**
   * Overriding the hashcode method to look up a card more easily.
   *
   * @return an integer value that represents a hash code
   */
  int hashCode();
}
