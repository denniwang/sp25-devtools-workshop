package model;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.FileNotFoundException;

/**
 * Class to test the Reverse Fallen Rule model of the game.
 */
public class ReverseFallenModelTest extends AbstractModelTests {
  ThreeTrioModel<ThreeTrioCard> model;
  FallenModel fallen;

  @Before
  public void setup() throws FileNotFoundException {
    this.model = new ThreeTrioGame("board.config", "deck.config");
    this.deck = model.createDeck();
    this.board = model.createBoard();
    model.setDeck(this.deck);
    this.fallen = new FallenModel(model);
    this.game = new ReverseModel(this.fallen);
  }

  /**
   * Tests that an ace will beat a one because the fallen game is reversed.
   */
  @Test
  public void AceBeatsOne() {
    try {
      game.startGame(game.createDeck(), game.createBoard());
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }

    //Play a one
    game.playToBoard(0, 1, 2);
    // Flip the one with an Ace because it is a reverse fallen game
    game.playToBoard(0, 0, 2);
    Assert.assertTrue(game.getBoard()[0][0].getColor().equals(game.getBoard()[0][1].getColor()));
  }
}
