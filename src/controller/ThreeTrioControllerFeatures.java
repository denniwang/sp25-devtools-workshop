package controller;

/**
 * Represents the features that the controller can use to interact with the view.
 */
public interface ThreeTrioControllerFeatures {
  /**
   * Sends a notification to the controller that other player made a move.
   * Model then tells view to send a notification to other controller.
   */
  void notifyPlay();

  /**
   * Sends a notification to the controller that the game is over.
   * Controller then tells view to display game over and announce winner and the score.
   */
  void notifyWinner();

  /**
   * Refresh the view to display the current state of the game.
   */
  void refresh();

  /**
   * Selects a card from the hand of a player.
   *
   * @param cardIdx     index of the selected card
   * @param isPlayerOne true if player one, false if player two
   */
  void selectCard(int cardIdx, boolean isPlayerOne);

  /**
   * Return the player using the controller.
   *
   * @return name of owner of controller.
   */
  String getUsername();

  /**
   * Plays a card from the player's hand to the board.
   *
   * @param row     the row to play the card to
   * @param col     the column to play the card to
   * @param cardIdx the index of the card to play
   */
  void playToBoard(int row, int col, int cardIdx);
}
