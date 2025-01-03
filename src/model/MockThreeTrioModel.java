package model;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import player.Human;
import player.Player;

/**
 * A mock model for testing multiple strategies.
 * Includes some hard coded valuse for testing.
 * Not all functions are expected to work
 */
public class MockThreeTrioModel implements ThreeTrioModel<ThreeTrioCard> {
  private final Map<String, Integer> flipCounts = new HashMap<>();
  Appendable log;

  /**
   * Constructs a mock model with the given log and board.
   *
   * @param log   the log to append messages to
   * @param board the board to reference in the game
   */
  public MockThreeTrioModel(Appendable log, ThreeTrioCard[][] board) {
    this.log = log;
  }


  @Override
  public boolean isGameOver() {
    try {
      log.append("Checking if game is over.");
    } catch (IOException ignore) {
      //ignore  this exception because we are a mock
    }
    return false;
  }

  @Override
  public Color getWinner() {
    try {
      log.append(".Getting winner.");
    } catch (IOException ignore) {
      //ignore  this exception because we are a mock
    }
    return Color.RED;
  }

  @Override
  public int getNumTiles() {
    return 0;
  }

  @Override
  public List<ThreeTrioCard> getPlayerHand() {
    return null;
  }

  @Override
  public List<ThreeTrioCard> getOtherPlayerHand() {
    return null;
  }

  @Override
  public List<ThreeTrioCard> getPlayerOneHand() {
    return null;
  }

  @Override
  public List<ThreeTrioCard> getPlayerTwoHand() {
    return null;
  }

  @Override
  public ThreeTrioCard[][] getBoard() {
    return new ThreeTrioCard[0][];
  }

  @Override
  public void setBoard(ThreeTrioCard[][] board) {
    //mock does nothing
  }

  @Override
  public boolean getTurn() {
    try {
      log.append("Getting turn.");
    } catch (IOException ignore) {
      //ignore  this exception because we are a mock
    }
    return true;
  }

  @Override
  public ThreeTrioCard getCard(int row, int col) {
    return null;
  }

  @Override
  public int getBoardW() {
    return 0;
  }

  @Override
  public int getBoardH() {
    return 0;
  }

  @Override
  public Color getCardColor(int row, int col) {
    return null;
  }

  @Override
  public int getScore(Color color) {
    try {
      log.append("Getting score.");
    } catch (IOException ignore) {
      //ignore  this exception because we are a mock
    }
    return 0;
  }

  @Override
  public int countPossibleFlips(int row, int col, ThreeTrioCard card) {
    return 0;
  }

  @Override
  public boolean isValidMove(int row, int col) {
    return false;
  }

  @Override
  public boolean hasGameStarted() {
    return false;
  }

  @Override
  public void startGame(List<ThreeTrioCard> deck, ThreeTrioCard[][] board) {
    //mock does nothing
  }

  @Override
  public void playToBoard(int row, int col, int handIdx) {
    if (handIdx < 0) {
      throw new IllegalArgumentException("Invalid handIdx");
    }
    try {
      log.append("Playing to board! Row: " + row + " Col: " + col + " HandIdx: " + handIdx);
    } catch (IOException ignore) {
      //ignore  this exception because we are a mock
    }
  }

  @Override
  public List<ThreeTrioCard> createDeck() throws FileNotFoundException {
    return null;
  }

  @Override
  public ThreeTrioCard[][] createBoard() throws FileNotFoundException {
    return new ThreeTrioCard[0][];
  }

  @Override
  public void startBattle(int row, int col) {
    //mock does nothing
  }

  @Override
  public void dealCards() {
    //mock does nothing
  }

  @Override
  public void setDeck(List<ThreeTrioCard> deck) {
    //mock does nothing
  }

  @Override
  public void setPlayers(Player p1, Player p2) {
    //mock does nothing
  }

  @Override
  public Player getActivePlayer() {
    try {
      log.append("Getting current player.");
    } catch (IOException ignore) {
      //ignore  this exception because we are a mock
    }
    return new Human("John");
  }

  @Override
  public void checkAndFlip(int adjRow, int adjCol, Direction dirFrom, Direction dirTo, int row,
                           int col, ThreeTrioCard lastCard, List<ThreeTrioCard> flippedCards) {
    //mock does nothing
  }

  @Override
  public void checkAndFlip(ThreeTrioCard[][] board, int adjRow, int adjCol, Direction dirFrom,
                           Direction dirTo, int row, int col, ThreeTrioCard lastCard,
                           List<ThreeTrioCard> flippedCards) {
    //mock does nothing
  }

  @Override
  public boolean compareAttacks(ThreeTrioCard adjacentCard, ThreeTrioCard lastCard,
                                Direction dirFrom, Direction dirTo) {
    return false;
  }

  @Override
  public String getDeckConfig() {
    return null;
  }

  @Override
  public String getBoardConfig() {
    return null;
  }
}
