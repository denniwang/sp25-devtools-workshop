package view;

import controller.ThreeTrioControllerFeatures;
import model.ReadonlyThreeTrioModel;
import player.Player;

/**
 * Represents a mock GUI view of the game for testing purposes.
 */
public class MockGuiView implements ThreeTrioGuiView {

  private Appendable log;

  /**
   * Constructs a mock GUI view with the given log, model, and player.
   *
   * @param log    the log to append messages to
   * @param model  the model to render
   * @param player the player the view is for
   */
  public MockGuiView(Appendable log, ReadonlyThreeTrioModel model, Player player) {
    this.log = log;
  }

  @Override
  public void addFeaturesListener(ThreeTrioGuiFeatures features) {
    //mock does nothing
  }

  @Override
  public void addControllerFeaturesListener(ThreeTrioControllerFeatures features) {
    //mock does nothing
  }

  @Override
  public void display(boolean show) {
    //mock does nothing
  }

  @Override
  public void invalidPlay() {
    try {
      this.log.append("Please select a card from the hand first");
    } catch (Exception e) {
      //ignore
    }
  }

  @Override
  public void selectP1Card(int cardIdx) {
    try {
      this.log.append("Player 1 selected card " + cardIdx);
    } catch (Exception e) {
      //ignore
    }
  }

  @Override
  public void selectP2Card(int cardIdx) {
    try {
      this.log.append("Player 2 selected card " + cardIdx);
    } catch (Exception e) {
      //ignore
    }
  }

  @Override
  public void refresh() {
    try {
      this.log.append("Refreshed view");
    } catch (Exception e) {
      //ignore
    }
  }

  @Override
  public void notify(String message) {
    try {
      this.log.append(message);
    } catch (Exception e) {
      //ignore
    }
  }
}
