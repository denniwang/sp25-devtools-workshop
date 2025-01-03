package provider;

import view.ThreeTrioGuiFeatures;
import view.ThreeTrioGuiView;

/**
 * This class adapts the GUI features to an event listener.
 */
public class GuiFeaturesToEventListener implements EventListener {
  private final ThreeTrioGuiFeatures features;
  private final ModelAdapter model;
  private final ThreeTrioGuiView view;
  private int cardIdx = -1;

  /**
   * Constructor for the GuiFeaturesToEventListener class.
   *
   * @param features the GUI features
   * @param model    the model adapter to be used
   * @param view     the view to be used
   */
  public GuiFeaturesToEventListener(ThreeTrioGuiFeatures features,
                                    ModelAdapter model, ThreeTrioGuiView view) {
    this.features = features;
    this.model = model;
    this.view = view;
  }

  @Override
  public void onCellClicked(int row, int col) {
    if (cardIdx == -1) {
      return;
    }
    features.playToBoard(row, col, cardIdx);
    cardIdx = -1;
    System.out.println("Cell clicked, refresh view");
    view.refresh();
  }

  @Override
  public void onCardSelected(Card card, PlayerColor owner) {
    cardIdx = owner == PlayerColor.RED ?
            model.getRedPlayerHand().indexOf(card) :
            model.getBluePlayerHand().indexOf(card);
    view.selectP2Card(cardIdx);
  }
}
