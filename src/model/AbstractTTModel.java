package model;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import player.Player;

/**
 * Abstract class for the ThreeTrioModel interface.
 */
public class AbstractTTModel extends ThreeTrioGame implements ThreeTrioModel<ThreeTrioCard> {
  private ThreeTrioModel<ThreeTrioCard> model;

  /**
   * Constructor for the AbstractTTModel class.
   *
   * @param model the model to be used
   */
  public AbstractTTModel(ThreeTrioModel<ThreeTrioCard> model) {
    this.model = model;
  }

  @Override
  public boolean isGameOver() {
    return model.isGameOver();
  }

  @Override
  public Color getWinner() {
    return model.getWinner();
  }

  @Override
  public int getNumTiles() {
    return model.getNumTiles();
  }

  @Override
  public List<ThreeTrioCard> getPlayerHand() {
    return model.getPlayerHand();
  }

  @Override
  public List<ThreeTrioCard> getOtherPlayerHand() {
    return model.getOtherPlayerHand();
  }

  @Override
  public List<ThreeTrioCard> getPlayerOneHand() {
    return model.getPlayerOneHand();
  }

  @Override
  public List<ThreeTrioCard> getPlayerTwoHand() {
    return model.getPlayerTwoHand();
  }

  @Override
  public ThreeTrioCard[][] getBoard() {
    return model.getBoard();
  }

  @Override
  public void setBoard(ThreeTrioCard[][] board) {
    model.setBoard(board);
  }

  @Override
  public boolean getTurn() {
    return model.getTurn();
  }

  @Override
  public ThreeTrioCard getCard(int row, int col) {
    return model.getCard(row, col);
  }

  @Override
  public int getBoardW() {
    return model.getBoardW();
  }

  @Override
  public int getBoardH() {
    return model.getBoardH();
  }

  @Override
  public Color getCardColor(int row, int col) {
    return model.getCardColor(row, col);
  }

  @Override
  public int getScore(Color color) {
    return model.getScore(color);
  }

  @Override
  public int countPossibleFlips(int row, int col, ThreeTrioCard card) {
    return model.countPossibleFlips(row, col, card);
  }

  @Override
  public boolean isValidMove(int row, int col) {
    return model.isValidMove(row, col);
  }

  @Override
  public boolean hasGameStarted() {
    return model.hasGameStarted();
  }

  @Override
  public void startGame(List<ThreeTrioCard> deck, ThreeTrioCard[][] board) {
    model.startGame(deck, board);
  }

  @Override
  protected void flipAdjacentCards(int row, int col, ThreeTrioCard lastCard,
                                   List<ThreeTrioCard> flippedCards) {
    super.flipAdjacentCards(row, col, lastCard, flippedCards);
  }

  @Override
  protected void flipAdjacentCards(ThreeTrioCard[][] board, int row,
                                   int col, ThreeTrioCard lastCard,
                                   List<ThreeTrioCard> flippedCards) {
    super.flipAdjacentCards(board, row, col, lastCard, flippedCards);
  }

  @Override
  public void playToBoard(int row, int col, int handIdx) {
    System.out.println("abstract play to board");
    this.model.playToBoard(row, col, handIdx);
  }

  @Override
  public List<ThreeTrioCard> createDeck() throws FileNotFoundException {
    return model.createDeck();
  }

  @Override
  public ThreeTrioCard[][] createBoard() throws FileNotFoundException {
    return model.createBoard();
  }

  @Override
  public void startBattle(int row, int col) {
    if (!this.gameStarted || this.gameOver) {
      throw new IllegalStateException("The game is not started or is already over");
    }
    ThreeTrioCard lastCard = board[row][col];
    List<ThreeTrioCard> flippedCards = new ArrayList<>();
    flipAdjacentCards(row, col, lastCard, flippedCards);
  }

  @Override
  public void dealCards() {
    model.dealCards();
  }

  @Override
  public void setDeck(List<ThreeTrioCard> deck) {
    model.setDeck(deck);
  }

  @Override
  public void setPlayers(Player p1, Player p2) {
    model.setPlayers(p1, p2);
  }

  @Override
  public Player getActivePlayer() {
    return model.getActivePlayer();
  }

  @Override
  public void checkAndFlip(int adjRow, int adjCol, Direction dirFrom, Direction dirTo, int row,
                           int col, ThreeTrioCard lastCard, List<ThreeTrioCard> flippedCards) {
    System.out.println("THIS SHOULD NOT BE HERE");
    //to be implemented by subclasses
  }

  @Override
  public void checkAndFlip(ThreeTrioCard[][] board, int adjRow, int adjCol, Direction dirFrom,
                           Direction dirTo, int row, int col, ThreeTrioCard lastCard,
                           List<ThreeTrioCard> flippedCards) {
    System.out.println("THIS SHOULD NOT BE HERE");
    //to be implemented by subclasses
  }

}