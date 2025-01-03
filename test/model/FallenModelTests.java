package model;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.FileNotFoundException;

import static org.junit.Assert.assertEquals;

/**
 * Class to test the Fallen model of the game.
 */
public class FallenModelTests extends AbstractModelTests {
  ThreeTrioModel<ThreeTrioCard> model;

  @Before
  public void setup() throws FileNotFoundException {
    this.model = new ThreeTrioGame("board.config", "deck.config");
    this.deck = model.createDeck();
    this.board = model.createBoard();
    model.setDeck(this.deck);
    this.game = new FallenModel(model);
  }

  /**
   * Tests that the one beats the ace in a fallen game.
   */
  @Test
  public void testOneBeatsAce() {
    try {
      game.startGame(game.createDeck(), game.createBoard());
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }

    game.playToBoard(0, 1, 0);
    game.playToBoard(0, 0, 1);
    //make sure it flips in a line
    assertEquals(game.getBoard()[0][0].getColor(), game.getBoard()[0][1].getColor());
  }

  /**
   * Tests that when one beats ace the game score is properly updated.
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
    // Play to the board so blue flips red
    game.playToBoard(0, 1, 0);
    // Check that red went down a card
    Assert.assertEquals(game.getScore(Color.RED), 7);
    // Check that blue went up a card
    Assert.assertEquals(game.getScore(Color.BLUE), 9);
  }

  /**
   * Tests cards are properly flipped and the combo effect works including a ace vs one scenario.
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
