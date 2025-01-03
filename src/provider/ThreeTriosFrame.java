package provider;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 This class holds the methods to create the Frame of the game.
 */
public class ThreeTriosFrame extends JFrame implements GameFrame, EventListener {
  private final ReadonlyThreeTriosModel model;
  private final GameBoard gameBoard;
  private final PlayerHandDisplay redHandDisplay;
  private final PlayerHandDisplay blueHandDisplay;
  private Card selectedCard;
  private Card currentlyHighlightedCard;

  /**
   This is the constructor for the Frame.
   */
  public ThreeTriosFrame(ReadonlyThreeTriosModel model) {
    super("Three Trios Game");
    this.model = model;
    this.gameBoard = new ThreeTriosBoard(model);
    this.redHandDisplay = new PlayerHandPanel(PlayerColor.RED, model);
    this.blueHandDisplay = new PlayerHandPanel(PlayerColor.BLUE, model);
    initialize();
  }

  private void setupListeners() {
    gameBoard.setGameEventListener(this);
    redHandDisplay.setGameEventListener(this);
    blueHandDisplay.setGameEventListener(this);
  }

  @Override
  public void setGameEventListener(EventListener listener) {
    // Set listener for all components
    gameBoard.setGameEventListener(listener);
    redHandDisplay.setGameEventListener(listener);
    blueHandDisplay.setGameEventListener(listener);
  }

  @Override
  public void highlightSelectedCard(Card card, PlayerColor owner) {
    // Unhighlight previous card if exists
    if (currentlyHighlightedCard != null) {
      redHandDisplay.highlightCard(currentlyHighlightedCard, false);
      blueHandDisplay.highlightCard(currentlyHighlightedCard, false);
    }

    // Highlight new card
    if (card != null) {
      if (owner == PlayerColor.RED) {
        redHandDisplay.highlightCard(card, true);
      } else {
        blueHandDisplay.highlightCard(card, true);
      }
      currentlyHighlightedCard = card;
    } else {
      currentlyHighlightedCard = null;
    }
  }

  @Override
  public void onCellClicked(int row, int col) {
    if (selectedCard != null) {
      try {
        deselectCard();
        gameBoard.refreshBoard();
        updatePlayerHands();
      } catch (Exception ex) {
        showMessage("Error placing card: " + ex.getMessage());
      }
    }
  }

  @Override
  public void onCardSelected(Card card, PlayerColor owner) {
    if (model.getCurrentPlayer() != owner) {
      return;
    }

    if (selectedCard == card) {
      deselectCard();
    } else {
      if (selectedCard != null) {
        deselectCard();
      }
      selectCard(card, owner);
    }
  }

  @Override
  public void initialize() {
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLayout(new BorderLayout());

    this.setTitle("Current Player: RED");
    add(blueHandDisplay.createHandPanel(), BorderLayout.EAST);
    add(gameBoard.createGridPanel(), BorderLayout.CENTER);
    add(redHandDisplay.createHandPanel(), BorderLayout.WEST);

    setupListeners();
    pack();
    setVisible(true);
  }

  @Override
  public void refresh() {
    gameBoard.refreshBoard();
    updatePlayerHands();
  }

  @Override
  public void showMessage(String message) {
    JOptionPane.showMessageDialog(this,
            message);
  }

  @Override
  public void updatePlayerTurn(PlayerColor currentPlayer) {
    updatePlayerHands();
    this.setTitle("Current Player: " + currentPlayer);
  }

  private void updatePlayerHands() {
    redHandDisplay.updateHand(model.getRedPlayerHand());
    blueHandDisplay.updateHand(model.getBluePlayerHand());
  }

  private void selectCard(Card card, PlayerColor owner) {
    selectedCard = card;
    redHandDisplay.highlightCard(card, owner == PlayerColor.RED);
    blueHandDisplay.highlightCard(card, owner == PlayerColor.BLUE);
  }

  private void deselectCard() {
    if (selectedCard != null) {
      redHandDisplay.highlightCard(selectedCard, false);
      blueHandDisplay.highlightCard(selectedCard, false);
      selectedCard = null;
    }
  }
}