package model;

/**
 * Enum to represent the directions where attack values are correlated to on a playable card.
 * Each playable card in the game will have an associated attack value in each of these directions.
 */
public enum Direction {
  NORTH, SOUTH, EAST, WEST;

  /**
   * Converts a provider direction to a model direction.
   *
   * @param providerDirection the provider direction to convert
   * @return the model direction corresponding to the given provider direction
   */
  public static Direction fromProviderDirection(provider.Direction providerDirection) {
    switch (providerDirection) {
      case NORTH:
        return Direction.NORTH;
      case SOUTH:
        return Direction.SOUTH;
      case EAST:
        return Direction.EAST;
      case WEST:
        return Direction.WEST;
      default:
        throw new IllegalArgumentException("Unknown provider direction: " + providerDirection);
    }
  }
}
