package player;

import playerstrategy.Move;
import playerstrategy.PlayerStrategy;

/**
 * Represents a computer player for the ThreeTrio game that plays a strategy.
 */
public class Computer implements Player {
  private String username;
  private PlayerStrategy strategy;


  /**
   * Constructs a computer player with a username and strategy.
   *
   * @param username the username of the computer player
   * @param strategy the strategy to be executed on every move
   */
  public Computer(String username, PlayerStrategy strategy) {
    this.username = username;
    this.strategy = strategy;
  }

  /**
   * Gets the name of the player.
   *
   * @return the name of the player as a string
   */
  @Override
  public String getName() {
    return username;
  }

  /**
   * Gets the move from the strategy.
   *
   * @return the move from the strategy.
   */
  public Move getMove() {
    return strategy.getMove();
  }
}
