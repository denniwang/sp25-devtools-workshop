package model;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.FileNotFoundException;


/**
 * Class to test the Reverse Rule model of the game.
 */
public class ReverseModelTests extends AbstractModelTests {
  ThreeTrioModel<ThreeTrioCard> model;

  @Before
  public void setup() throws FileNotFoundException {
    this.model = new ThreeTrioGame("board.config", "deck.config");
    this.deck = model.createDeck();
    this.board = model.createBoard();
    model.setDeck(this.deck);
    this.game = new ReverseModel(model);
  }

  /**
   * Tests that cards are flipped opposite to how they normally would be in the normal game.
   */
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
    // Play to the board so RED flips BLUE instead of the other way around
    game.playToBoard(0, 1, 0);
    // Check that red went down a card
    Assert.assertEquals(game.getScore(Color.RED), 8);
    // Check that blue went up a card
    Assert.assertEquals(game.getScore(Color.BLUE), 8);
  }

  /**
   * Tests that a card that would be flipped normally is not flipped.
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
    Assert.assertFalse(game.getBoard()[0][0].getColor().equals(game.getBoard()[2][0].getColor()));
  }
}
