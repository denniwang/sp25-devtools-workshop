package provider;

import java.util.List;

import javax.swing.JPanel;

/**
 * This is the interface that creates the methods to show a player's hand.
 */
public interface PlayerHandDisplay {

  /**
   * Creates the hand panel and returns it.
   *
   * @return the hand panel as a JPanel
   */
  JPanel createHandPanel();

  /**
   * Updates the hand with the given cards.
   *
   * @param cards the cards to display in the hand
   */
  void updateHand(List<Card> cards);

  /**
   * Highlights the given card in the hand.
   *
   * @param card      the card to highlight
   * @param highlight whether to highlight the card
   */
  void highlightCard(Card card, boolean highlight);

  /**
   * Sets the game event listener for the player hand.
   *
   * @param listener the game event listener
   */
  void setGameEventListener(EventListener listener);
}
