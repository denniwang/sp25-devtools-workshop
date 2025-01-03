package playerstrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import model.ReadonlyThreeTrioModel;
import model.ThreeTrioCard;
import model.ThreeTrioGame;

/**
 * Represents a player strategy that tries to give the opposing player the worst move possible.
 * Weighing different types of strategies differently based on opponent moves.
 */
public class MinimaxStrategy implements PlayerStrategy {
  private Map<StrategyType, Integer> weights;
  private ReadonlyThreeTrioModel<ThreeTrioCard> game;

  /**
   * Constructs a MinimaxStrategy with the given model and weights.
   *
   * @param model   model to create strategy for
   * @param weights weights for different strategies
   */
  public MinimaxStrategy(ReadonlyThreeTrioModel<ThreeTrioCard> model,
                         Map<StrategyType, Integer> weights) {
    this.weights = weights;
    this.game = model;
  }

  @Override
  public Move getMove() {
    List<Move> ties = new ArrayList<>();
    Move bestMove = null;
    int bestScore = Integer.MAX_VALUE;

    // Iterate over all possible moves
    for (int row = 0; row < game.getBoardH(); row++) {
      for (int col = 0; col < game.getBoardW(); col++) {
        for (ThreeTrioCard card : game.getPlayerHand()) {
          if (game.isValidMove(row, col)) {
            Move move = new StrategyMove(row, col, card);
            int score = getScore(move);
            //System.out.println("Move: " + move + " Score: " + score); // Debugging statement
            if (score < bestScore) {
              bestScore = score;
              bestMove = move;
              ties.clear();
              ties.add(move);
            } else if (score == bestScore) {
              ties.add(move);
            }
          } else {
            //System.out.println("Invalid move at row: " + row + " col: " + col);
            // Debugging statement
          }
        }
      }
    }

    if (ties.size() > 1) {
      TieBreaker breaker = new TieBreaker();
      bestMove = breaker.breakTie(ties);
    }

    //System.out.println("Best Move: " + bestMove); // Debugging statement
    return bestMove;
  }

  @Override
  public int getScore(Move move) {
    int maxOppScore = Integer.MIN_VALUE;
    //tempBoard[move.getRow()][move.getCol()] = move.getCard();
    ThreeTrioGame temp = new ThreeTrioGame(game.getPlayerHand(),
            game.getOtherPlayerHand(), game.getBoard(), game.getTurn());


    temp.playToBoard(move.getRow(), move.getCol(), temp.getPlayerHand().indexOf(move.getCard()));
    // Evaluate the best move the opponent can make using each strategy
    for (StrategyType strategyType : weights.keySet()) {
      PlayerStrategy strategy = StrategyFactory.createStrategy(strategyType, temp);
      int stratScore = 0;
      for (int row = 0; row < temp.getBoardH(); row++) {
        for (int col = 0; col < temp.getBoardW(); col++) {
          for (ThreeTrioCard card : temp.getOtherPlayerHand()) {
            if (temp.isValidMove(row, col)) {
              //System.out.println("Opponent Move: ");
              Move opponentMove = new StrategyMove(row, col, card);
              //System.out.println("Opponent Move: " + opponentMove); // Debugging statement
              int opponentScore = strategy.getScore(opponentMove);
              stratScore = Math.max(stratScore, opponentScore);
            }
          }
        }
      }
      maxOppScore = Math.max(maxOppScore, stratScore * weights.get(strategyType));
    }

    // Return the inverse of the maximum opponent score to minimize it
    // hard coded in weighing for score, will need to fix in future

    System.out.println("Move: " + move + " Score: " + (1000 - maxOppScore)); // Debugging statement

    return 1000 - maxOppScore;
  }

  private int calculateScoreIfMove(Move m, PlayerStrategy strategy) {
    return strategy.getScore(m);
  }
}