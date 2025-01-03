package playerstrategy;

import playerstrategy.Move;

/**
 * Represents a strategy that a player can use to make a move.
 */
public interface PlayerStrategy {
  /**
   * Returns the move that the player wants to make.
   *
   * @return the move that the player wants to make.
   */
  Move getMove();

  /**
   * Returns the score of the move. Higher number = better move
   *
   * @param move the move to get the score of
   * @return the score of the move onto the model
   */
  int getScore(Move move);

  /**
   * Adds a strategy to be played after this one, to form more complex strategies.
   *
   * @param nextStrategy the strategy to be played next
   */
  default PlayerStrategy then(PlayerStrategy nextStrategy) {
    return new ComplexPlayerStrategy(this, nextStrategy);
  }

  ;


}
