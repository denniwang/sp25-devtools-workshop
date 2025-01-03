import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.util.List;

import model.Color;
import model.ThreeTrioCard;
import model.ThreeTrioGame;
import view.GameView;

/**
 * Class to test actual gameplay with the model.
 */
public class GameplayTest {

  private ThreeTrioGame game;
  private List<ThreeTrioCard> deck;
  private ThreeTrioCard[][] board;
  private GameView view;

  @Before
  public void setup() throws FileNotFoundException {
    game = new ThreeTrioGame("board.config", "deck.config");
    this.deck = game.createDeck();
    this.board = game.createBoard();
    game.setBoard(this.board);
    game.setDeck(this.deck);
    GameView view = new GameView(game);
  }

  /**
   * Tests playing to board will switch the current turn.
   */
  @Test
  public void testPlayToBoardSwitchesTurn() {
    try {
      game.startGame(game.createDeck(), game.createBoard());
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    game.playToBoard(1, 0, 0);
    Assert.assertEquals(game.getTurn(), false);
    game.playToBoard(0, 0, 2);
    Assert.assertEquals(game.getTurn(), true);
  }

  /**
   * Tests playing to board works as expected.
   */
  @Test
  public void testPlayingCardToShortString() {
    try {
      game.startGame(game.createDeck(), game.createBoard());
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    Assert.assertEquals(game.getBoard()[0][0].shortString(), "_");
    Assert.assertEquals(game.getBoard()[0][2].shortString(), "X");
  }

  /**
   * Tests playing an entrie game behaves properly and a winner is declared.
   */
  @Test
  public void testShortGame() throws FileNotFoundException {
    game = new ThreeTrioGame("board1.config", "deck1.config");
    this.deck = game.createDeck();
    this.board = game.createBoard();
    game.setBoard(this.board);
    game.setDeck(this.deck);

    try {
      game.startGame(game.createDeck(), game.createBoard());
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    game.playToBoard(0, 0, 0);
    game.playToBoard(0, 1, 0);
    game.playToBoard(0, 2, 0);
    game.playToBoard(1, 0, 0);
    game.playToBoard(2, 0, 0);
    game.playToBoard(2, 1, 0);
    game.playToBoard(2, 2, 0);
    Assert.assertTrue(game.isGameOver());
    Assert.assertEquals(game.getWinner(), Color.RED);
  }

  /**
   * Tests that a card doesn't flip if the new card doesn't beat the existing card.
   */
  @Test
  public void testCardDoesNotFlipIfNotBeaten() throws FileNotFoundException {
    game = new ThreeTrioGame("board1.config", "deck1.config");
    this.deck = game.createDeck();
    this.board = game.createBoard();
    game.setBoard(this.board);
    game.setDeck(this.deck);

    try {
      game.startGame(game.createDeck(), game.createBoard());
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    GameView view = new GameView(game);
    //very big card
    game.playToBoard(0, 0, 2);
    //small card
    game.playToBoard(0, 1, 0);
    System.out.println(view.toString());
    // original big card should not be flipped
    Assert.assertEquals(game.getBoard()[0][0].shortString(), "R");
  }

  /**
   * Tests that a card doesn't flip card from own team.
   */
  @Test
  public void testCantFlipSelf() throws FileNotFoundException {
    game = new ThreeTrioGame("board1.config", "deck1.config");
    this.deck = game.createDeck();
    this.board = game.createBoard();
    game.setBoard(this.board);
    game.setDeck(this.deck);

    try {
      game.startGame(game.createDeck(), game.createBoard());
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    //med P1 card
    game.playToBoard(0, 0, 3);
    //small P2 card
    game.playToBoard(0, 1, 0);
    //BIG p1 card
    game.playToBoard(1, 0, 2);
    // original P1 card in top right should not be flipped
    Assert.assertEquals(game.getBoard()[0][0].shortString(), "R");
  }

  /**
   * Tests that game isn't over until all cards played or deck is filled.
   */
  @Test
  public void testGameNotOver() throws FileNotFoundException {
    game = new ThreeTrioGame("board1.config", "deck1.config");
    this.deck = game.createDeck();
    this.board = game.createBoard();
    game.setBoard(this.board);
    game.setDeck(this.deck);

    try {
      game.startGame(game.createDeck(), game.createBoard());
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    //med P1 card
    game.playToBoard(0, 0, 3);
    //small P2 card
    game.playToBoard(0, 1, 0);
    //BIG p1 card
    game.playToBoard(1, 0, 2);
    // original P1 card in top right should not be flipped
    Assert.assertFalse(game.isGameOver());
  }

  @Test
  public void testRender() {
    try {
      game.startGame(game.createDeck(), game.createBoard());
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    GameView view = new GameView(game);
    String expectedOutput = "Player: Player 1\n" +
            "__XXXX_\n" +
            "_X_XXX_\n" +
            "_XX_XX_\n" +
            "_XXX_X_\n" +
            "_XXXX__\n\n" +
            "Hand:\n" +
            "John 1 2 3 4\n" +
            "Alice 5 6 7 8\n" +
            "Charlie 9 8 7 6\n" +
            "Eve 6 5 4 3\n" +
            "Grace 4 3 2 1\n" +
            "Ivy 1 9 8 7\n" +
            "Kevin 3 5 7 9\n" +
            "Mike 5 7 9 3";
    Assert.assertEquals(expectedOutput, view.toString());
  }

  /**
   * Tests that creating a board with an even number of tiles throws an exception.
   */
  @Test(expected = IllegalStateException.class)
  public void testBoardCreationWithEvenNumberOfTilesThrowsException() throws FileNotFoundException {
    game = new ThreeTrioGame("invalid.config", "deck.config");
    this.board = game.createBoard();
  }

}
