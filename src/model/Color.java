package model;

/**
 * Enum to represent the color of a card.
 */
public enum Color {
  RED('R'), BLUE('B');
  public final char asChar;

  Color(char color) {
    this.asChar = color;
  }

  /**
   * Returns the color as a single character string.
   *
   * @return the color as a single character string
   */
  public String toString() {
    return "" + this.asChar;
  }
}
