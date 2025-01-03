package view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Font;
import java.awt.BasicStroke;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.event.MouseInputAdapter;

import controller.ThreeTrioControllerFeatures;
import model.Direction;
import model.ReadonlyThreeTrioModel;
import model.ThreeTrioCard;


/**
 * Represents the GUI view of the game, represents a model of a ThreeTrio game in a JPanel.
 */
public class ThreeTrioPanel extends JPanel {
  protected final ReadonlyThreeTrioModel model;
  protected final List<ThreeTrioGuiFeatures> featureListeners;
  protected final Color blueCard = new Color(100, 170, 255);
  protected final Color redCard = new Color(255, 175, 175);
  protected final Color yellowTile = new Color(210, 200, 30);
  protected final Color grayHole = new Color(192, 192, 192);
  private final int[] selectedBoardLocation = new int[]{-1, -1};
  protected int cardWidth;
  protected int cardHeight;
  protected int tileHeight;
  protected int selectedCardLocation = -1;
  private List<ThreeTrioControllerFeatures> controllerFeaturesListener;
  //private ThreeTrioCard selectedCard = null;
  private boolean mouseIsDown;

  /**
   * Constructs a panel, taking in a model to render.
   *
   * @param model model to be rendered
   */
  public ThreeTrioPanel(ReadonlyThreeTrioModel model) {
    this.model = model;
    this.featureListeners = new ArrayList<>();
    this.controllerFeaturesListener = new ArrayList<>();
    MouseEventsListener listener = new MouseEventsListener();
    this.addMouseListener(listener);
    this.addMouseMotionListener(listener);
    boolean playerOneTurn = true;
  }


  /**
   * Adds a listener to the panel to listen for features.
   *
   * @param listener the features to listen for
   */
  public void addFeaturesListener(ThreeTrioGuiFeatures listener) {
    this.featureListeners.add(listener);
  }

  /**
   * Adds a listener to the panel to listen for controller features.
   *
   * @param listener a controller listener to listen for features
   */
  public void addControllerFeaturesListener(ThreeTrioControllerFeatures listener) {
    this.controllerFeaturesListener.add(listener);
  }


  /**
   * Paints the panel with the given graphics object.
   * Converts it to graphics2d and draws the board and hands based on panel size.
   *
   * @param g the <code>Graphics</code> object to protect
   */
  @Override
  public void paintComponent(Graphics g) {
    Font font = new Font("Verdana", Font.BOLD, 30);
    g.setFont(font);
    super.paintComponent(g);
    Graphics2D g2d = (Graphics2D) g;
    ThreeTrioCard[][] board = model.getBoard();
    g2d.setStroke(new BasicStroke(3));

    // Get the current width and height of the panel
    int panelWidth = getWidth();
    int panelHeight = getHeight();

    // Get player hands
    List<ThreeTrioCard> playerOneHand = model.getPlayerOneHand();
    List<ThreeTrioCard> playerTwoHand = model.getPlayerTwoHand();

    // Calculate the maximum number of cards in any player's hand
    int maxHandSize = Math.max(playerOneHand.size(), playerTwoHand.size());

    // Calculate the card width and height based on the panel's dimensions
    cardWidth = (panelWidth / (model.getBoardW() + 2)) - 1; // +2 for extra columns
    tileHeight = panelHeight / model.getBoardH();
    cardHeight = panelHeight / Math.max(board.length, maxHandSize);

    // Draw player one's hand
    drawPlayerHand(g2d, playerOneHand, 0, redCard, model.getTurn());

    // Draw the board
    drawBoard(g2d, board);

    // Draw player two's hand
    drawPlayerHand(g2d, playerTwoHand, (model.getBoardW() + 1) *
            cardWidth + 10, blueCard, !model.getTurn());
  }

