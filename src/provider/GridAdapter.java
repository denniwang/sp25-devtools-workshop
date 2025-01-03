package provider;

import model.ThreeTrioCard;
import model.ThreeTrioGame;

/**
 * This class adapts the ThreeTrioGame class to the Grid interface.
 */
public class GridAdapter extends ThreeTrioGame implements Grid {
  private Cell[][] board;
  private ThreeTrioCard[][] modelBoard;

  /**
   * Constructs a grid adapter with the given board and model board.
   *
   * @param board      board to adapt
   * @param modelBoard model board to adapt
   */
  public GridAdapter(Cell[][] board, ThreeTrioCard[][] modelBoard) {
    this.board = board;
    this.modelBoard = modelBoard;
  }

  @Override
  public Cell getCell(int row, int col) {
    return new CellAdapter(row, col, this.modelBoard[row][col]);
  }

  @Override
  public int getRows() {
    return board.length;
  }

  @Override
  public int getCols() {
    return board[0].length;
  }

  @Override
  public int getCardCellCount() {
    return board.length * board[0].length;
  }

  @Override
  public Grid getGrid() {
    return threeTrioCardsToGrid(getBoard());
  }

  @Override
  public Grid getGridCopy() {
    return threeTrioCardsToGrid(getBoard()); // come back to make sure its deep copy
  }

  @Override
  public void placeCardGrid(int row, int col, Card card) {
    int idxToPlay = -1;
    for (int i = 0; i < getPlayerHand().size(); i++) {
      if (getPlayerHand().get(i).equals(card)) {
        idxToPlay = i;
        break;
      }
    }
    playToBoard(row, col, idxToPlay);
  }

  private Grid threeTrioCardsToGrid(ThreeTrioCard[][] board) {
    Cell[][] grid = new Cell[board.length][board[0].length];
    for (int i = 0; i < board.length; i++) {
      for (int j = 0; j < board[0].length; j++) {
        grid[i][j] = new CellAdapter(i, j, board[i][j]);
      }
    }
    return new GridAdapter(grid, board);
  }
}
