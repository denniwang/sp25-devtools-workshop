package view;

import model.ReadonlyThreeTrioModel;
import model.ThreeTrioCard;

/**
 * Class to be the string view of Three Trio. This is NOT a GUI view but rather
 * a text-based view of the game primarily for testing purposes.
 */
public class GameView implements ThreeTrioView {
  private ReadonlyThreeTrioModel<ThreeTrioCard> model;

  /**
   * Constructor for the game view. Takes in a model to be rendered.
   *
   * @param model the model of the game
   */
  public GameView(ReadonlyThreeTrioModel model) {
    this.model = model;
  }

  private String boardToString() {
    StringBuilder sb = new StringBuilder();
    int rowIdx = 0;
    for (ThreeTrioCard[] row : model.getBoard()) {
      for (ThreeTrioCard card : row) {
        sb.append(card.shortString());
      }
      if (rowIdx++ < model.getBoardH()) {
        sb.append("\n");
      }
    }
    return sb.toString();
  }

  private String handToString() {
    StringBuilder sb = new StringBuilder();
    int cardIdx = 0;
    for (ThreeTrioCard card : model.getPlayerHand()) {
      sb.append(card.toString());
      if (cardIdx++ < model.getPlayerHand().size() - 1) {
        sb.append(System.lineSeparator());
      }
    }
    return sb.toString();
  }

  /**
   * Renders the game view as a string.
   *
   * @return a string that displaying the deck, board, player hands,
   *     and information like turn information.
   */
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("Player: " + (model.getTurn() ? "Player 1" : "Player 2"));
    sb.append(System.lineSeparator());
    sb.append(boardToString());
    sb.append(System.lineSeparator());
    sb.append("Hand:");
    sb.append(System.lineSeparator());
    sb.append(handToString());
    return sb.toString();
  }
}
