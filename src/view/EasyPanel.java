package view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

import model.ReadonlyThreeTrioModel;
import model.ThreeTrioCard;
import playerstrategy.FlipMaxCardsStrategy;
import playerstrategy.PlayerStrategy;
import playerstrategy.StrategyMove;

/**
 * Panel for the easy level of the game.
 */
public class EasyPanel extends ThreeTrioPanel {

  private PlayerStrategy strat;

  /**
   * Constructs a panel, taking in a model to render.
   *
   * @param model model to be rendered
   */
  public EasyPanel(ReadonlyThreeTrioModel model) {
    super(model);
    strat = new FlipMaxCardsStrategy(model);
  }

  /**
   * Sets the selected card index.
   *
   * @param cardIdx index of the card in the player's hand
   */
  public void setSelectedCardIdx(int cardIdx) {
    this.selectedCardLocation = cardIdx;
    //update board with number of cards that can be flipped with selected card
    this.repaint();
  }

  @Override
  protected void drawBoard(Graphics2D g2d, ThreeTrioCard[][] board) {
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
          } else if (card.getColor() == null) {
            //System.out.println("hint here");
            if (selectedCardLocation != -1) {
              int numFlips = strat.getScore(new StrategyMove(row, col,
                      (ThreeTrioCard) model.getPlayerHand().get(selectedCardLocation)));
              g2d.drawString(Integer.toString(numFlips), x + 5, y + tileHeight - 15);
            }
          }

        }
      }
    }
  }

}
