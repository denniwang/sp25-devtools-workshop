package controller;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.util.List;

import model.MockThreeTrioModel;
import model.ThreeTrioCard;
import model.ThreeTrioGame;
import model.ThreeTrioModel;
import player.Human;
import player.Player;
import view.MockGuiView;
import view.ThreeTrioGuiView;

/**
 * Tests the controller for the ThreeTrio game behaves as expected. Delegates methods properly.
 * Tested via a mock.
 */
public class ControllerTest {

  private SimpleController controller;
  private StringBuilder modelLog;
  private StringBuilder viewLog;

  @Before
  public void setup() throws FileNotFoundException {
    ThreeTrioGame game = new ThreeTrioGame("board2.config", "deck.config");
    List<ThreeTrioCard> deck = game.createDeck();
    ThreeTrioCard[][] board = game.createBoard();
    game.setBoard(board);
    game.setDeck(deck);
    game.setPlayers(new Human("John"), new Human("Jane"));
    Player player = new Human("John");
    board = game.createBoard();
    modelLog = new StringBuilder();
    viewLog = new StringBuilder();
    ThreeTrioGuiView mockView = new MockGuiView(viewLog, game, player);
    ThreeTrioModel mockGame = new MockThreeTrioModel(modelLog, board);
    controller = new SimpleController(mockGame, mockView, player);
  }

  @Test
  public void testNotifyPlay() {
    controller.notifyPlay();
    System.out.println(viewLog.toString());
    Assert.assertTrue(this.modelLog.toString().contains("Getting current player."));

    // Test view properly displays who's turn it is after a play
    Assert.assertTrue(this.viewLog.toString().contains("John's turn"));
    Assert.assertTrue(this.viewLog.toString().contains("Refreshed view"));
  }

  // Testing the playing to board via controller calls the correct function
  // and logs the correct message
  @Test
  public void testPlayingToBoard() {
    controller.playToBoard(0, 0, 0);
    Assert.assertTrue(this.modelLog.toString()
            .contains("Playing to board! Row: 0 Col: 0 HandIdx: 0"));
  }

  // Test for ThreeTrioControllerFeatures.notifyWinner()
  @Test
  public void testNotifyWinner() {
    controller.notifyWinner();
    Assert.assertTrue(this.modelLog.toString().contains("Checking if game is over."));
    Assert.assertTrue(this.modelLog.toString().contains("Getting score."));
    Assert.assertTrue(this.modelLog.toString().contains("Getting winner."));
    System.out.println(viewLog.toString());
    // Asserts the view displays the game is sucessfully over and the winner
    Assert.assertTrue(this.viewLog.toString().contains("Game Over! Red wins with a score of 0"));
  }

  // Test for ThreeTrioControllerFeatures.selectCard(int cardIdx, boolean isPlayerOne)
  @Test
  public void testSelectCard() {
    controller.selectCard(0, true);
    Assert.assertTrue(this.modelLog.toString().contains("Getting current player."));
    Assert.assertTrue(this.modelLog.toString().contains("Getting turn."));
    Assert.assertTrue(this.viewLog.toString().contains("Player 1 selected card 0"));
    Assert.assertTrue(this.viewLog.toString().contains("Refreshed view"));
  }

  // Test for ThreeTrioControllerFeatures.getUsername()
  @Test
  public void testGetUsername() {
    String username = controller.getUsername();
    Assert.assertEquals("John", username);
  }

  @Test
  public void testClickBoardWithNoSelectedCard() {
    // invalid hand index to throw the exception
    controller.playToBoard(0, 0, -1);
    Assert.assertTrue(this.viewLog.toString().contains("Please select a card from the hand first"));
  }


}
