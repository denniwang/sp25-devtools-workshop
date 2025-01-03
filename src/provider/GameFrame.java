package provider;

/**
 * This is the interface that holds the methods to create the frame of the game.
 */
public interface GameFrame {

  /**
   * Initializes the game frame.
   */
  void initialize();

  /**
   * Refreshes the game frame to reflect the current state of the game.
   */
  void refresh();

  /**
   * Shows a message to the user.
   *
   * @param message the message to show
   */
  void showMessage(String message);

  /**
   * Sets the game event listener for the game frame.
   *
   * @param listener the game event listener
   */
  void setGameEventListener(EventListener listener);

  /**
   * Updates the player turn.
   *
   * @param currentPlayer the current player
   */
  void updatePlayerTurn(PlayerColor currentPlayer);

  /**
   * Highlights the selected card.
   *
   * @param card  the card to highlight
   * @param owner the owner of the card
   */
  void highlightSelectedCard(Card card, PlayerColor owner);
}
