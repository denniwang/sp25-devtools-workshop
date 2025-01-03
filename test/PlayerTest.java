import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.util.List;

import model.Color;
import playerstrategy.Move;
import model.ThreeTrioCard;
import model.ThreeTrioGame;
import player.Computer;
import player.Human;
import player.Player;
import playerstrategy.FlipMaxCardsStrategy;

/**
 * Tests basic functionality of Player interface for Humans and Computers.
 */
public class PlayerTest {
  private ThreeTrioGame model;
  private Player humanPlayer;
  private Player machinePlayer;

  @Before
  public void setup() throws FileNotFoundException {
    model = new ThreeTrioGame("board2.config", "deck.config");
    List<ThreeTrioCard> deck = model.createDeck();
    ThreeTrioCard[][] board = model.createBoard();
    model.setBoard(board);
    model.setDeck(deck);
    humanPlayer = new Human("John");
    machinePlayer = new Computer("Jane", new FlipMaxCardsStrategy(model));
  }

  @Test
  public void testGetNameForHuman() {
    Assert.assertEquals(humanPlayer.getName(), "John");
  }

  @Test
  public void testGetNameForMachine() {
    Assert.assertEquals(machinePlayer.getName(), "Jane");
  }

  // Test showing that if getMove is ever called on a human null will be returned
  @Test
  public void testHumansDontGetMove() {
    Assert.assertEquals(humanPlayer.getMove(), null);
  }

  // Tests that a machine will return a valid move in accordance to its provided strategy
  @Test
  public void testMachineMove() {
    try {
      model.startGame(model.createDeck(), model.createBoard());
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    Move move = machinePlayer.getMove();
    Assert.assertEquals(move.getRow(), 0);
    Assert.assertEquals(move.getCol(), 0);
    ThreeTrioCard card = move.getCard();
    Assert.assertEquals(card.getColor(), Color.RED);
  }
}
