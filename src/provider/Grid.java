package provider;

/**
 * This is the interface for the Grid. It allows for the creation of a grid and
 * the placement of cards on the grid.
 */
public interface Grid {

  /**
   * Get the cell at the specified row and column.
   *
   * @param row the row index
   * @param col the column index
   * @return the cell at the specified position
   */
  Cell getCell(int row, int col);

  /**
   * Get the number of rows in the grid.
   *
   * @return the number of rows
   */
  int getRows();

  /**
   * Get the number of columns in the grid.
   *
   * @return the number of columns
   */
  int getCols();

  /**
   * Get the count of card cells in the grid.
   *
   * @return the count of card cells
   */
  int getCardCellCount();

  /**
   * Get the current grid.
   *
   * @return the current grid
   */
  Grid getGrid();

  /**
   * Get a copy of the grid.
   *
   * @return a copy of the grid
   */
  Grid getGridCopy();

  /**
   * Place a card in the grid at the specified row and column.
   *
   * @param row the row index
   * @param col the column index
   * @param card the card to place
   */
  void placeCardGrid(int row, int col, Card card);
}
