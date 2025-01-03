package provider;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import model.Color;
import model.Direction;
import model.PlayingCard;
import model.ThreeTrioCard;
import model.ThreeTrioModel;

/**
 * An adapter class that adapts the ThreeTrioModel class to the ReadonlyThreeTriosModel interface.
 */
public class ModelAdapter implements ReadonlyThreeTriosModel {

  private ThreeTrioModel model;

  /**
   * Constructs a model adapter with the given model and grid path.
   *
   * @param model    the model to adapt
   * @param gridPath the path to the grid
   * @throws FileNotFoundException if the file is not found`
   */
  public ModelAdapter(ThreeTrioModel model, String gridPath) throws FileNotFoundException {
    this.model = model;
    //cardSetup();
    gridSetup();
    //model.startGame(model.createDeck(),model.createBoard());
  }

  @Override
  public PlayerColor getCardOwner(Card card) {
    try {
      List<ThreeTrioCard> cards = model.createDeck();
      for (int i = 0; i < cards.size(); i++) {
        if (cards.get(i).getName().equals(card.getName())) {
          return i % 2 == 0 ? PlayerColor.RED : PlayerColor.BLUE;
        }
      }
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    return null; // should never reach
  }

  @Override
  public PlayerColor getCurrentPlayer() {
    return this.model.getTurn() ? PlayerColor.RED : PlayerColor.BLUE;
  }

  @Override
  public Grid getGridCopy() {
    Cell[][] board = boardOfThreeTrioCardToCell(this.model.getBoard());
    return new GridAdapter(board, this.model.getBoard());
  }

  @Override
  public List<Card> getCurrentPlayerHand() {
    if (this.model.getTurn()) {
      return getRedPlayerHand();
    } else {
      return getBluePlayerHand();
    }
  }

  @Override
  public boolean isGameOver() {
    return this.model.isGameOver();
  }

  @Override
  public boolean isGameWon() {
    return this.model.getWinner() != null;
  }

  @Override
  public PlayerColor getWinningPlayer() {
    return this.model.getWinner() == Color.RED ? PlayerColor.RED : PlayerColor.BLUE;
  }

  @Override
  public int getScore(PlayerColor player) {
    Color color = player == PlayerColor.RED ? Color.RED : Color.BLUE;
    return this.model.getScore(color);
  }

  @Override
  public List<Card> getRedPlayerHand() {
    return threeTrioCardsToCards(this.model.getPlayerOneHand());
  }

  @Override
  public List<Card> getBluePlayerHand() {
    return threeTrioCardsToCards(this.model.getPlayerTwoHand());
  }

  private List<Card> threeTrioCardsToCards(List<ThreeTrioCard> cards) {
    List<Card> newCards = new ArrayList<>();
    for (ThreeTrioCard card : cards) {
      newCards.add(new CardAdapter(card.getName(), card.getAttacks().get(Direction.NORTH),
              card.getAttacks().get(Direction.SOUTH),
              card.getAttacks().get(Direction.EAST),
              card.getAttacks().get(Direction.WEST)));
    }
    return newCards;
  }

  private Cell[][] boardOfThreeTrioCardToCell(ThreeTrioCard[][] board) {
    Cell[][] newBoard = new Cell[this.model.getBoardH()][this.model.getBoardW()];
    for (int i = 0; i < this.model.getBoardH(); i++) {
      for (int j = 0; j < this.model.getBoardW(); j++) {
        if (board[i][j].isHole()) {
          newBoard[i][j] = new CellAdapter(i, j, new PlayingCard(true));
        } else if (board[i][j].getName() == null) {
          newBoard[i][j] = new CellAdapter(i, j, new PlayingCard());
        } else {
          newBoard[i][j] = new CellAdapter(i, j, new PlayingCard(board[i][j].getName(),
                  board[i][j].getAttacks().get(Direction.NORTH),
                  board[i][j].getAttacks().get(Direction.SOUTH),
                  board[i][j].getAttacks().get(Direction.EAST),
                  board[i][j].getAttacks().get(Direction.WEST)));
        }
      }
    }
    return newBoard;
  }

  private Cell[][] threeTrioCardsToCellArray(ThreeTrioCard[][] board) {
    Cell[][] grid = new Cell[board.length][board[0].length];
    for (int i = 0; i < board.length; i++) {
      for (int j = 0; j < board[0].length; j++) {
        grid[i][j] = new CellAdapter(i, j, board[i][j]);
      }
    }
    return grid;
  }

  /**
   * Sets up the cards for the game.
   *
   * @throws FileNotFoundException if the file is not found
   */
  public void cardSetup() throws FileNotFoundException {
    this.model.setDeck(this.model.createDeck());
    this.model.dealCards();
  }

  /**
   * Sets up the grid for the game.
   *
   * @return the model adapter
   * @throws FileNotFoundException if the file is not found
   */
  public ModelAdapter gridSetup() throws FileNotFoundException {
    this.model.setBoard(this.model.createBoard());
    return this;
  }
}
