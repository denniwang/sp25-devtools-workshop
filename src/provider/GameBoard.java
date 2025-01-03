package provider;

import javax.swing.JPanel;


/**
 * This is the interface that has the methods related to creating the actual Grid Panel.
 */
public interface GameBoard {

  /**
   * Creates the grid panel and return it.
   *
   * @return the grid panel as a JPanel
   */
  JPanel createGridPanel();

  /**
   * Updates the cell at the given row and column with the given card.
   *
   * @param row  the row of the cell
   * @param col  the column of the cell
   * @param card the card to place in the cell
   */
  void updateCell(int row, int col, Card card);

  /**
   * Refreshes the board to reflect the current state of the game.
   */
  void refreshBoard();

  /**
   * Sets the game event listener for the game board.
   *
   * @param listener the game event listener
   */
  void setGameEventListener(EventListener listener);
}
