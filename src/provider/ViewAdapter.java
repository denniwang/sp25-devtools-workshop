package provider;

import java.io.FileNotFoundException;

import controller.ThreeTrioControllerFeatures;
import player.Player;
import view.ThreeTrioGuiFeatures;
import view.ThreeTrioGuiView;

/**
 * This class adapts the GUI features to an event listener.
 */
public class ViewAdapter extends ThreeTriosFrame implements ThreeTrioGuiView {
  //private final ThreeTriosFrame frame;
  private final ModelAdapter model;

  /**
   * This is the constructor for the Frame.
   *
    * @param model model to display
   */
  public ViewAdapter(ModelAdapter model, Player player) throws FileNotFoundException {
    super(model.gridSetup());
    this.model = model;
    //this.frame = new ThreeTriosFrame(model);
  }

  @Override
  public void addFeaturesListener(ThreeTrioGuiFeatures features) {
    setGameEventListener(new GuiFeaturesToEventListener(features, model, this));
  }

  @Override
  public void addControllerFeaturesListener(ThreeTrioControllerFeatures features) {
    // setGameEventListener(new ControllerFeaturesToEventListener(features, model,this));
  }

  @Override
  public void display(boolean show) {
    if (show) {
      this.setVisible(true);
    } else {
      this.setVisible(false);
    }
  }

  @Override
  public void invalidPlay() {
    this.notify("Please select a card from the hand first");
  }

  @Override
  public void selectP1Card(int cardIdx) {
    this.highlightSelectedCard(model.getRedPlayerHand().get(cardIdx), PlayerColor.RED);
  }

  @Override
  public void selectP2Card(int cardIdx) {
    this.highlightSelectedCard(model.getBluePlayerHand().get(cardIdx), PlayerColor.BLUE);
  }

  @Override
  public void notify(String message) {
    showMessage(message);
  }
}
