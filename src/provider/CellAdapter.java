package provider;

import model.PlayingCard;
import model.ThreeTrioCard;

/**
 * This class adapts the PlayingCard class to the Cell interface.
 */
public class CellAdapter extends PlayingCard implements Cell {
  private ThreeTrioCard card;
  private int row;
  private int col;

  /**
   * Constructs a cell adapter with a hole.
   *
   * @param hole whether the cell is a hole
   */
  public CellAdapter(boolean hole) {
    if (hole) {
      card = new PlayingCard(hole);
    } else {
      card = new PlayingCard();
    }
  }

  /**
   * Constructs a cell adapter with a card.
   *
   * @param row  the row of the cell
   * @param col  the column of the cell
   * @param card the card to be placed in the cell
   */
  public CellAdapter(int row, int col, ThreeTrioCard card) {
    this.row = row;
    this.col = col;
    if (card.isHole()) {
      this.card = null;
    } else {
      this.card = card;
    }
  }

  @Override
  public CellType getType() {
    if (card.isHole()) {
      return CellType.HOLE;
    } else {
      return CellType.CARD_CELL;
    }
  }

  @Override
  public boolean isEmpty() {
    return card.getName() == null;
  }

  @Override
  public Card getCard() {
    if (card.getName() == null) {
      return null;
      //System.out.println("TRIYNG TO CREATE A NULL CARD :((");
    }
    return new CardAdapter(card);
  }

  @Override
  public void setCard(Card card) {
    if (this.card.isHole()) {
      throw new IllegalArgumentException("cannot place a card at a hole");
    } else if (this.card.getName() != null) {
      throw new IllegalArgumentException("cannot place a card at an already occupied cell");
    } else {
      this.card = new PlayingCard(card.getName(), card.getNorthValue(), card.getEastValue(),
              card.getSouthValue(), card.getWestValue());
    }
  }

  @Override
  public String toString() {
    return "CellAdapter{" +
            "card=" + card +
            ", row=" + row +
            ", col=" + col +
            '}';
  }
}
