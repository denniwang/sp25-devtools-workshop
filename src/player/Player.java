package player;

import playerstrategy.Move;

/**
 * Represents a player for the ThreeTrio game. Can be a human or computer player.
 */
public interface Player {
  /**
   * Returns the name of the player.
   *
   * @return string representing the name of the player;
   */
  String getName();

  /**
   * Gets the move from the strategy.
   * Only used for the machine player.
   *
   * @return the move from the strategy.
   */
  Move getMove();
}
