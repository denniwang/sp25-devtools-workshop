package model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import player.Player;
import playerstrategy.StrategyMove;

/**
 * A mock model for testing multiple strategies.
 * Includes some hard coded valuse for testing.
 * Not all functions are expected to work
 */
public class ReadonlyMockThreeTrioModel implements ReadonlyThreeTrioModel<ThreeTrioCard> {
  Appendable log;
  private ThreeTrioCard[][] board;
  private Map<String, Integer> flipCounts = new HashMap<>();

  /**
   * Constructs a mock model with an appendable to log and boaard to play on.
   *
   * @param log   appendable to log
   * @param board board to play on
   */
  public ReadonlyMockThreeTrioModel(Appendable log, ThreeTrioCard[][] board) {
    this.log = log;
    this.board = board;
  }

  /**
   * Mock place card on the board.
   * Actually plays the card to the board. Not sure if this was optimal, but testing would've been
   * a headache otherwise.
   *
   * @param move move to place card
   */
  public void mockPlaceCard(StrategyMove move) {
    this.board[move.getRow()][move.getCol()] = move.getCard();
    try {
      log.append(this.board[move.getRow()][move.getCol()].getName() + " was placed at "
              + move.getRow() + " " + move.getCol()).append(System.lineSeparator());
    } catch (IOException ignore) {
      //ignore  this exception because we are a mock
    }
  }

  @Override
  public boolean isGameOver() {
    return false;
  }

  @Override
  public Color getWinner() {
    return null;
  }

  @Override
  public int getNumTiles() {
    return 0;
  }

  @Override
  public ArrayList<ThreeTrioCard> getPlayerHand() {
    try {
      log.append("Checking a new card").append(System.lineSeparator());
    } catch (IOException ignore) {
      //ignore  this exception because we are a mock
    }
    ArrayList<ThreeTrioCard> hand = new ArrayList<>();
    hand.add(new PlayingCard("Jerome", Attack.ONE, Attack.TWO, Attack.THREE, Attack.FOUR));
    hand.add(new PlayingCard("John", Attack.ONE, Attack.TWO, Attack.THREE, Attack.FOUR));
    hand.add(new PlayingCard("Hank", Attack.EIGHT, Attack.SEVEN, Attack.SIX, Attack.FIVE));
    hand.add(new PlayingCard("Frank", Attack.SEVEN, Attack.EIGHT, Attack.NINE, Attack.ONE));
    hand.add(new PlayingCard("Charlie", Attack.NINE, Attack.EIGHT, Attack.SEVEN, Attack.SIX));
    return hand;
  }

  @Override
  public ArrayList<ThreeTrioCard> getOtherPlayerHand() {
    try {
      log.append("Checking a new card").append(System.lineSeparator());
    } catch (IOException ignore) {
      //ignore  this exception because we are a mock
    }
    ArrayList<ThreeTrioCard> hand = new ArrayList<>();
    hand.add(new PlayingCard("Jerome", Attack.ONE, Attack.TWO, Attack.THREE, Attack.FOUR));
    hand.add(new PlayingCard("John", Attack.TWO, Attack.THREE, Attack.FOUR, Attack.FIVE));
    hand.add(new PlayingCard("Hank", Attack.THREE, Attack.TWO, Attack.ONE, Attack.FOUR));
    hand.add(new PlayingCard("Frank", Attack.SEVEN, Attack.EIGHT, Attack.NINE, Attack.ONE));
    hand.add(new PlayingCard("Charlie", Attack.EIGHT, Attack.SEVEN, Attack.SIX, Attack.FIVE));
    return hand;
  }

  @Override
  public List<ThreeTrioCard> getPlayerOneHand() {
    try {
      log.append("getting player one  hand").append(System.lineSeparator());
    } catch (IOException ignore) {
      //ignore  this exception because we are a mock
    }
    return List.of();
  }

  @Override
  public List<ThreeTrioCard> getPlayerTwoHand() {
    try {
      log.append("getting player two hand").append(System.lineSeparator());
    } catch (IOException ignore) {
      //ignore  this exception because we are a mock
    }
    return List.of();
  }

  @Override
  public ThreeTrioCard[][] getBoard() {
    return this.board;
  }

  public void setBoard(ThreeTrioCard[][] board) {
    this.board = board;
  }

  @Override
  public boolean getTurn() {
    return false;
  }

  @Override
  public ThreeTrioCard getCard(int row, int col) {
    try {
      log.append("checking spot at row: " + row + " col: " + col).append(System.lineSeparator());
    } catch (IOException ignore) {
      //ignore  this exception because we are a mock
    }
    return null;
  }

  @Override
  public int getBoardW() {
    return this.board[0].length;
  }

  @Override
  public int getBoardH() {
    return this.board.length;
  }

  @Override
  public Color getCardColor(int row, int col) {
    return null;
  }

  @Override
  public int getScore(Color color) {
    return 0;
  }


  /**
   * Set the flip count for a card at a position on the board so strategy can see.
   *
   * @param row   target row
   * @param col   target col
   * @param card  target card
   * @param count count to set
   */
  public void setFlipCount(int row, int col, ThreeTrioCard card, int count) {
    String key = row + "," + col + "," + card.getName();
    flipCounts.put(key, count);
  }

  @Override
  public int countPossibleFlips(int row, int col, ThreeTrioCard card) {
    try {
      log.append("Was a valid move. Checking max flips at " + row + " "
              + col).append(System.lineSeparator());
    } catch (IOException ignore) {
      //ignore  this exception because we are a mock
    }
    String key = row + "," + col + "," + card.getName();
    // Return 0 if no flips are set for this card and position
    return flipCounts.getOrDefault(key, 0);
  }

  @Override
  public boolean isValidMove(int row, int col) {
    boolean isValidMove = (!this.board[row][col].isHole()
            && this.board[row][col].getName() == null);
    try {
      if (isValidMove) {
        log.append("Spot " + row + " " + col + " is a valid move").append(System.lineSeparator());
      } else {
        log.append("Spot " + row + " " + col + " is NOT a valid move")
                .append(System.lineSeparator());
      }
    } catch (IOException ignore) {
      // ignore this exception because we are a mock
    }
    return ((row == 0 && col == 1) || (row == 0 & col == 0) || (row == 2 && col == 1)
            || (row == 4 && col == 0) || (row == 2 && col == 0)) && isValidMove;
  }

  @Override
  public boolean hasGameStarted() {
    return false;
  }

  @Override
  public Player getActivePlayer() {
    return null;
  }

  void playToBoard(int row, int col, int handIdx) {
    try {
      log.append("Playing to the board");
    } catch (IOException ignore) {
      //ignore  this exception because we are a mock
    }
  }

}
