package provider;

import provider.Card;
import provider.PlayerColor;

/**
 * This is the listener interface to listen to status changes in the model.
 */
public interface ModelStatusListener {

  /**
   * Invoked when the current player's turn changes.
   * This method is called by the model to notify listeners that the turn has switched
   * to a new player. Implementing classes should update their state to reflect the
   * current player and enable or disable user interactions as appropriate.
   *
   * @param currentPlayer the {@link PlayerColor} representing the player whose turn it is now
   */
  void onTurnChanged(PlayerColor currentPlayer);


  /**
   * Invoked when a card is placed on the grid.
   * This method is called by the model to notify listeners that a player has successfully
   * placed a card at the specified location on the game grid. Implementing classes should
   * update the game view to reflect the new state of the grid.
   *
   * @param card the {@link Card} that was placed on the grid
   * @param row  the row index (zero-based) where the card was placed
   * @param col  the column index (zero-based) where the card was placed
   */
  void onCardPlaced(Card card, int row, int col);

  /**
   * Invoked when the game is over.
   * This method is called by the model to notify listeners that the game has concluded.
   * The winner of the game is provided if there is one; otherwise, a {@code null} value
   * indicates a tie. Implementing classes should handle end-of-game logic, such as
   * displaying the final results to the user.
   *
   */
  void onGameOver(PlayerColor winner);
}
