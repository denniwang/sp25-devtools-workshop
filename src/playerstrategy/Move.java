package playerstrategy;


import model.ThreeTrioCard;

/**
 * Represents a move in the game. This move is represented by a row, column, and card.
 * Where the card is played to the row and column on the board.
 */
public interface Move {
  /**
   * Gets the row of the move.
   *
   * @return the row of the move
   */
  int getRow();

  /**
   * Gets the column of the move.
   *
   * @return the column of the move
   */
  int getCol();

  /**
   * Gets the card of the move.
   *
   * @return the card of the move
   */
  ThreeTrioCard getCard();
}