package provider;

import controller.ThreeTrioControllerFeatures;
import view.ThreeTrioGuiView;

/**
 * This class adapts the controller features to an event listener.
 */
public class ControllerFeaturesToEventListener implements EventListener {
  private final ThreeTrioControllerFeatures features;
  private final ModelAdapter model;
  private final ThreeTrioGuiView view;
  private int cardIdx = -1;

  /**
   * Constructor for the ControllerFeaturesToEventListener class.
   *
   * @param features the controller features
   * @param model    the model adapter to be used
   * @param view     the view to be used
   */
  public ControllerFeaturesToEventListener(ThreeTrioControllerFeatures features,
                                           ModelAdapter model, ThreeTrioGuiView view) {
    this.features = features;
    this.model = model;
    this.view = view;
  }

  @Override
  public void onCellClicked(int row, int col) {
    System.out.println("THIS IS HIT WHENEVER WE CLICK ON A CELL");
    features.playToBoard(row, col, this.cardIdx);
    view.refresh();
  }

  @Override
  public void onCardSelected(Card card, PlayerColor owner) {
    System.out.println("THIS is HIT WHEn WE SELECT A CARD FROM HAND");
    boolean isPlayerOne = model.getCurrentPlayer() == PlayerColor.RED;
    this.cardIdx = owner == PlayerColor.RED ?
            model.getRedPlayerHand().indexOf(card)
            : model.getBluePlayerHand().indexOf(card);
    features.selectCard(cardIdx, isPlayerOne);
  }
}
