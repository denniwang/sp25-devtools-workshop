package model;

import java.util.List;

import player.Player;


/**
 * Represents a readonly model for the ThreeTrio game. So no other sneaky classes can modify the
 * game state.
 *
 * @param <C> the type of card that the model uses
 */
public interface ReadonlyThreeTrioModel<C extends ThreeTrioCard> {
  /**
   * Returns whether the game is over.
   *
   * @throws IllegalStateException if the game has not started.
   */
  boolean isGameOver();

  /**
   * Get the winner of the game.
   *
   * @throws IllegalStateException if the game is not over.
   */
  Color getWinner();

  /**
   * Iterates through the board and returns the number of tiles.
   *
   * @return the number of tiles on the board.
   * @throws IllegalStateException if the board has not been initialized.
   */
  int getNumTiles();

  /**
   * Returns the hand of the current player.
   *
   * @return hand of current player as a List of TTCard
   */
  List<C> getPlayerHand();

  /**
   * Returns the hand of the opponent of the current player.
   *
   * @return hand of opponent as a List of TTCard
   */
  List<C> getOtherPlayerHand();

  /**
   * Returns the hand of player one.
   *
   * @return hand of player one as a List of TTCard
   */
  List<C> getPlayerOneHand();

  /**
   * Returns the hand of player two.
   *
   * @return hand of player two as a List of TTCard
   */
  List<C> getPlayerTwoHand();

  /**
   * Returns the board of the game as a 2D array of TTCards.
   *
   * @return a 2d array of ThreeTruoCards representing the board.
   */
  C[][] getBoard();

  /**
   * Returns the current player's turn.
   *
   * @return true if player one's turn, false if player two's turn
   */
  boolean getTurn();

  /**
   * Returns the card at the given row and column.
   *
   * @param row the row of the card
   * @param col the column of the card
   * @return the card at the given row and column
   * @throws IllegalArgumentException if the given row and col is out of bounds
   */
  C getCard(int row, int col);

  /**
   * Returns the width of the board 2d array.
   *
   * @return number of cols in the board
   */
  int getBoardW();

  /**
   * Return the height of the board 2d array.
   *
   * @return number of rows in the board
   */
  int getBoardH();

  /**
   * Returns the color of the card at the given row and column.
   *
   * @param row the row of the card
   * @param col the column of the card
   * @return the color of the card at the given row and column
   * @throws IllegalArgumentException if the given row and col is a hole
   * @throws IllegalArgumentException if there is not a card played at the given row and col
   */
  Color getCardColor(int row, int col);


  /**
   * Returns the score of the given player.
   *
   * @param color the color of the player
   * @return the score of the given player
   */
  int getScore(Color color);

  /**
   * Returns the number of cards a given card can flip at a certain coordiante.
   *
   * @return the number of cards that can be flipped
   * @throws IllegalStateException    if the game has not started or is already over
   * @throws IllegalArgumentException if the given row and col is a hole
   */
  int countPossibleFlips(int row, int col, C card);

  /**
   * Returns whether the given move is valid.
   *
   * @param row the row of the card
   * @param col the column of the card
   * @return whether the given move is valid (whether it's a tile or not)
   */
  boolean isValidMove(int row, int col);

  /**
   * Gets the status of the game, started or not started.
   *
   * @return true if the game has started, false otherwise.
   */
  boolean hasGameStarted();

  /**
   * Returns the current player after players have been set.
   *
   * @return the current player
   */
  Player getActivePlayer();
}
