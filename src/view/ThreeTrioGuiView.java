package view;


import controller.ThreeTrioControllerFeatures;

/**
 * Represents the GUI view of the game, represents a model of a ThreeTrio game in a JFrame.
 * The view has a controller subscribe as a listener to handle user input and exceptions.
 * The view also has a panel subscribed as a listener to draw the actual view and
 * interact with the model.
 */
public interface ThreeTrioGuiView {
  /**
   * Adds a listener to the view to listen for features.
   * This allows the view to interact with the model
   *
   * @param features the features to listen for
   */
  void addFeaturesListener(ThreeTrioGuiFeatures features);

  /**
   * Adds a listener to the view to listen for controller features.
   * This allows the view to interact with the controller
   *
   * @param features the features to listen for
   */
  void addControllerFeaturesListener(ThreeTrioControllerFeatures features);

  /**
   * Starts rendering view.
   *
   * @param show whether to display the view
   */
  void display(boolean show);

  /**
   * Displays a message to the user. Tell them that the play is invalid and they should try again.
   */
  void invalidPlay();

  /**
   * Selects a card from player 1's hand. Highlights the selected card in the view.
   *
   * @param cardIdx index of card from player 1's hand
   */
  void selectP1Card(int cardIdx);

  /**
   * Selects a card from player 2's hand. Highlights the selected card in the view.
   *
   * @param cardIdx index of card from player 2's hand
   */
  void selectP2Card(int cardIdx);

  /**
   * Refreshes the view to reflect changes in the model.
   */
  void refresh();

  /**
   * Notifies the user of a message.
   *
   * @param message message to display.
   */
  void notify(String message);

}
