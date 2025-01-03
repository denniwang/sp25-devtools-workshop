package controller;

import java.io.FileNotFoundException;

import model.Color;
import playerstrategy.Move;
import model.ThreeTrioModel;
import player.Computer;
import player.Player;
import view.ThreeTrioGuiFeatures;
import view.ThreeTrioGuiView;

/**
 * The controller for the ThreeTrio game. Extends features to listen for in the GUI.
 */
public class SimpleController implements ThreeTrioGuiFeatures, ThreeTrioControllerFeatures {
  private final ThreeTrioGuiView view;
  private final ThreeTrioModel model;
  private int selectedCardIdx;
  private final Player player;

  /**
   * Constructs a SimpleController with a given model and view.
   *
   * @param model model to be interacted with and rendered
   * @param view  view to render the model with and listen for user input
   */
  public SimpleController(ThreeTrioModel model, ThreeTrioGuiView view, Player player) {
    this.model = model;
    this.view = view;
    this.view.addFeaturesListener(this);
    int[] selectedBoardLocation = new int[2];
    this.selectedCardIdx = -1;
    this.player = player;
  }


  /**
   * Gets the players username.
   *
   * @return the players username
   */
  public String getUsername() {
    return this.player.getName();
  }

  /**
   * Selects a card from the player's hand in the model.
   * May come in handy if for future gui card selection implementation.
   *
   * @param cardIdx the index of the card to be selected.
   */
  @Override
  public void selectCard(int cardIdx, boolean isPlayerOne) {
    if (model.getActivePlayer().getName().equals(player.getName())) {
      if (isPlayerOne && model.getTurn()) {
        this.selectedCardIdx = cardIdx;
        view.selectP1Card(cardIdx);
      } else if (!isPlayerOne && !model.getTurn()) {
        this.selectedCardIdx = cardIdx;
        view.selectP2Card(cardIdx);
      } else {
        this.view.notify("Not your turn!");
      }
      view.refresh();
    }
  }

  /**
   * Starts the game by creating a deck and board and displaying the view.
   */
  public void start() {
    if (!this.model.hasGameStarted()) {
      try {
        this.model.startGame(model.createDeck(), model.createBoard());
      } catch (FileNotFoundException e) {
        e.printStackTrace();
      }
    }
    this.view.display(true);
  }

  /**
   * Method to handle a machine playing its turn.
   */
  private void playMachineTurn() {
    if (model.isGameOver()) {
      return;
    }
    if (model.getActivePlayer() instanceof Computer) {
      Computer machinePlayer = (Computer) model.getActivePlayer();
      Move move = machinePlayer.getMove();
      int rowToPlay = move.getRow();
      int colToPlay = move.getCol();
      int cardIdxToPlay = -1;
      for (int i = 0; i < this.model.getPlayerHand().size(); i++) {
        if (this.model.getPlayerHand().get(i).equals(move.getCard())) {
          cardIdxToPlay = i;
          break;
        }
      }
      try {
        model.playToBoard(rowToPlay, colToPlay, cardIdxToPlay);
      } catch (IllegalArgumentException e) {
        view.invalidPlay();
      }
      view.refresh();
    }
  }

  /**
   * Plays a card from the player's hand to the board.
   *
   * @param row     the row to play the card to
   * @param col     the column to play the card to
   * @param cardIdx the index of the card to play
   */
  @Override
  public void playToBoard(int row, int col, int cardIdx) {
    if (model.isGameOver()) {
      System.out.println("Game is over");
      return;
    }
    System.out.println("Game is not over");
    if (this.model.getActivePlayer().getName().equals(player.getName())) {
      try {
        this.model.playToBoard(row, col, cardIdx);
      } catch (IllegalArgumentException e) {
        this.view.invalidPlay();
      }
    } else {
      this.view.notify("Not your turn!");
    }
    this.view.refresh();
  }

  @Override
  public String toString() {
    return this.view.toString();
  }

  /**
   * Notifies the view that it is the player's turn.
   */
  @Override
  public void notifyPlay() {
    this.view.refresh();
    if (model.getActivePlayer() instanceof Computer) {
      this.playMachineTurn();
    } else if (model.getActivePlayer().getName().equals(player.getName())) {
      this.view.notify(this.model.getActivePlayer().getName() + "'s turn");
    }
    //this.view.notify("Your turn!");
  }

  /**
   * Refreshes the view to display the current state of the game.
   */
  @Override
  public void refresh() {
    this.view.refresh();
  }


  /**
   * Notifies the view that the game is over and displays the winner and score.
   */
  @Override
  public void notifyWinner() {
    if (model.isGameOver()) {
      Color winner = model.getWinner();
      int score = model.getScore(winner);
      String winnerName = "";
      if (winner == Color.RED) {
        winnerName = "Red";
      } else {
        winnerName = "Blue";
      }
      System.out.println("THIS IS BEING HIT CAUSE THE GAME IS OVERRR");
      this.view.notify("Game Over! " + winnerName + " wins with a score of " + score);
    }
  }
}
