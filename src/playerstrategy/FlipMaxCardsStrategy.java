package playerstrategy;

import java.util.ArrayList;
import java.util.List;

import model.ReadonlyThreeTrioModel;
import model.ThreeTrioCard;

/**
 * Represents a player strategy that prioritizes flipping the most cards.
 * The strategy calculates the number of cards that can be flipped for each possible move and plays
 * the card that flips the most cards.
 */
public class FlipMaxCardsStrategy implements PlayerStrategy {
  private ReadonlyThreeTrioModel<ThreeTrioCard> game;

  /**
   * Constructor for the FlipMaxCardsStrategy.
   * Initializes the game model.
   *
   * @param game the game model
   */
  public FlipMaxCardsStrategy(ReadonlyThreeTrioModel game) {
    this.game = game;
  }

  @Override
  public Move getMove() {
    int maxFlips = 0;
    int bestCardInHandIdx = -1;
    int bestRow = -1;
    int bestCol = -1;
    List<Move> ties = new ArrayList<>();

    for (int idx = 0; idx < game.getPlayerHand().size(); idx++) {
      System.out.println(game.getPlayerHand().get(idx).toString());
      ThreeTrioCard card = game.getPlayerHand().get(idx);
      for (int row = 0; row < game.getBoardH(); row++) {
        for (int col = 0; col < game.getBoardW(); col++) {
          if (game.isValidMove(row, col)) {
            if (game.countPossibleFlips(row, col, card) >= maxFlips) {
              if (game.countPossibleFlips(row, col, card) > maxFlips) {
                ties.clear();
              }
              maxFlips = game.countPossibleFlips(row, col, card);
              bestCardInHandIdx = idx;
              bestRow = row;
              bestCol = col;
              ties.add(new StrategyMove(row, col, card));
            }
          }
        }
      }
    }

    // If there are multiple best moves
    if (ties.size() > 1) {
      return new TieBreaker().breakTie(ties);
    }
    // If there is no best move
    if (bestCardInHandIdx == -1) {
      return new NoBestMove(game).getNoBestMove();
    }
    // There is a best move for this strategy
    return new StrategyMove(bestRow, bestCol, game.getPlayerHand().get(bestCardInHandIdx));
  }

  @Override
  public int getScore(Move move) {
    int row = move.getRow();
    int col = move.getCol();
    ThreeTrioCard card = move.getCard();
    return game.countPossibleFlips(row, col, card);
  }
}