  private void drawPlayerHand(Graphics2D g2d, List<ThreeTrioCard> hand, int x,
                              Color color, boolean isTurn) {
    g2d.setColor(color);
    for (int cardIdx = 0; cardIdx < hand.size(); cardIdx++) {
      ThreeTrioCard card = hand.get(cardIdx);
      int y = cardIdx * cardHeight;
      g2d.fillRect(x, y, cardWidth, cardHeight);
      if (isTurn && cardIdx == selectedCardLocation) {
        g2d.setColor(Color.GREEN); // Set border color to green
        g2d.drawRect(x + 2, y + 2, cardWidth - 4, cardHeight - 4);
      } else {
        g2d.setColor(Color.DARK_GRAY); // Set border color to gray
      }
      g2d.drawRect(x, y, cardWidth, cardHeight);
      g2d.setColor(Color.BLACK); // Reset color for text
      drawCardText(g2d, card, x, y);
      g2d.setColor(color); // Reset color for text
    }
  }

  /**
   * Draws the board based on the given board.
   *
   * @param g2d   the graphics object to draw on
   * @param board the board to draw
   */
  protected void drawBoard(Graphics2D g2d, ThreeTrioCard[][] board) {
    //System.out.println(board[0][0].getColor());
    for (int row = 0; row < board.length; row++) {
      for (int col = 0; col < board[row].length; col++) {
        ThreeTrioCard card = board[row][col];
        if (card != null) {
          int x = (col + 1) * cardWidth + 5; // +1 to leave space for the left hand
          int y = row * tileHeight;
          g2d.setStroke(new BasicStroke(2));
          if (card.isHole()) {
            g2d.setColor(grayHole);
          } else if (card.getColor() == null) {
            g2d.setColor(yellowTile);
          } else if (card.getColor().toString().equals("R")) {
            g2d.setColor(redCard);
          } else if (card.getColor().toString().equals("B")) {
            g2d.setColor(blueCard);
          }
          g2d.fillRect(x, y, cardWidth, tileHeight);
          g2d.setColor(Color.GRAY); // Set border color to gray
          g2d.drawRect(x, y, cardWidth, tileHeight);
          g2d.setColor(Color.BLACK); // Reset color for text
          if (!card.isHole() && card.getColor() != null) {
            drawCardText(g2d, card, x, y);
          }
        }
      }
    }
  }

  /**
   * Draws the text on the card.
   *
   * @param g2d  the graphics object to draw on
   * @param card the card to draw text on
   * @param x    the x coordinate of the card
   * @param y    the y coordinate of the card
   */
  protected void drawCardText(Graphics2D g2d, ThreeTrioCard card, int x, int y) {
    int centerX = x + cardWidth / 3;
    int centerY = y + (cardHeight / 2) + 15;
    for (Direction dir : Direction.values()) {
      int dx = 0;
      int dy = 0;
      if (dir == Direction.NORTH) {
        dy = -cardHeight / 4;
      }
      if (dir == Direction.SOUTH) {
        dy = cardHeight / 4;
      }
      if (dir == Direction.EAST) {
        dx = cardWidth / 4;
      }
      if (dir == Direction.WEST) {
        dx = -cardWidth / 4;
      }
      if (card.getAttacks().get(dir).getValue() == 10) {
        g2d.drawString("A", centerX + dx, centerY + dy);
      } else {
        g2d.drawString(card.getAttacks().get(dir).getValue()
                + "", centerX + dx, centerY + dy);
      }
    }
  }


  /**
   * Handles a click on the player 1's hand.
   * resets the selected board position.
   *
   * @param cardIdx index of the card in the player's hand
   */
  public void playerOneHandClicked(int cardIdx) {
    for (ThreeTrioControllerFeatures features : this.controllerFeaturesListener) {
      features.selectCard(cardIdx, true);
    }
  }

  /**
   * Handles a click on the player 2's hand.
   * resets the selected board position.
   *
   * @param cardIdx index of the card in the player's hand
   */
  public void playerTwoHandClicked(int cardIdx) {
    for (ThreeTrioControllerFeatures features : this.controllerFeaturesListener) {
      features.selectCard(cardIdx, false);
    }
  }

