package playerstrategy;

import java.util.ArrayList;
import java.util.List;

import model.Direction;
import model.ReadonlyThreeTrioModel;
import model.ThreeTrioCard;

/**
 * Represents a player strategy that prioritizes playing cards in the corners of the board.
 * The strategy calculates a defensive score for each corner move and plays to the card with the
 * highest score. The defensive score is calculated by summing the attack values of the open sides
 * of the card. If an open side is next to a hole, or a card that has already been played, it is not
 * possible to flip that card from that position and therefore will be represented as a 10 be
 * default.
 */
public class CornerStrategy implements PlayerStrategy {
  private ReadonlyThreeTrioModel game;
  private int bestRow;
  private int bestCol;
  private ThreeTrioCard bestCard;
  private List<Move> ties;
  private int bestScore;

  /**
   * Constructor for the CornerStrategy.
   * Initializes the game model and sets the best row, column, and card to -1.
   *
   * @param game the game model
   */
  public CornerStrategy(ReadonlyThreeTrioModel game) {
    this.game = game;
    this.bestRow = -1;
    this.bestCol = -1;
    this.bestCard = null;
    ties = new ArrayList<>();
    this.bestScore = 0;
  }

  @Override
  public Move getMove() {
    for (int i = 0; i < game.getPlayerHand().size(); i++) {
      ThreeTrioCard card = (ThreeTrioCard) game.getPlayerHand().get(i);
      // Top Left Corner
      if (game.isValidMove(0, 0)) {
        calculateDefensiveScore(0, 0, card, Direction.SOUTH, Direction.EAST);
      }
      // Top Right Corner
      if (game.isValidMove(0, game.getBoardW() - 1)) {
        calculateDefensiveScore(0, game.getBoardW() - 1, card,
                Direction.SOUTH, Direction.WEST);
      }
      // Bottom Left Corner
      if (game.isValidMove(game.getBoardH() - 1, 0)) {
        calculateDefensiveScore(game.getBoardH() - 1, 0, card,
                Direction.NORTH, Direction.EAST);
      }
      // Bottom Right Corner
      if (game.isValidMove(game.getBoardH() - 1, game.getBoardW() - 1)) {
        calculateDefensiveScore(game.getBoardH() - 1, game.getBoardW() - 1, card,
                Direction.NORTH, Direction.WEST);
      }
    }
    // If there are multiple best moves
    if (this.ties.size() > 1) {
      return new TieBreaker().breakTie(this.ties);
    }
    // If there is no best move with this strategy
    if (this.bestCard == null) {
      return new NoBestMove(game).getNoBestMove();
    }
    // If there is a best move for this strategy
    return new StrategyMove(this.bestRow, this.bestCol, this.bestCard);
  }

  @Override
  public int getScore(Move move) {
    int score = 0;
    int row = move.getRow();
    int col = move.getCol();
    ThreeTrioCard card = move.getCard();

    // Check if the move is in a corner
    if ((row == 0 && col == 0) || (row == 0 && col == game.getBoardW() - 1) ||
            (row == game.getBoardH() - 1 && col == 0) ||
            (row == game.getBoardH() - 1 && col == game.getBoardW() - 1)) {
      score += 10; // Assign a high score for corner moves
    }

    // Calculate the difficulty of flipping the card
    score += calculateFlipDifficulty(row, col, card);

    return score;
  }

  private int calculateFlipDifficulty(int row, int col, ThreeTrioCard card) {
    int difficulty = 0;
    int[] dx = {0, 1, 0, -1};
    int[] dy = {1, 0, -1, 0};

    for (int i = 0; i < 4; i++) {
      int r = row + dx[i];
      int c = col + dy[i];
      if (r >= 0 && r < game.getBoardH() && c >= 0 && c < game.getBoardW()) {
        ThreeTrioCard adjacentCard = game.getBoard()[r][c];
        if (adjacentCard != null && !adjacentCard.isHole() && adjacentCard.getAttacks() != null) {
          if (i == 0 && adjacentCard.getAttacks().get(Direction.SOUTH).getValue() <
                  card.getAttacks().get(Direction.NORTH).getValue()) {
            difficulty++;
          } else if (i == 1 && adjacentCard.getAttacks().get(Direction.WEST).getValue() <
                  card.getAttacks().get(Direction.EAST).getValue()) {
            difficulty++;
          } else if (i == 2 && adjacentCard.getAttacks().get(Direction.NORTH).getValue() <
                  card.getAttacks().get(Direction.SOUTH).getValue()) {
            difficulty++;
          } else if (i == 3 && adjacentCard.getAttacks().get(Direction.EAST).getValue() <
                  card.getAttacks().get(Direction.WEST).getValue()) {
            difficulty++;
          }
        }
      }
    }

    return difficulty;
  }


  private void calculateDefensiveScore(int row, int col,
                                       ThreeTrioCard card, Direction dir1, Direction dir2) {
    int rows = game.getBoard().length;
    int cols = game.getBoard()[0].length;
    int score = 0;

    // Check top left corner
    if (row == 0 && col == 0) {
      if (row + 1 < rows && col + 1 < cols) {
        // Check tile to the right and below
        score += calculateScore(row, col, row + 1, col, card, dir1);
        score += calculateScore(row, col, row, col + 1, card, dir2);
      }
    }
    // Check top right corner
    else if (row == 0 && col == cols - 1) {
      if (row + 1 < rows && col - 1 >= 0) {
        // Check tile to the left and below
        score += calculateScore(row, col, row + 1, col, card, dir1);
        score += calculateScore(row, col, row, col - 1, card, dir2);
      }
    }
    // Check bottom left corner
    else if (row == rows - 1 && col == 0) {
      if (row - 1 >= 0 && col + 1 < cols) {
        // Check tile to the right and above
        score += calculateScore(row, col, row - 1, col, card, dir1);
        score += calculateScore(row, col, row, col + 1, card, dir2);
      }
    }
    // Check bottom right corner
    else if (row == rows - 1 && col == cols - 1) {
      if (row - 1 >= 0 && col - 1 >= 0) {
        // Check tile to the left and above
        score += calculateScore(row, col, row - 1, col, card, dir1);
        score += calculateScore(row, col, row, col - 1, card, dir2);
      }
    }
    updateBestScore(row, col, card, score);
  }

  private void updateBestScore(int row, int col, ThreeTrioCard card, int score) {
    if (score > this.bestScore) {
      this.bestScore = score;
      this.bestRow = row;
      this.bestCol = col;
      this.bestCard = card;
      ties.clear();
      ties.add(new StrategyMove(row, col, card));
    } else if (score == bestScore) {
      ties.add(new StrategyMove(row, col, card));
    }
  }

  private int calculateScore(int row, int col, int adjacentRow,
                             int adjacentCol, ThreeTrioCard card, Direction dir) {
    if (game.isValidMove(row, col)) {
      if (game.getBoard()[adjacentRow][adjacentCol].isHole()
              || game.getBoard()[adjacentRow][adjacentCol].getName() != null) {
        return 10; // return the value of A since it's virtually unflippable
      } else {
        return card.getAttacks().get(dir).getValue();
      }
    } else {
      // if it's not a valid move then don't even consider it so give it a score of 0
      return 0;
    }
  }

}