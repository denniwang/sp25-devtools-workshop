package playerstrategy;

import java.util.Objects;

import model.ThreeTrioCard;

/**
 * Represents a move in the game. A move is made up of a row, column, and card.
 */
public class StrategyMove implements Move {
  private int row;
  private int col;
  private ThreeTrioCard card;

  /**
   * Constructor for the Move class. A move is made up of a row, column, and card.
   *
   * @param row  the row of the move
   * @param col  the column of the move
   * @param card the card to play
   */
  public StrategyMove(int row, int col, ThreeTrioCard card) {
    this.row = row;
    this.col = col;
    this.card = card;
  }

  public int getRow() {
    return row;
  }

  public int getCol() {
    return col;
  }

  public ThreeTrioCard getCard() {
    return card;
  }

  /**
   * Returns a string representation of the move.
   *
   * @return a string representation of the move
   */
  public String toString() {
    return "Row: " + row + " Col: " + col + " Card: " + card.toString();
  }

  /**
   * Checks if two moves' names, and attack values are equal.
   *
   * @param obj other move to compare to
   * @return true if the move values are equal, false otherwise
   */
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (!(obj instanceof Move)) {
      return false;
    }
    StrategyMove move = (StrategyMove) obj;
    return this.row == move.row && this.col == move.col && this.card.equals(move.card);
  }

  @Override
  public int hashCode() {
    return Objects.hash(row, col, card);
  }
}
