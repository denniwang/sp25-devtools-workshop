package model;

import java.util.HashMap;
import java.util.Objects;

/**
 * Class to represent a playing game in a game of ThreeTrioCard. A playing card is one
 * of three types:
 * 1. A playable card that has a name and four associated attack values for each direction
 * specified in the Direction enum.
 * 2. A hole card that has no name, no attack values, and is not playable.
 * 3. A tile card represents a tile on the board where a card can be played to.
 */
public class PlayingCard implements ThreeTrioCard {
  private String name;
  private HashMap<Direction, Attack> attacks;
  private Color color;
  private boolean isHole;

  /**
   * Constructor for a playing card.
   *
   * @param name  the name of the card
   * @param north the attack value of the card in the north direction
   * @param south the attack value of the card in the south direction
   * @param east  the attack value of the card in the east direction
   * @param west  the attack value of the card in the west direction
   */
  public PlayingCard(String name, Attack north, Attack south, Attack east, Attack west) {
    this.attacks = new HashMap<>();
    this.name = name;
    this.attacks.put(Direction.NORTH, north);
    this.attacks.put(Direction.SOUTH, south);
    this.attacks.put(Direction.EAST, east);
    this.attacks.put(Direction.WEST, west);
    this.color = null;
    this.isHole = false;
  }

  /**
   * Constructor for .
   *
   * @param other the card to copy
   */
  public PlayingCard(PlayingCard other) {
    if (other.attacks == null) {
      System.out.println("OTHER ATTACKS IS NULL!!!");
    }
    this.name = other.name;
    this.attacks = new HashMap<>(other.attacks);
    this.color = other.color;
    this.isHole = other.isHole;
  }

  /**
   * Constructor for a hole card.
   *
   * @param isHole whether the card is a hole card
   */
  public PlayingCard(boolean isHole) {
    this.isHole = isHole;
    this.name = null;
  }

  /**
   * Constructor for dummy card to represent a tile that is not a playing card.
   */
  public PlayingCard() {
    this.isHole = false;
    this.name = null;
  }

  /**
   * Constructor for a playing card with a name and attack values for each direction.
   *
   * @param name       the name of the card
   * @param northValue the attack value of the card in the north direction
   * @param eastValue  the attack value of the card in the east direction
   * @param southValue the attack value of the card in the south direction
   * @param westValue  the attack value of the card in the west direction
   */
  public PlayingCard(String name, int northValue, int eastValue, int southValue, int westValue) {
    this.name = name;
    this.attacks = new HashMap<>();
    this.attacks.put(Direction.NORTH, Attack.fromValue(northValue));
    this.attacks.put(Direction.SOUTH, Attack.fromValue(southValue));
    this.attacks.put(Direction.EAST, Attack.fromValue(eastValue));
    this.attacks.put(Direction.WEST, Attack.fromValue(westValue));

  }

  /**
   * Returns the playing card formatted as a single character string for the board.
   */
  public String shortString() {
    if (this.isHole) {
      return "X";
    } else if (this.name == null || this.color == null) {
      return "_";
    } else {
      return color.toString();
    }
  }


  @Override
  public String toString() {
    if (isHole) {
      return "Hole";
    } else if (name == null) {
      return "Tile";
    }
    StringBuilder sb = new StringBuilder();
    sb.append(this.name + " ");
    sb.append(attacks.get(Direction.NORTH).getValue() + " ");
    sb.append(attacks.get(Direction.SOUTH).getValue() + " ");
    sb.append(attacks.get(Direction.EAST).getValue() + " ");
    sb.append(attacks.get(Direction.WEST).getValue());
    return sb.toString();
  }

  /**
   * Returns the name of this card.
   */
  public String getName() {
    return this.name;
  }

  /**
   * Returns whether this card is a hole card.
   */
  public boolean isHole() {
    return this.isHole;
  }

  /**
   * Returns the color of this card.
   */
  public Color getColor() {
    return this.color;
  }

  /**
   * Sets the color of this card.
   */
  public void setColor(Color color) {
    this.color = color;
  }

  /**
   * Returns the attacks of this card, in a map of direction to attack.
   *
   * @return the attacks of this card, in a hashmap format
   */
  public HashMap<Direction, Attack> getAttacks() {
    if (isHole) {
      System.out.println("GETTING ATTACKS OF HOLE CARD!!!");
    }
    return this.attacks;
  }

  /**
   * Returns a deep copy of this card, copying over name and attacks.
   *
   * @return a deep copy of this card
   */
  public PlayingCard deepCopy() {
    if (this.name == null && !this.isHole) {
      return new PlayingCard();
    } else if (this.isHole) {
      return new PlayingCard(true);
    } else {
      PlayingCard newCard = new PlayingCard(this.name,
              this.attacks.get(Direction.NORTH),
              this.attacks.get(Direction.SOUTH),
              this.attacks.get(Direction.EAST),
              this.attacks.get(Direction.WEST));
      newCard.setColor(this.color);
      return newCard;
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof PlayingCard)) {
      return false;
    }
    PlayingCard that = (PlayingCard) o;
    return Objects.equals(this.name, that.name) &&
            Objects.equals(this.color, that.color) &&
            Objects.equals(this.attacks, that.attacks);
  }


  @Override
  public int hashCode() {
    return Objects.hash(name, color, attacks);
  }
}
