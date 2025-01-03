package model;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Class to test the model of the game.
 */
public class NormalModelTests extends AbstractModelTests {

  @Before
  public void setup() throws FileNotFoundException {
    this.game = new ThreeTrioGame("board.config", "deck.config");
    this.deck = game.createDeck();
    this.board = game.createBoard();
    game.setBoard(this.board);
    game.setDeck(this.deck);
  }

  /**
   * Testing that game deals (N+1)/2 where N is the number of tiles on the board.
   */
  @Test
  public void testDealHalf() throws FileNotFoundException {
    game.dealCards();
    assertEquals(game.getPlayerOneHand().size(), (game.getNumTiles() + 1) / 2);
  }

  /**
   * Tests that getPlayerOneHand returns an immutable copy of the player one hand.
   */
  @Test
  public void testGetPlayerOneHandImmutable() {
    game.dealCards();
    List<ThreeTrioCard> originalHand = game.getPlayerOneHand();
    List<ThreeTrioCard> handCopy = game.getPlayerOneHand();

    // Modify the copy
    handCopy.remove(0);

    // Verify that the original hand remains unchanged
    Assert.assertNotEquals(originalHand.size(), handCopy.size());
    Assert.assertEquals(originalHand.size(), game.getPlayerOneHand().size());
  }

  /**
   * Tests that getPlayerOneHand returns an immutable copy of the player one hand.
   */
  @Test
  public void testGetPlayerTwoHandImmutable() {
    game.dealCards();
    List<ThreeTrioCard> originalHand = game.getPlayerTwoHand();
    List<ThreeTrioCard> handCopy = game.getPlayerTwoHand();

    // Modify the copy
    handCopy.remove(0);

    // Verify that the original hand remains unchanged
    Assert.assertNotEquals(originalHand.size(), handCopy.size());
    Assert.assertEquals(originalHand.size(), game.getPlayerTwoHand().size());
  }

  @Test
  public void testGetScore() {
    try {
      game.startGame(game.createDeck(), game.createBoard());
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    // Check before any cards are played the score is the # of cards in both players hands
    Assert.assertEquals(game.getScore(Color.RED), game.getPlayerOneHand().size());
    Assert.assertEquals(game.getScore(Color.BLUE), game.getPlayerTwoHand().size());
    // Check score increases when placing a card to the board for both players,
    // and then the game will refresh their hands
    game.playToBoard(0, 0, 0);
    // Play to the board so blue flips red
    game.playToBoard(0, 1, 0);
    // Check that red went down a card
    Assert.assertEquals(game.getScore(Color.RED), 7);
    // Check that blue went up a card
    Assert.assertEquals(game.getScore(Color.BLUE), 9);
  }

  /**
   * Tests cards are properly flipped and the combo effect works.
   */
  @Test
  public void testComboFlip() {
    try {
      game.startGame(game.createDeck(), game.createBoard());
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    game.playToBoard(0, 0, 0);
    game.playToBoard(1, 0, 2);
    game.playToBoard(2, 0, 1);
    //make sure it flips in a line
    assertEquals(game.getBoard()[0][0].getColor(), game.getBoard()[2][0].getColor());
  }

}
