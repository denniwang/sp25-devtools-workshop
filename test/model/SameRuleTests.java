package model;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.FileNotFoundException;

import static org.junit.Assert.assertEquals;

/**
 * Class to test the Same Rule model of the game.
 */
public class SameRuleTests extends AbstractModelTests {
  ThreeTrioModel<ThreeTrioCard> model;

  @Before
  public void setup() throws FileNotFoundException {
    this.model = new ThreeTrioGame("board.config", "deck.config");
    this.model.startGame(model.createDeck(), model.createBoard());
    this.deck = model.createDeck();
    this.board = model.createBoard();
    model.setDeck(this.deck);
    this.game = new SameModel(model);
  }

  /**
   * Tests that the same rule rules work properly.
   * (if there are 2+ cards who's opposing value is equal to a center card).
   */
  @Test
  public void sameRuleFlipsWork() throws FileNotFoundException {
    ThreeTrioModel<ThreeTrioCard> newModel = new ThreeTrioGame("board2.config",
            "deck2.config");
    newModel.startGame(newModel.createDeck(), newModel.createBoard());
    this.deck = newModel.createDeck();
    this.board = newModel.createBoard();
    newModel.setDeck(this.deck);
    ThreeTrioGame newGame = new PlusModel(newModel);

    newGame.playToBoard(1, 0, 2);
    newGame.playToBoard(1, 2, 1);
    newGame.playToBoard(1, 1, 2);
    // Check that the second card placed is flipped to red
    assertEquals(newGame.getBoard()[0][0].getColor(), newGame.getBoard()[0][1].getColor());
  }

  /**
   * Tests getBoard works as expected.
   */
  @Test
  public void testGetBoard() {
    Assert.assertEquals(game.getBoard().length, 5);
    Assert.assertEquals(game.getBoard()[0].length, 7);
  }

}
