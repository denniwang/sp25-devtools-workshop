package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Class for the SameModel. A decorator class that adds the same rule to the game.
 */
public class SameModel extends ThreeTrioGame implements ThreeTrioModel<ThreeTrioCard> {
  private ThreeTrioModel<ThreeTrioCard> model;

  /**
   * Constructor for the SameModel class.
   *
   * @param model the model to be decorated
   */
  public SameModel(ThreeTrioModel<ThreeTrioCard> model) {
    super(model.getBoardConfig(), model.getDeckConfig());
    System.out.println("same model");
    this.board = model.getBoard();
    this.playerOneHand = model.getPlayerOneHand();
    this.playerTwoHand = model.getPlayerTwoHand();
    this.model = model;
    gameStarted = true;
  }

  @Override
  public boolean hasGameStarted() {
    return true;
  }

  private ThreeTrioCard getCard(ThreeTrioCard[][] board, int row, int col) {
    if (row >= 0 && row < board.length && col >= 0 && col < board[0].length) {
      return board[row][col];
    }
    return null;
  }

  @Override
  public void startBattle(int row, int col) {
    if (!this.gameStarted || this.gameOver) {
      throw new IllegalStateException("The game is not started or is already over");
    }
    System.out.println("Battle at row:" + row + " col:" + col);
    ThreeTrioCard lastCard = board[row][col];
    int sameCount = 0;
    ThreeTrioCard[] adjacentCards = new ThreeTrioCard[4];
    adjacentCards[0] = getCard(board, row - 1, col); // North
    adjacentCards[1] = getCard(board, row + 1, col); // South
    adjacentCards[2] = getCard(board, row, col - 1); // West
    adjacentCards[3] = getCard(board, row, col + 1); // East
    Direction[] dirs = {Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST};
    Direction[] dirsFrom = {Direction.SOUTH, Direction.NORTH, Direction.EAST, Direction.WEST};
    System.out.println(Arrays.toString(adjacentCards));

    //count number of similar cards from same color
    int idx = 0;
    for (ThreeTrioCard card : adjacentCards) {
      if (card == null) {
        continue;
      }
      if (card.getColor() == lastCard.getColor()) {
        if (card.getAttacks().get(dirsFrom[idx])
                == getCard(board, row, col).getAttacks().get(dirs[idx])) {
          sameCount++;
        }
      }
      idx++;
    }
    List<ThreeTrioCard> flippedCards = new ArrayList<>();
    if (sameCount >= 1) {
      flipAdjacentCards(row, col, lastCard, flippedCards, true);
    } else {
      flipAdjacentCards(row, col, lastCard, flippedCards);
    }
  }

  @Override
  public void checkAndFlip(int adjRow, int adjCol,
                           Direction dirFrom, Direction dirTo, int row, int col,
                           ThreeTrioCard lastCard, List<ThreeTrioCard> flippedCards) {
    System.out.println("same check and flip");

    if (adjRow >= 0 && adjRow < board.length && adjCol >= 0 && adjCol < board[0].length) {
      ThreeTrioCard adjacentCard = board[adjRow][adjCol];

      if (adjacentCard.getColor() != lastCard.getColor()
              && !adjacentCard.isHole()
              && adjacentCard.getName() != null
              && model.compareAttacks(adjacentCard, lastCard, dirFrom, dirTo)) {
        flippedCards.add(adjacentCard);
        adjacentCard.setColor(lastCard.getColor());
        recursivelyFlip(board, adjRow, adjCol, adjacentCard, flippedCards, dirTo);
      }
    }
  }

  /**
   * Flips adjacent cards to the last card played.
   *
   * @param row          the row of the last card played
   * @param col          the column of the last card played
   * @param lastCard     the last card played
   * @param flippedCards the list of flipped cards
   * @param boost        if the flip is boosted
   */
  protected void flipAdjacentCards(int row, int col, ThreeTrioCard lastCard,
                                   List<ThreeTrioCard> flippedCards, boolean boost) {
    // Check NORTH
    checkAndFlipBoost(row - 1, col, Direction.NORTH, Direction.SOUTH,
            row, col, lastCard, flippedCards);
    // Check SOUTH
    checkAndFlipBoost(row + 1, col, Direction.SOUTH, Direction.NORTH,
            row, col, lastCard, flippedCards);
    // Check WEST
    checkAndFlipBoost(row, col - 1, Direction.WEST, Direction.EAST,
            row, col, lastCard, flippedCards);
    // Check EAST
    checkAndFlipBoost(row, col + 1, Direction.EAST, Direction.WEST,
            row, col, lastCard, flippedCards);
  }

  /**
   * Checks and flips the adjacent card.
   *
   * @param adjRow       the row of the adjacent card
   * @param adjCol       the column of the adjacent card
   * @param dirFrom      the direction from which the last card is attacking
   * @param dirTo        the direction to which the adjacent card is attacking
   * @param row          the row of the last card
   * @param col          the column of the last card
   * @param lastCard     the last card
   * @param flippedCards the list of flipped cards
   */
  private void checkAndFlipBoost(int adjRow, int adjCol, Direction dirFrom, Direction dirTo,
                                 int row, int col, ThreeTrioCard lastCard,
                                 List<ThreeTrioCard> flippedCards) {
    // Ensure the adjacent row and col are within bounds
    if (adjRow >= 0 && adjRow < board.length && adjCol >= 0 && adjCol < board[0].length) {
      ThreeTrioCard adjacentCard = board[adjRow][adjCol];

      // Check conditions for flipping the card
      if (adjacentCard.getColor() != lastCard.getColor()
              && !adjacentCard.isHole()
              && adjacentCard.getName() != null
              && compareAttacksBoost(adjacentCard, lastCard, dirFrom, dirTo)) {
        flippedCards.add(adjacentCard);
        adjacentCard.setColor(lastCard.getColor());
        // recursively flip from this card we just flipped
        // note we pass the dirTo because to the new card, that is the direction we are coming from
        //recursivelyFlip(adjRow, adjCol, adjacentCard, flippedCards, dirTo);
      }
    }
  }

  /**
   * Compares the attacks of the adjacent card and the last card.
   * Gives a boost if enemy card is same attack value.
   *
   * @param adjacentCard the adjacent card
   * @param lastCard    the last card
   * @param dirFrom   the direction from which the last card is attacking
   * @param dirTo    the direction to which the adjacent card is attacking
   * @return true if the card attack beats the attack of the other card, false otherwise
   */
  protected boolean compareAttacksBoost(ThreeTrioCard adjacentCard, ThreeTrioCard lastCard,
                                        Direction dirFrom, Direction dirTo) {
    System.out.println("same compare attak");
    if (lastCard.getAttacks().get(dirFrom).getValue()
            == adjacentCard.getAttacks().get(dirTo).getValue()) {
      return true;
    } else {
      System.out.println("using base model");
      return model.compareAttacks(adjacentCard, lastCard, dirFrom, dirTo);
    }
  }


}
