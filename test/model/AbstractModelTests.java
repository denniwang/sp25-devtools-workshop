package model;

import org.junit.Assert;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.util.List;

import view.GameView;

import static org.junit.Assert.assertEquals;

/**
 * Class to test the model of the game.
 */
public abstract class AbstractModelTests {
  protected ThreeTrioGame game;
  protected List<ThreeTrioCard> deck;
  protected ThreeTrioCard[][] board;

  @Test
  public void testCountPossibleFlips() {
    ThreeTrioGame game = new ThreeTrioGame("board2.config", "deck.config");
    GameView view = new GameView(game);
    try {
      game.startGame(game.createDeck(), game.createBoard());
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    game.playToBoard(1, 1, 0);
    game.playToBoard(0, 1, 0);

    System.out.println("Card we are going to play " + game.getPlayerOneHand().get(0).toString());
    Assert.assertEquals(2, game.countPossibleFlips(0, 2, game.getPlayerOneHand().get(0)));
  }

  @Test
  public void quickStart() {
    game.playToBoard(0, 0, 0);
    game.playToBoard(0, 1, 0);
    // Continue playing the game however you like
    Assert.assertFalse(game.isGameOver());
  }


  /**
   * Testing that start game throws IAE if deck is null.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testStartGameNullDeck() {
    game.startGame(null, this.board);
  }

  /**
   * Testing that start game throws IAE if board is null.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testStartGameNullBoard() {
    game.startGame(this.deck, null);
  }

  /**
   * Testing that start game throws IAE if deck is too short.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testStartGameSmallDeck() {
    game.startGame(this.deck.subList(0, 1), this.board);
  }

  /**
   * Testing that start game throws IAE if board is invalid.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testStartGameInvalidBoard() {
    ThreeTrioCard[][] board = new ThreeTrioCard[1][2];
    board[0][0] = new PlayingCard();
    board[0][1] = new PlayingCard();
    game.startGame(this.deck, board);
  }

  /**
   * Testing that start game throws ISE if game is already started.
   */
  @Test(expected = IllegalStateException.class)
  public void testStartGameAlreadyStarted() {
    game.startGame(this.deck, this.board);
    game.startGame(this.deck, this.board);
  }

  /**
   * Testing that game throws ISE if trying to deal cards after game has started.
   */
  @Test(expected = IllegalStateException.class)
  public void testDealCardsAlreadyStarted() {
    game.startGame(this.deck, this.board);
    game.dealCards();
  }

  /**
   * Testing that game throws IAE if playing to invalid index.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testPlayToBoardOutOfBounds() {
    //game.startGame(this.deck, this.board);
    game.playToBoard(-10, 100, 0);
  }



  /**
   * Testing that game throws ISE if it checks for winner before game has started.
   */
  @Test(expected = IllegalStateException.class)
  public void testGetWinnerGameNotOver() {
    game.startGame(this.deck, this.board);
    game.getWinner();
  }

  /**
   * Testing that game deals cards evenly.
   */
  @Test
  public void testEvenDeal() {
    //game.dealCards();
    assertEquals(game.getPlayerOneHand().size(), game.getPlayerTwoHand().size());
  }

  /**
   * Testing that model throws IAE if playing to a hole.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testPlayToHole() {

    game.playToBoard(1, 1, 0);
  }

  /**
   * Testing that model throws IAE if playing invalid hand index.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testPlayOutOfRangeHand() {
    game.playToBoard(0, 0, 1000);
  }

  /**
   * Testing that model throws IAE if playing invalid board index.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testPlayOutOfRangeBoard() {
    game.playToBoard(1000, 0, 0);
  }

  /**
   * Test get turn works as expected.
   */
  @Test
  public void testGetTurn() {

    Assert.assertEquals(game.getTurn(), true);
  }

  /**
   * Tests deck is created properly.
   */
  @Test
  public void testCreateDeck() {
    try {
      Assert.assertEquals(game.createDeck().size(), 20);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
  }

  /**
   * Tests player hand is created properly.
   */
  @Test
  public void testGetPlayerHand() {

    Assert.assertEquals(game.getPlayerHand().size(), (game.getNumTiles() + 1) / 2);
  }

  /**
   * Tests getBoard works as expected.
   */
  @Test
  public void testGetBoard() {
    try {
      game.startGame(game.createDeck(), game.createBoard());
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    Assert.assertEquals(game.getBoard().length, 5);
    Assert.assertEquals(game.getBoard()[0].length, 7);
  }

  /**
   * Tests board is created properly.
   */
  @Test
  public void testCreateBoard() {
    try {
      Assert.assertEquals(game.createBoard().length, 5);
      Assert.assertEquals(game.createBoard()[0].length, 7);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
  }

  /**
   * Tests getNumTiles works as expected.
   */
  @Test
  public void testGetNumTiles() {

    Assert.assertEquals(game.getNumTiles(), 15);
  }

  /**
   * Testing getBoard() returns a copy of the board.
   */
  @Test
  public void testImmutableGetBoard() {

    ThreeTrioCard[][] board = game.getBoard();
    board[0][0] = new PlayingCard("James", Attack.ONE, Attack.TWO, Attack.THREE, Attack.FOUR);
    Assert.assertNotEquals(game.getBoard()[0][0], board[0][0]);
  }

  @Test
  public void testGetCard() {

    Assert.assertEquals(game.getCard(0, 0).getName(), game.getBoard()[0][0].getName());
  }

  @Test
  public void testGetCardIsImmutable() {
    game.playToBoard(0, 0, 0);
    ThreeTrioCard card = game.getCard(0, 0);
    card.setColor(Color.BLUE);
    Assert.assertNotEquals(game.getCard(0, 0).getColor(), Color.BLUE);
  }

  @Test
  public void testGetCardColor() {
    game.playToBoard(0, 0, 0);
    Assert.assertEquals(game.getCardColor(0, 0), Color.RED);
  }

  @Test
  public void testIsValidMove() {
    // Asserts true because the card is played to a playable spot
    Assert.assertTrue(game.isValidMove(0, 0));
    game.playToBoard(0, 0, 0);
    // Asserts false because there is already a card where this card is trying to be played
    Assert.assertFalse(game.isValidMove(0, 0));
    // Asserts false because we are trying to play to a hole
    Assert.assertFalse(game.isValidMove(1, 1));
    // Asserts false because we are trying to play to a spot that is out of bounds
    Assert.assertFalse(game.isValidMove(1000, 1000));
  }

}
