package playerstrategy;

import playerstrategy.Move;
import model.ReadonlyThreeTrioModel;
import model.ThreeTrioCard;

/**
 * This class is used to just choose the first available move found on the board
 * which will be the uppermost-leftmost move with hand index 0.
 */
public class NoBestMove {
  private ReadonlyThreeTrioModel<ThreeTrioCard> game;

  /**
   * Constructs a NoBestMove object with the given game model.
   *
   * @param game the game model to be used for the strategy.
   */
  public NoBestMove(ReadonlyThreeTrioModel game) {
    this.game = game;
  }

  /**
   * Finds and returns the first available move found on the board.
   *
   * @return the first available move found on the board.
   */
  public Move getNoBestMove() {
    for (int row = 0; row < game.getBoardH(); row++) {
      for (int col = 0; col < game.getBoardW(); col++) {
        if (game.isValidMove(row, col)) {
          return new StrategyMove(row, col, game.getPlayerHand().get(0));
        }
      }
    }
    throw new IllegalArgumentException("No valid moves, the board is full");
  }
}