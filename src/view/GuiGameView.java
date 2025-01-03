package view;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import controller.ThreeTrioControllerFeatures;
import model.ReadonlyThreeTrioModel;
import player.Player;


/**
 * Represents the GUI view of the game.
 * Renders a model of a ThreeTrio game in a JFrame.
 * To use, just pass in a readOnly TT model and it should render correctly!
 * The controller can be added to the view to listen for user input.
 * The panel is also subscribed as a listener to draw the actual view.
 */
public class GuiGameView extends JFrame implements ThreeTrioGuiView {
  private final ThreeTrioPanel panel;
  private final Player player;
  private ReadonlyThreeTrioModel model;
  private int selectedCardIdx;

  /**
   * Constructs a GuiGameView to render a given model.
   *
   * @param model model to be rendered
   */
  public GuiGameView(ReadonlyThreeTrioModel model, Player player, boolean easyMode) {
    if (model == null || player == null) {
      throw new IllegalArgumentException("Game and player cannot be null");
    }
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.model = model;
    this.setPreferredSize(new Dimension(800, 600));
    if (easyMode) {
      this.panel = new EasyPanel(model);
    } else {
      //System.out.println("not easy mode");
      this.panel = new ThreeTrioPanel(model);
    }
    this.add(panel);
    this.pack();
    this.selectedCardIdx = -1;
    this.player = player;
    // Debug statements
    //System.out.println("Player: " + player.getName());
    //System.out.println("Current Player: " + model.getActivePlayer());
    //System.out.println("Turn: " + model.getTurn());
    this.setTitle("I am: " + player.getName() + ", Current Player: " +
            model.getActivePlayer().getName() + " - " + (model.getTurn() ? "RED" : " BLUE"));
  }

  /**
   * Adds a features listener to the view.
   *
   * @param features the features to listen for
   */
  @Override
  public void addFeaturesListener(ThreeTrioGuiFeatures features) {
    this.panel.addFeaturesListener(features);
  }

  /**
   * Adds a controller features listener to the view.
   *
   * @param features the features to listen for
   */
  @Override
  public void addControllerFeaturesListener(ThreeTrioControllerFeatures features) {
    this.panel.addControllerFeaturesListener(features);
  }

  /**
   * Displays the view.
   *
   * @param show whether to display the view
   */
  @Override
  public void display(boolean show) {
    this.setVisible(show);
  }

  /**
   * Refreshes the view to reflect changes in the model.
   */
  public void refresh() {
    this.setTitle("I am: " + player.getName() + ", Current Player: " +
            model.getActivePlayer().getName() + " - " + (model.getTurn() ? "RED" : " BLUE"));
    this.panel.repaint();
    super.repaint();
  }

  /**
   * Notifies the current player with a message.
   *
   * @param message message to display.
   */
  @Override
  public void notify(String message) {
    JOptionPane.showMessageDialog(this, message);
  }


  /**
   * Notifies the current player that they need to select a card from their hand first.
   */
  @Override
  public void invalidPlay() {
    this.notify("Player " + model.getActivePlayer().getName() +
            ": Please select a card from the hand first");
  }

  /**
   * Selects a card from player 1's hand.
   *
   * @param cardIdx index of card from player 1's hand
   */
  @Override
  public void selectP1Card(int cardIdx) {
    this.selectedCardIdx = cardIdx;
    // this.panel.playerOneHandClicked(cardIdx);
    this.panel.setSelectedCardIdx(cardIdx);
  }

  /**
   * Selects a card from player 2's hand.
   *
   * @param cardIdx index of card from player 2's hand
   */
  @Override
  public void selectP2Card(int cardIdx) {
    this.selectedCardIdx = cardIdx;
    this.panel.setSelectedCardIdx(cardIdx);
  }
}
