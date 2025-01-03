import java.io.FileNotFoundException;

import controller.SimpleController;
import model.FallenModel;
import model.PlusModel;
import model.ReadonlyThreeTrioModel;
import model.ReverseModel;
import model.SameModel;
import model.ThreeTrioCard;
import model.ThreeTrioGame;
import model.ThreeTrioModel;
import player.Computer;
import player.Human;
import player.Player;
import playerstrategy.CornerStrategy;
import playerstrategy.FlipMaxCardsStrategy;
import playerstrategy.LeastLikelyToFlipStrategy;
import playerstrategy.PlayerStrategy;
import view.GuiGameView;
import view.ThreeTrioGuiView;

/**
 * Main class adopted from Red7 to run the game with the GUI.
 * Makes testing so much easier.
 */
public class TTGame {
  private static Player createPlayer(String arg, String playerName, ThreeTrioModel model) {
    if (arg.equalsIgnoreCase("human")) {
      return new Human(playerName);
    } else if (arg.startsWith("computer:")) {
      String strategyName = arg.split(":")[1];
      return new Computer(playerName, getStrategy(strategyName, model));
    } else {
      throw new IllegalArgumentException("Invalid player type: " + arg);
    }
  }

  private static PlayerStrategy getStrategy(String strategyName, ThreeTrioModel model) {
    switch (strategyName.toLowerCase()) {
      case "flipmaxcards":
        return new FlipMaxCardsStrategy(model);
      case "corner":
        return new CornerStrategy(model);
      case "leastlikely":
        return new LeastLikelyToFlipStrategy(model);
      default:
        throw new IllegalArgumentException("Unknown strategy: " + strategyName);
    }
  }

  private static ThreeTrioModel<ThreeTrioCard> createModel(String arg, String boardConfig,
                                                           String deckConfig) {
    ThreeTrioModel<ThreeTrioCard> baseModel = new ThreeTrioGame(boardConfig, deckConfig);
    try {
      baseModel.startGame(baseModel.createDeck(), baseModel.createBoard());
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    if (arg.equalsIgnoreCase("reverse")) {
      System.out.println("Reverse");
      return new ReverseModel(baseModel);
    } else if (arg.equalsIgnoreCase("fallen")) {
      return new FallenModel(baseModel);
    } else if (arg.equalsIgnoreCase("reverseFallen")) {
      return new FallenModel(new ReverseModel(baseModel));
    } else if (arg.equalsIgnoreCase("normal")) {
      return baseModel;
    } else {
      throw new IllegalArgumentException("Invalid game mode: " + arg);
    }
  }

  private static ThreeTrioModel<ThreeTrioCard> addRule(String arg,
                                                       ThreeTrioModel<ThreeTrioCard> baseModel) {
    if (arg.equalsIgnoreCase("same")) {
      return new SameModel(baseModel);
    } else if (arg.equalsIgnoreCase("plus")) {
      return new PlusModel(baseModel);
    } else {
      return baseModel;
    }
  }

  /**
   * Main method to run the game.
   *
   * @param args command line arguments
   * @throws FileNotFoundException if the file is not found
   */
  public static void main(String[] args) throws FileNotFoundException {
    if (args.length < 4 || args.length > 5) {
      System.out.println("Usage: java TTGame <Player1Type> <Player2Type>" +
              " <P1Hints>:<P2Hints> <GameMode> <Modifier>");
      System.out.println("<PlayerType> can be 'human' or 'computer:<strategy>'");
      System.out.println("<PlayerType> can be 'human' or 'computer:<strategy>'");
      System.out.println("<PHints> == 'easy' for hints");
      System.out.println("<GameMode> can be 'reverse', 'fallen', or 'normal'");
      System.out.println("<Modifier> can be 'same', 'plus', or 'normal'");
      return;
    }

    ThreeTrioModel<ThreeTrioCard> ttGame = addRule(args[4], createModel(args[3],
            "./board2.config", "./deck2.config"));

    Player player1 = createPlayer(args[0], "Player 1", ttGame);
    Player player2 = createPlayer(args[1], "Player 2", ttGame);


    ttGame.setPlayers(player1, player2);

    ThreeTrioGuiView view = new GuiGameView((ReadonlyThreeTrioModel) ttGame, player1,
            args[2].split(":")[0].equalsIgnoreCase("easy"));
    ThreeTrioGuiView view2 = new GuiGameView((ReadonlyThreeTrioModel) ttGame, player2,
            args[2].split(":")[1].equalsIgnoreCase("easy"));

    //ttGame.startGame(ttGame.createDeck(), ttGame.createBoard());
    //ModelAdapter modelAdapter = new ModelAdapter(ttGame, "./board2.config");
    //modelAdapter.cardSetup();
    //ThreeTrioGuiView view2 = new ViewAdapter(modelAdapter, player2);


    SimpleController controller = new SimpleController((ThreeTrioGame) ttGame, view, player1);
    SimpleController controller2 = new SimpleController((ThreeTrioGame) ttGame, view2, player2);

    // must register the controller a listener to the view
    view.addControllerFeaturesListener(controller);
    view2.addControllerFeaturesListener(controller2);

    // must register the controller as a listener to the model
    ((ThreeTrioGame) ttGame).addControllerListener(controller);
    ((ThreeTrioGame) ttGame).addControllerListener(controller2);


    // once again if you want to play against a machine you must uncomment this line of code and
    // register the machine controller to the view
    // view2.addControllerFeaturesListener(controller2);

    Thread thread1 = new Thread(() -> controller.start());
    Thread thread2 = new Thread(() -> controller2.start());

    thread1.start();
    thread2.start();
  }

  private ThreeTrioGuiView createView(ReadonlyThreeTrioModel model, String arg, Player player) {
    return new GuiGameView(model, player, arg.equalsIgnoreCase("easy"));
  }
}