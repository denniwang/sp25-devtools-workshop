import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import playerstrategy.CornerStrategy;
import playerstrategy.FlipMaxCardsStrategy;
import playerstrategy.Move;
import playerstrategy.PlayerStrategy;
import playerstrategy.LeastLikelyToFlipStrategy;
import playerstrategy.ComplexPlayerStrategy;
import playerstrategy.MinimaxStrategy;
import playerstrategy.StrategyType;
import model.Attack;
import model.ReadonlyMockThreeTrioModel;
import playerstrategy.StrategyMove;
import model.PlayingCard;
import model.ThreeTrioCard;
import model.ThreeTrioGame;
import view.GameView;

/**
 * Tests for the strategies used by the AI player in Three Trio using a mock model.
 */
public class StrategyTest {
  private ThreeTrioGame game;
  private List<ThreeTrioCard> deck;
  private ThreeTrioCard[][] board;
  private GameView view;
  private ReadonlyMockThreeTrioModel mockGame;
  private ReadonlyMockThreeTrioModel customMockGame;
  private FileWriter fw;

  @Before
  public void setup() throws FileNotFoundException {
    game = new ThreeTrioGame("board2.config", "deck.config");
    this.deck = game.createDeck();
    this.board = game.createBoard();
    game.setBoard(this.board);
    game.setDeck(this.deck);
    view = new GameView(game);
    PlayingCard placeholder = new PlayingCard("placeholder",
            Attack.A, Attack.A, Attack.A, Attack.A);
    try {
      fw = new FileWriter("strategy-transcript.txt");
      mockGame = new ReadonlyMockThreeTrioModel(fw, this.board);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Sets up a custom configuration for the game. Used for setting up mock with different board and
   * deck.
   *
   * @param board the board configuration
   * @param deck  the deck configuration
   * @throws FileNotFoundException if the file is not found
   */
  public void setupCustomConfig(String board, String deck) throws FileNotFoundException {
    ThreeTrioGame customGame = new ThreeTrioGame(board, deck);
    this.deck = customGame.createDeck();
    this.board = customGame.createBoard();
    customGame.setBoard(this.board);
    customGame.setDeck(this.deck);
    view = new GameView(customGame);
    try {
      fw = new FileWriter("log.txt");
      customMockGame = new ReadonlyMockThreeTrioModel(fw, this.board);
    } catch (IOException e) {
      e.printStackTrace();
    }

  }


  @After
  public void tearDown() {
    try {
      if (fw != null) {
        fw.flush();
        fw.close();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testFlipMaxCardsStrategyTieBreaker() {
    // Place a card at (0,0) so that the next best move is calculated
    mockGame.mockPlaceCard(new StrategyMove(0, 0, new PlayingCard(
            "John", Attack.ONE, Attack.TWO, Attack.THREE, Attack.FOUR)));

    // Set flip counts to create a tie scenario
    mockGame.setFlipCount(0, 1, new PlayingCard(
            "Jerome", Attack.ONE, Attack.TWO, Attack.THREE, Attack.FOUR), 2);
    mockGame.setFlipCount(1, 1, new PlayingCard(
            "David", Attack.THREE, Attack.TWO, Attack.ONE, Attack.FOUR), 2);

    // Create strategy and get the best move
    FlipMaxCardsStrategy strategy = new FlipMaxCardsStrategy(mockGame);
    Move move = strategy.getMove();

    // Expect the move to (0,1), the uppermost-leftmost of tied moves
    PlayingCard cardToPlay = new PlayingCard(
            "Jerome", Attack.ONE, Attack.TWO, Attack.THREE, Attack.FOUR);
    Assert.assertEquals(new StrategyMove(0, 1, cardToPlay), move);
  }

  @Test
  public void testFlipMaxCardsStrategyNoBestMove() {
    mockGame.mockPlaceCard(new StrategyMove(0, 2, new PlayingCard(
            "Charlie", Attack.NINE, Attack.EIGHT, Attack.SEVEN, Attack.SIX)));

    // No need to set a flip count because there is NO spot to play where any cards can be flipped
    FlipMaxCardsStrategy strategy = new FlipMaxCardsStrategy(mockGame);
    Move move = strategy.getMove();

    Assert.assertEquals(move, new StrategyMove(0, 0, new PlayingCard(
            "Jerome", Attack.ONE, Attack.TWO, Attack.THREE, Attack.FOUR)));
  }

  @Test
  public void testFlipMaxCardsStrategyEasyMove() {
    FlipMaxCardsStrategy strategy = new FlipMaxCardsStrategy(mockGame);
    Move move = strategy.getMove();
    Assert.assertEquals(move, new StrategyMove(0, 0, new PlayingCard("Jerome", Attack.ONE,
            Attack.TWO, Attack.THREE, Attack.FOUR)));
  }

  @Test
  public void testFlipMaxCardsStrategyClearBestMove() {
    mockGame.mockPlaceCard(new StrategyMove(2, 2,
            new PlayingCard("Charlie", Attack.NINE, Attack.EIGHT, Attack.SEVEN, Attack.SIX)));
    // Set the flip count for the best move --> this is the only move that can flip any card
    mockGame.setFlipCount(2, 1,
            new PlayingCard("Hank", Attack.EIGHT, Attack.SEVEN, Attack.SIX, Attack.FIVE),
            1);
    FlipMaxCardsStrategy strategy = new FlipMaxCardsStrategy(mockGame);
    Move move = strategy.getMove();

    Assert.assertEquals(move, new StrategyMove(2, 1,
            new PlayingCard("Hank", Attack.EIGHT, Attack.SEVEN, Attack.SIX, Attack.FIVE)));
  }

  /**
   * Testing the corner strategy able to correctly break ties when there is a tie between moves.
   */
  @Test
  public void testCornerStrategyTieBreaker() {
    CornerStrategy strategy = new CornerStrategy(mockGame);
    Move move = strategy.getMove();
    System.out.println(move.toString());
    Assert.assertEquals(move.toString(), "Row: 0 Col: 0 Card: Frank 7 8 9 1");
    //Assert.assertEquals(move, new Move(0, 0, new PlayingCard("Charlie", Attack.NINE, Attack.EIGHT,
    // Attack.SEVEN, Attack.SIX)));
  }

  /**
   * Testing the corner strategy works when there is a clear best move.
   */
  @Test
  public void testCornerStrategyClearBestMove() {
    // Play the the top left corner already so there is only one clear option according to
    // this strategy
    mockGame.mockPlaceCard(new StrategyMove(0, 0,
            new PlayingCard("Charlie", Attack.NINE, Attack.EIGHT, Attack.SEVEN, Attack.SIX)));

    CornerStrategy strategy = new CornerStrategy(mockGame);
    Move move = strategy.getMove();
    Assert.assertEquals(move, new StrategyMove(2, 0,
            new PlayingCard("Frank", Attack.SEVEN, Attack.EIGHT, Attack.NINE, Attack.ONE)));
  }

  /**
   * Testing the corner strategy does not play to holes and can work around holes.
   */
  @Test
  public void testCornerStrategyConsidersHoles() {
    try {
      setupCustomConfig("board.config", "deck.config");
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    CornerStrategy strategy = new CornerStrategy(customMockGame);
    Move move = strategy.getMove();
    Assert.assertEquals(move, new StrategyMove(4, 0, new PlayingCard(
            "Charlie", Attack.NINE, Attack.EIGHT, Attack.SEVEN, Attack.SIX)));
  }


  @Test
  public void testCornerStrategyNoBestMove() {
    // Play cards to the board so all the corners are taken and there is no best move
    // according the strategy
    mockGame.mockPlaceCard(new StrategyMove(0, 0,
            new PlayingCard("Random", Attack.ONE, Attack.TWO, Attack.THREE, Attack.FOUR)));
    mockGame.mockPlaceCard(new StrategyMove(0, 2,
            new PlayingCard("Random", Attack.ONE, Attack.TWO, Attack.THREE, Attack.FOUR)));
    mockGame.mockPlaceCard(new StrategyMove(2, 0,
            new PlayingCard("Random", Attack.FIVE, Attack.SIX, Attack.SEVEN, Attack.EIGHT)));
    mockGame.mockPlaceCard(new StrategyMove(2, 2,
            new PlayingCard("Random", Attack.TWO, Attack.THREE, Attack.FOUR, Attack.FIVE)));

    CornerStrategy strategy = new CornerStrategy(mockGame);
    Move move = strategy.getMove();
    Assert.assertEquals(new StrategyMove(0, 1, new PlayingCard(
            "Jerome", Attack.ONE, Attack.TWO, Attack.THREE, Attack.FOUR)), move);
  }

  /**
   * Testing the least likely to flip strategy works.
   */
  @Test
  public void testLeastLikelyToFlip() {
    // Play cards to the board so all the corners are taken and there is no best
    // move according the strategy
    mockGame.mockPlaceCard(new StrategyMove(0, 0, new PlayingCard("Random", Attack.ONE,
            Attack.TWO, Attack.THREE, Attack.FOUR)));
    PlayerStrategy strategy = new LeastLikelyToFlipStrategy(mockGame);
    Move move = strategy.getMove();
    Assert.assertEquals(new StrategyMove(0, 1, new PlayingCard("Charlie", Attack.NINE,
            Attack.EIGHT, Attack.SEVEN, Attack.SIX)), move);
  }

  /**
   * Testing the least likely to flip strategy when there is a tie between moves.
   */
  @Test
  public void testLeastLikelyToFlipTieBreaker() {
    // Play cards to the board so all the corners are taken and there is no best
    // move according the strategy
    mockGame.mockPlaceCard(new StrategyMove(0, 0, new PlayingCard("Random", Attack.ONE,
            Attack.TWO, Attack.THREE, Attack.FOUR)));
    mockGame.mockPlaceCard(new StrategyMove(0, 1, new PlayingCard("Charlie", Attack.SEVEN,
            Attack.EIGHT, Attack.NINE, Attack.ONE)));
    mockGame.mockPlaceCard(new StrategyMove(0, 2, new PlayingCard("Barly", Attack.NINE,
            Attack.EIGHT, Attack.SEVEN, Attack.SIX)));
    PlayerStrategy strategy = new LeastLikelyToFlipStrategy(mockGame);

    Move move = strategy.getMove();
    Assert.assertEquals(new StrategyMove(2, 0, new PlayingCard("Charlie", Attack.NINE,
            Attack.EIGHT, Attack.SEVEN, Attack.SIX)), move);
  }

  /**
   * Testing the least likely to flip strategy when there is a tie between moves.
   */
  @Test
  public void testMinMaxCorner() {
    // Play cards to the board so all the corners are taken and there is no best move according
    // the strategy
    try {
      game.startGame(game.createDeck(), game.createBoard());
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    Map<StrategyType, Integer> weights = new HashMap<>();
    weights.put(StrategyType.DEFENSE, 10);
    weights.put(StrategyType.CORNER, 10);
    weights.put(StrategyType.FLIPMAX, 10);
    PlayerStrategy strategy = new MinimaxStrategy(game, weights);

    Move move = strategy.getMove();
    game.playToBoard(move.getRow(), move.getCol(), game.getPlayerHand().indexOf(move.getCard()));
    Assert.assertEquals(move.toString(), "Row: 0 Col: 0 Card: John 1 2 3 4");
  }


  /**
   * Testing the least likely to flip strategy when there is a tie between moves.
   */
  @Test
  public void testFlipMaxCardsStrategyWithRealModel() {
    try {
      game.startGame(game.createDeck(), game.createBoard());
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    game.playToBoard(0, 0, 0);
    FlipMaxCardsStrategy strategy = new FlipMaxCardsStrategy(game);
    Assert.assertEquals(strategy.getMove().toString(), "Row: 0 Col: 1 Card: Jerome 1 2 3 4");
  }

  /**
   * Testing the least likely to flip strategy when there is a tie between moves.
   */
  @Test
  public void testCornerStrategyWithRealModel() {
    try {
      game.startGame(game.createDeck(), game.createBoard());
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    game.playToBoard(0, 0, 0);
    game.playToBoard(2, 0, 0);
    game.playToBoard(0, 2, 0);
    game.playToBoard(2, 2, 0);
    CornerStrategy move = new CornerStrategy(game);
    Assert.assertEquals(move.getMove().toString(), "Row: 0 Col: 1 Card: Charlie 9 8 7 6");
  }

  // i mean it makes moves..???
  @Test
  public void testCombinedStrategies() {
    try {
      game.startGame(game.createDeck(), game.createBoard());
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }

    PlayerStrategy corner = new CornerStrategy(game);
    PlayerStrategy minMax = new CornerStrategy(game);
    ComplexPlayerStrategy complex = new ComplexPlayerStrategy(corner, minMax);

    GameView view = new GameView(game);
    Move move1 = complex.getMove();
    game.playToBoard(move1.getRow(), move1.getCol(), game.getPlayerHand().indexOf(move1.getCard()));
    Assert.assertEquals(move1.toString(), "Row: 2 Col: 0 Card: Charlie 9 8 7 6");

    Move move2 = complex.getMove();

    game.playToBoard(move2.getRow(), move2.getCol(), game.getPlayerHand().indexOf(move2.getCard()));
    Assert.assertEquals(move2.toString(), "Row: 0 Col: 0 Card: Frank 7 8 9 1");
  }

}
