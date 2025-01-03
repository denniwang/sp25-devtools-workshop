package model;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.FileNotFoundException;

/**
 * Class to test the Plus Rule model of the game.
 */
public class PlusRuleTests extends AbstractModelTests {
  ThreeTrioModel<ThreeTrioCard> model;

  @Before
  public void setup() throws FileNotFoundException {
    this.model = new ThreeTrioGame("board.config", "deck.config");
    this.model.startGame(model.createDeck(), model.createBoard());
    this.deck = model.createDeck();
    this.board = model.createBoard();
    model.setDeck(this.deck);
    this.game = new PlusModel(model);
  }

  /**
   * Tests that with the plus rule cards with the same sum are flipped (if there are 2+).
   */
  @Test
  public void sameRuleFlipsWork() throws FileNotFoundException {
    ThreeTrioModel<ThreeTrioCard> newModel = new ThreeTrioGame("board2.config", "deck2.config");
    newModel.startGame(newModel.createDeck(), newModel.createBoard());
    this.deck = newModel.createDeck();
    this.board = newModel.createBoard();
    newModel.setDeck(this.deck);
    ThreeTrioGame newGame = new PlusModel(newModel);

    newGame.playToBoard(2, 1, 2);
    newGame.playToBoard(0, 1, 3);
    newGame.playToBoard(1, 1, 1);
    // Check that the second card placed is flipped to red because the sums are 5
    Assert.assertEquals(newGame.getBoard()[0][1].getColor(), newGame.getBoard()[1][1].getColor());
  }

  /**
   * Tests getBoard works as expected.
   */
  @Override
  @Test
  public void testGetBoard() {
    Assert.assertEquals(game.getBoard().length, 5);
    Assert.assertEquals(game.getBoard()[0].length, 7);
  }
}
