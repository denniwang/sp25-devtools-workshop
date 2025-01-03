package provider;

/**
 * This interface represents a cell in the grid.
 * It includes methods for different cell types and card management.
 */
public interface Cell {

  /**
   * Get the type of the cell.
   *
   * @return the cell type
   */
  CellType getType();

  /**
   * Check if the cell is empty.
   *
   * @return true if the cell is empty, false otherwise
   */
  boolean isEmpty();

  /**
   * Set a card at the specified cell.
   *
   * @param card the card to set at the cell
   * @throws IllegalArgumentException if the cell type is a hole
   */
  void setCard(Card card);

  /**
   * Get the card at the cell.
   *
   * @return the card at the cell, or null if the cell is empty
   */
  Card getCard();
}