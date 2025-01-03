package provider;


import model.Attack;
import model.Color;
import model.Direction;
import model.PlayingCard;
import model.ThreeTrioCard;

/**
 * This class adapts the PlayingCard class to the Card interface.
 */
public class CardAdapter extends PlayingCard implements Card {
  /**
   * Constructor for the CardAdapter class.
   *
   * @param name  the name of the card
   * @param north the attack value facing north
   * @param south the attack value facing south
   * @param east  the attack value facing east
   * @param west  the attack value facing west
   */
  public CardAdapter(String name, Attack north, Attack south, Attack east, Attack west) {
    super(name, north, south, east, west);
  }

  /**
   * Constructor for the CardAdapter class, constructs a cardadapter using a ThreeTrioCard.
   *
   * @param other the ThreeTrioCard to adapt
   */
  public CardAdapter(ThreeTrioCard other) {
    super((PlayingCard) other);
  }

  /**
   * Constructor a tile for the CardAdapter class.
   */
  public CardAdapter() {
    super();
  }


  @Override
  public int getNorthValue() {
    return this.getAttacks().get(model.Direction.NORTH).getValue();
  }

  @Override
  public int getSouthValue() {
    return this.getAttacks().get(model.Direction.SOUTH).getValue();
  }

  @Override
  public int getEastValue() {
    return this.getAttacks().get(model.Direction.EAST).getValue();
  }

  @Override
  public int getWestValue() {
    return this.getAttacks().get(model.Direction.WEST).getValue();
  }

  @Override
  public int getAttackValue(provider.Direction direction) {
    return this.getAttacks().get(Direction.fromProviderDirection(direction)).getValue();
  }

  /**
   * Get the color of the card.
   *
   * @return the color of the card
   */
  public Color getColor() {
    return super.getColor();

  }

  /**
   * Set the color of the card.
   *
   * @param color the color to set this card to
   */
  public void setColor(Color color) {
    super.setColor(color);
  }
}
