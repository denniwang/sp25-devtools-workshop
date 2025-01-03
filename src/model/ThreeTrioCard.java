package model;


import java.util.HashMap;

/**
 * Interface to represent a playing card in the game.
 */
public interface ThreeTrioCard {

  /**
   * Returns the playing card formatted including its name and attack values.
   *
   * @return the playing card formatted including its name and attack values
   */
  String toString();

  /**
   * Returns the name of this card.
   *
   * @return the name of this card
   */
  String getName();

  /**
   * Returns whether this card is a hole card.
   *
   * @return whether this card is a hole card
   */
  boolean isHole();

  /**
   * Returns the color of this card.
   *
   * @return the color of this card
   */
  Color getColor();

  /**
   * Sets the color of this card.
   *
   * @param color the color to set this card to
   */
  void setColor(Color color);

  /**
   * Returns the short string representation of this card.
   *
   * @return the short string representation of this card
   */
  String shortString();

  /**
   * Returns all attack values for this card.
   *
   * @return all attack values for this card
   */
  HashMap<Direction, Attack> getAttacks();

  /**
   * Returns a deep copy of this card.
   *
   * @return a deep copy of this card
   */
  ThreeTrioCard deepCopy();

}
