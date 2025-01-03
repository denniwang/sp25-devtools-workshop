package playerstrategy;


import java.util.HashMap;

import model.ReadonlyThreeTrioModel;
import model.ThreeTrioCard;

/**
 * Factory class to create a strategy based on the given type.
 */
public class StrategyFactory {
  /**
   * Creates a strategy based on the given type.
   *
   * @param type  the type of strategy to create
   * @param model the model to create the strategy for
   * @return the strategy created
   */
  public static PlayerStrategy createStrategy(StrategyType type,
                                              ReadonlyThreeTrioModel<ThreeTrioCard> model) {
    try {
      switch (type) {
        case MINIMAX:
          return new MinimaxStrategy(model, new HashMap<>());
        case CORNER:
          return new CornerStrategy(model);
        case COMPLEX:
          return new ComplexPlayerStrategy(new CornerStrategy(model),
                  new MinimaxStrategy(model, new HashMap<>()));
        case DEFENSE:
          return new LeastLikelyToFlipStrategy(model);
        case FLIPMAX:
          return new FlipMaxCardsStrategy(model);
        default:
          return null;
      }
    } catch (Exception e) {
      e.printStackTrace();
      return null; // Handle the exception as needed
    }
  }
}