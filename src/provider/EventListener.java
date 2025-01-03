package provider;


/**
 * This is the interface for EventListener. It allows for mouse clicking.
 */
public interface EventListener {
  /**
   * Called when a cell is clicked.
   *
   * @param row the row of the cell
   * @param col the column of the cell
   */
  void onCellClicked(int row, int col);

  /**
   * Called when a card is selected.
   *
   * @param card  the card that was selected
   * @param owner the owner of the card
   */
  void onCardSelected(Card card, PlayerColor owner);
}
