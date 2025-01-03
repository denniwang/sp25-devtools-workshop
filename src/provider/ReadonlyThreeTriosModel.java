package provider;

import java.util.List;

/**
 * This is the interface for the ReadonlyThreeTriosModel that implements some of the features
 * needed to view the game but not for any modifications.
 */
public interface ReadonlyThreeTriosModel {

  /**
   * Gets the current player.
   *
   * @return the current player
   */
  PlayerColor getCurrentPlayer();

  /**
   * Gets the owner of the card.
   *
   * @param card the card to get the owner of
   * @return the owner of the card
   */
  PlayerColor getCardOwner(Card card);

  /**
   * Get a copy of the game grid.
   *
   * @return the grid representation of the board
   */
  Grid getGridCopy();

  /**
   * Returns the hand of the current player.
   *
   * @return list of cards in the current player's hand
   */
  List<Card> getCurrentPlayerHand();

  /**
   * Returns  whether the game is over or not.
   *
   * @return true if the game is over, false otherwise
   */
  boolean isGameOver();

  /**
   * Returns whether the game has been won or not.
   *
   * @return true if the game has been won, false otherwise
   */
  boolean isGameWon();

  /**
   * Returns the winning player.
   *
   * @return the color of the winning player
   */
  PlayerColor getWinningPlayer();

  /**
   * Returns the score of the player.
   *
   * @param player the player to get the score of
   * @return the score of the player
   */
  int getScore(PlayerColor player);

  /**
   * Returns the hand of the red player.
   *
   * @return the hand of the red player
   */
  List<Card> getRedPlayerHand();


  /**
   * Returns the hand of the blue player.
   *
   * @return the hand of the blue player
   */
  List<Card> getBluePlayerHand();

}
