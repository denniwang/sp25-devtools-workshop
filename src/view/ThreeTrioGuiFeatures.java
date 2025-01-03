package view;


/**
 * Interface for the features of the ThreeTrio game GUI.
 */
public interface ThreeTrioGuiFeatures {

  /**
   * Allows the gui to tell the model to play a card somewhere.
   *
   * @param row     row to play the card to
   * @param col     column to play the card to
   * @param cardIdx index of the card to play, player is determined by the model
   */
  void playToBoard(int row, int col, int cardIdx);
}
