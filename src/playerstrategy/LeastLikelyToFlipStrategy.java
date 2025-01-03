package playerstrategy;

import java.util.ArrayList;
import java.util.List;

import model.Direction;
import model.ReadonlyThreeTrioModel;
import model.ThreeTrioCard;

/**
 * Strategy to find the card and location to play that has the lowest chance of being flipped.
 */
public class LeastLikelyToFlipStrategy implements PlayerStrategy {
  private ReadonlyThreeTrioModel<ThreeTrioCard> game;
  private int bestRow;
  private int bestCol;
  private ThreeTrioCard bestCard;

  /**
   * Constructor for the LeastLikelyToFlipStrategy.
   * Initializes the game model and sets the best row, column, and card to -1.
   *
   * @param game the game model
   */
  public LeastLikelyToFlipStrategy(ReadonlyThreeTrioModel<ThreeTrioCard> game) {
    this.game = game;
    this.bestRow = -1;
    this.bestCol = -1;
    this.bestCard = null;
  }

  @Override
  public Move getMove() {
    List<Move> ties;
    // placeholder
    //check in each direction how many opponent cards can flip them
    int[] dx = new int[]{0, 1, 0, -1};
    int[] dy = new int[]{1, 0, -1, 0};

    ties = new ArrayList<>();
    //number of possible cards that can flip the best card
    int min = Integer.MAX_VALUE;
    for (int row = 0; row < game.getBoardH(); row++) {
      for (int col = 0; col < game.getBoardW(); col++) {
        ThreeTrioCard minCard = null;
        int minForSpot = Integer.MAX_VALUE;
        if (!game.isValidMove(row, col)) {
          continue;
        }

        for (ThreeTrioCard currentCard : game.getPlayerHand()) {
          int flips = 0;
          for (int i = 0; i < 4; i++) {
            int r = row + dx[i];
            int c = col + dy[i];
            if (r < 0 || r >= game.getBoardH() || c < 0 || c >= game.getBoardW()) {
              continue;
            }
            //making sure its a tile enemy can play on
            if (i == 0) {
              //check north
              for (ThreeTrioCard otherCard : game.getOtherPlayerHand()) {
                if (otherCard.getAttacks().get(Direction.SOUTH).getValue() >
                        currentCard.getAttacks().get(Direction.NORTH).getValue()) {
                  flips++;
                }
              }
            } else if (i == 1) {
              //check east
              for (ThreeTrioCard otherCard : game.getOtherPlayerHand()) {
                if (otherCard.getAttacks().get(Direction.WEST).getValue() >
                        currentCard.getAttacks().get(Direction.EAST).getValue()) {
                  flips++;
                }
              }
            } else if (i == 2) {
              //check south
              for (ThreeTrioCard otherCard : game.getOtherPlayerHand()) {
                if (otherCard.getAttacks().get(Direction.NORTH).getValue() >
                        currentCard.getAttacks().get(Direction.SOUTH).getValue()) {
                  flips++;
                }
              }
            } else {
              //check west
              for (ThreeTrioCard otherCard : game.getOtherPlayerHand()) {
                if (otherCard.getAttacks().get(Direction.EAST).getValue() >
                        currentCard.getAttacks().get(Direction.WEST).getValue()) {
                  flips++;
                }
              }
            }
          }
          if (flips < minForSpot) {
            minForSpot = flips;
            minCard = currentCard;
          }
        }
        if (minForSpot < min) {
          min = minForSpot;
          bestRow = row;
          bestCol = col;
          bestCard = minCard;
          ties.clear();
          ties.add(new StrategyMove(row, col, minCard));
        } else if (minForSpot == min) {
          ties.add(new StrategyMove(row, col, minCard));
        }
      }
    }
    return new StrategyMove(bestRow, bestCol, bestCard);
  }

  @Override
  public int getScore(Move move) {
    int row = move.getRow();
    int col = move.getCol();
    ThreeTrioCard card = move.getCard();
    int flips = 0;
    int[] dx = {0, 1, 0, -1};
    int[] dy = {1, 0, -1, 0};

    for (int i = 0; i < 4; i++) {
      int r = row + dx[i];
      int c = col + dy[i];
      if (r >= 0 && r < game.getBoardH() && c >= 0 && c < game.getBoardW()) {
        for (ThreeTrioCard opponentCard : game.getOtherPlayerHand()) {
          if (i == 0 && opponentCard.getAttacks().get(Direction.SOUTH).getValue()
                  > card.getAttacks().get(Direction.NORTH).getValue()) {
            flips++;
          } else if (i == 1 && opponentCard.getAttacks().get(Direction.WEST).getValue()
                  > card.getAttacks().get(Direction.EAST).getValue()) {
            flips++;
          } else if (i == 2 && opponentCard.getAttacks().get(Direction.NORTH).getValue()
                  > card.getAttacks().get(Direction.SOUTH).getValue()) {
            flips++;
          } else if (i == 3 && opponentCard.getAttacks().get(Direction.EAST).getValue()
                  > card.getAttacks().get(Direction.WEST).getValue()) {
            flips++;
          }
        }
      }
    }
    return flips;
  }
}