package model;

import java.util.ArrayList;
import java.util.List;


/**
 * Class for the PlusModel. A decorator class that adds the plus rule to the game.
 */
public class PlusModel extends ThreeTrioGame implements ThreeTrioModel<ThreeTrioCard> {
  private ThreeTrioModel<ThreeTrioCard> model;

  /**
   * Constructor for the PlusModel class.
   *
   * @param model the model to be decorated
   */
  public PlusModel(ThreeTrioModel<ThreeTrioCard> model) {
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

  @Override
  public void checkAndFlip(int adjRow, int adjCol,
                           Direction dirFrom, Direction dirTo, int row, int col,
                           ThreeTrioCard lastCard, List<ThreeTrioCard> flippedCards) {
    //System.out.println("fallen check and flip");

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
    //System.out.println("Battle at row:" + row + " col:" + col);
    ThreeTrioCard lastCard = board[row][col];
    ThreeTrioCard[] adjacentCards = new ThreeTrioCard[4];
    adjacentCards[0] = getCard(board, row - 1, col); // North
    adjacentCards[1] = getCard(board, row + 1, col); // South
    adjacentCards[2] = getCard(board, row, col - 1); // West
    adjacentCards[3] = getCard(board, row, col + 1); // East
    Direction[] dirs = {Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST};
    Direction[] dirsFrom = {Direction.SOUTH, Direction.NORTH, Direction.EAST, Direction.WEST};
    //System.out.println(Arrays.toString(adjacentCards));

    int[] sums = new int[4];
    int idx = 0;
    for (ThreeTrioCard card : adjacentCards) {
      if (card == null || card.getName() == null) {
        sums[idx] = -1; // Use -1 to mark invalid sums
      } else {
        sums[idx] = card.getAttacks().get(dirsFrom[idx]).getValue()
                + lastCard.getAttacks().get(dirs[idx]).getValue();
      }
      idx++;
    }

    int maxSameSum = 0;
    int bestSum = 0;
    for (int sum : sums) {
      if (sum == -1) {
        continue;
      }
      int sameSum = 0;
      for (int i = 0; i < adjacentCards.length; i++) {
        if (adjacentCards[i] != null &&
                adjacentCards[i].getColor() == lastCard.getColor() && sums[i] == sum) {
          sameSum++;
        }
      }
      if (sameSum >= 2) {
        bestSum = sum;
        maxSameSum = 2;
        break;
      }
      //maxSameSum = Math.max(maxSameSum, sameSum);
    }

    List<ThreeTrioCard> flippedCards = new ArrayList<>();
    if (maxSameSum >= 2) {
      //System.out.println("hello!");
      flipAdjacentCards(row, col, lastCard, flippedCards, bestSum);
    } else {
      flipAdjacentCards(row, col, lastCard, flippedCards);
    }
  }

  /**
   * Flips the adjacent cards of the given card.
   *
   * @param row          the row of the card
   * @param col          the column of the card
   * @param lastCard     the card to be flipped
   * @param flippedCards the list of flipped cards
   * @param sum          the sum of the attacks of the card and the adjacent card
   */
  protected void flipAdjacentCards(int row, int col, ThreeTrioCard lastCard,
                                   List<ThreeTrioCard> flippedCards, int sum) {
    // Check NORTH
    checkAndFlipBoost(row - 1, col, Direction.NORTH, Direction.SOUTH,
            row, col, lastCard, flippedCards, sum);
    // Check SOUTH
    checkAndFlipBoost(row + 1, col, Direction.SOUTH, Direction.NORTH,
            row, col, lastCard, flippedCards, sum);
    // Check WEST
    checkAndFlipBoost(row, col - 1, Direction.WEST, Direction.EAST,
            row, col, lastCard, flippedCards, sum);
    // Check EAST
    checkAndFlipBoost(row, col + 1, Direction.EAST, Direction.WEST,
            row, col, lastCard, flippedCards, sum);
  }

  private void checkAndFlipBoost(int adjRow, int adjCol, Direction dirFrom, Direction dirTo,
                                 int row, int col, ThreeTrioCard lastCard,
                                 List<ThreeTrioCard> flippedCards, int sum) {
    // Ensure the adjacent row and col are within bounds
    if (adjRow >= 0 && adjRow < board.length && adjCol >= 0 && adjCol < board[0].length) {
      ThreeTrioCard adjacentCard = board[adjRow][adjCol];

      // Check conditions for flipping the card
      if (adjacentCard.getColor() != lastCard.getColor()
              && !adjacentCard.isHole()
              && adjacentCard.getName() != null
              && compareAttacksBoost(adjacentCard, lastCard, dirFrom, dirTo, sum)) {
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
   * Gives a boost to the attack sum if enemy sum is equal.
   *
   * @param adjacentCard the adjacent card
   * @param lastCard     the last card
   * @param dirFrom      the direction from which the last card is attacking
   * @param dirTo        the direction to which the adjacent card is attacking
   * @param sum          the sum of the attacks of the card and the adjacent card
   * @return true if the attacks are valid, false otherwise
   */
  protected boolean compareAttacksBoost(ThreeTrioCard adjacentCard, ThreeTrioCard lastCard,
                                        Direction dirFrom, Direction dirTo, int sum) {
    if (lastCard.getAttacks().get(dirFrom).getValue()
            + adjacentCard.getAttacks().get(dirTo).getValue() == sum) {
      return true;
    } else {
      return model.compareAttacks(adjacentCard, lastCard, dirFrom, dirTo);
    }
  }
}
