package playerstrategy;



/**
 * Represents a complex player strategy that is a combination of multiple strategies.
 * Currently just a queue of strategies to be played.
 */
public class ComplexPlayerStrategy implements PlayerStrategy {
  // A list of strategies to be played, acts as a queue
  private PlayerStrategy head;
  private PlayerStrategy next;

  /**
   * Constructs a complex player strategy with a head strategy and a next strategy.
   * Linked list?
   *
   * @param head the first strategy to be played
   * @param next the strategy to be played after the head is played
   */
  public ComplexPlayerStrategy(PlayerStrategy head, PlayerStrategy next) {
    this.head = head;
    this.next = next;
  }

  /**
   * Get the best move for the current strategy and move on to the next.
   *
   * @return the best move for the current strategy
   */
  @Override
  public Move getMove() {
    Move curMove = head.getMove();
    head = next;
    return curMove;
  }


  @Override
  public int getScore(Move move) {
    return head.getScore(move);
  }

  /**
   * Adds a strategy to be played after this one, to form more complex strategies.
   *
   * @param nextStrategy the strategy to be played next
   * @return the complex strategy with the new strategy added
   */
  public PlayerStrategy then(PlayerStrategy nextStrategy) {
    if (next == null) {
      return new ComplexPlayerStrategy(this, nextStrategy);
    } else {
      next.then(nextStrategy);
      return this;
    }
  }
}