  /**
   * Sets the selected card index.
   *
   * @param cardIdx index of the card in the player's hand
   */
  public void setSelectedCardIdx(int cardIdx) {
    this.selectedCardLocation = cardIdx;
    this.repaint();
  }

  /**
   * Handles a click on the board. resets the selected card position.
   *
   * @param row row of the board clicked
   * @param col column of the board clicked
   */
  public void boardClicked(int row, int col) {
    for (ThreeTrioGuiFeatures features : featureListeners) {
      //System.out.println("Playing to board from jpanel");
      features.playToBoard(row, col, selectedCardLocation);
    }
    selectedCardLocation = -1;
    this.repaint();
  }

  /**
   * Refreshes the panel. Using the Jpanel repaint method.
   */
  public void repaint() {
    super.repaint();
  }

  private void getCardAtLocation(Point2D p) {
    int x = (int) p.getX();
    int y = (int) p.getY();

    // Calculate the dimensions dynamically
    int panelWidth = getWidth();
    int panelHeight = getHeight();
    int cardWidth = (panelWidth / (model.getBoardW() + 2)) - 1; // +2 for extra columns
    int tileHeight = panelHeight / model.getBoardH();
    int cardHeight = panelHeight / Math.max(model.getBoard().length,
            Math.max(model.getPlayerOneHand().size(), model.getPlayerTwoHand().size()));

    int cardRow;
    int col;

    // Check if the click is in Player 1's hand area
    if (x < cardWidth) {
      cardRow = y / cardHeight;
      col = x / cardWidth;
      //System.out.println("Player 1 Hand area - Point: (" + x + ", " + y + ")");
      //System.out.println("Player 1 Hand area - Calculated cell: (" + cardRow + ", " + col + ")");
      if (model.getTurn()) {
        playerOneHandClicked(cardRow);
      }
    }
    // Check if the click is in Player 2's hand area
    else if (x > (model.getBoardW() + 1) * cardWidth) {
      x -= (model.getBoardW() + 1) * cardWidth;
      cardRow = y / cardHeight;
      col = x / cardWidth;
      //System.out.println("Player 2 Hand area - Point: (" + x + ", " + y + ")");
      //System.out.println("Player 2 Hand area - Calculated cell: (" + cardRow + ", " + col + ")");
      if (!model.getTurn()) {
        playerTwoHandClicked(cardRow);
      }
    }
    // Otherwise, the click is on the board
    else {
      x -= cardWidth;
      cardRow = y / tileHeight;
      col = x / cardWidth;
      //System.out.println("Board area - Point: (" + x + ", " + y + ")");
      //System.out.println("Board area - Calculated cell: (" + cardRow + ", " + col + ")");
      if (col >= 0 && col < model.getBoardW()) {
        boardClicked(cardRow, col);
      }
    }
  }

  /**
   * MouseEventsListener to listen to user mouse input to panel.
   */
  private class MouseEventsListener extends MouseInputAdapter {
    @Override
    public void mousePressed(MouseEvent e) {
      ThreeTrioPanel.this.mouseIsDown = true;
      Point point = e.getPoint();
      int x = point.x;
      int y = point.y;
      int cardRow = y / cardHeight;
      int col = x / cardWidth;
      //System.out.println("Mouse pressed at: (" + x + ", " + y + ")");
      //System.out.println("Calculated cell: (" + cardRow + ", " + col + ")");
      getCardAtLocation(point);
      this.mouseDragged(e);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
      //System.out.println(e.getX() + " " + e.getY());
      ThreeTrioPanel.this.mouseIsDown = false;
      //if()
    }

    @Override
    public void mouseDragged(MouseEvent e) {
      // This point is measured in actual physical pixels
      Point physicalP = e.getPoint();
      // For us to figure out which circle it belongs to, we need to transform it
      // into logical coordinates
      //Point2D logicalP = transformPhysicalToLogical().transform(physicalP, null);
      // TODO: Figure out whether this location is inside a card and if so, which one
      ThreeTrioPanel.this.repaint();
    }
  }
}
