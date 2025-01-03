package player;

/**
 * Represents a player action for the ThreeTrio game.
 */
public interface PlayerAction {

  /**
   * Places the selected card on the board.
   *
   * @param row     the row to place the card
   * @param col     the column to place the card
   * @param cardIdx the index of the card in the player's hand
   */
  public void placeCard(int row, int col, int cardIdx);
}
