package player;

import playerstrategy.Move;

/**
 * Represents a human player for the ThreeTrio game.
 */
public class Human implements Player {
  private String username;

  /**
   * Constructs a human player with a username.
   *
   * @param username the username of the human player
   */
  public Human(String username) {
    this.username = username;
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
   * Gets the move from the player.
   *
   * @return null since human players will always be choosing their own moves manually.
   */
  @Override
  public Move getMove() {
    return null; // Human player chooses their own move
  }
}
