package playerstrategy;

import java.util.List;

import playerstrategy.Move;

/**
 * This class is used to break ties between moves that have the same score.
 * This class will choose the move that is closest to the top left corner of the board with
 * the hand index closest to 0.
 */
public class TieBreaker {
  /**
   * Constructs a TieBreaker object. Used to break ties between moves that have the same score.
   *
   * @param ties the list of moves that have the same score
   * @return the move that is closest to the top left corner of the board with the hand index
   */
  public Move breakTie(List<Move> ties) {
    Move bestMove = ties.get(0);

    for (Move move : ties) {
      if (move.getRow() < bestMove.getRow() ||
              (move.getRow() == bestMove.getRow() && move.getCol() < bestMove.getCol())) {
        bestMove = move;
      }
    }
    return bestMove;
  }
}
