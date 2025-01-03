package model;

import java.util.List;

/**
 * Interface for the status of the model.
 */
public interface ModelStatus {
  /**
   * Returns the current turn of the model.
   *
   * @return true if p1 turn, false if p2 turn
   */
  public boolean getTurn();

  /**
   * Return if the game is over.
   *
   * @return true if the game is over, false otherwise
   */
  public boolean isGameOver();

  /**
   * Return the board of the game.
   *
   * @return 2d array of cards representing the board.
   */
  public ThreeTrioCard[][] getBoard();

  /**
   * Return the hand of player 1.
   *
   * @return list of cards representing the hand of player 1.
   */
  public List<ThreeTrioCard> getP1Hand();

  /**
   * Return the hand of player 2.
   *
   * @return list of cards representing the hand of player 2.
   */
  public List<ThreeTrioCard> getP2Hand();


}
