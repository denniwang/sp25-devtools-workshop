package provider;

import java.awt.Dimension;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.BorderFactory;
import javax.swing.SwingConstants;

/**
 * This is the class that has the methods to show a player's hand.
 */
public class PlayerHandPanel implements PlayerHandDisplay {
  private final PlayerColor player;
  private final ReadonlyThreeTriosModel model;
  private JPanel handPanel;
  private EventListener eventListener;

  /**
   * Constructor for this class.
   *
   * @param player the color of the player
   * @param model the readonly model of Three Trios game
   */
  public PlayerHandPanel(PlayerColor player, ReadonlyThreeTriosModel model) {
    this.player = player;
    this.model = model;
  }

  @Override
  public JPanel createHandPanel() {
    handPanel = new JPanel();
    handPanel.setLayout(new BoxLayout(handPanel, BoxLayout.Y_AXIS));
    handPanel.setPreferredSize(new Dimension(100, 100));

    Color backgroundColor = player == PlayerColor.RED
            ? new Color(254, 167, 169)
            : new Color(73, 169, 249);
    handPanel.setBackground(backgroundColor);

    List<Card> hand = player == PlayerColor.RED
            ? model.getRedPlayerHand()
            : model.getBluePlayerHand();

    for (Card card : hand) {
      JPanel cardPanel = createCardPanel(card, player);
      handPanel.add(cardPanel);
    }
    return handPanel;
  }

  private JPanel createCardPanel(Card card, PlayerColor owner) {
    JPanel cardPanel = new JPanel();
    cardPanel.setLayout(new GridLayout(3, 3));
    cardPanel.setPreferredSize(new Dimension(100, 100));
    cardPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

    Color cardBackground = owner == PlayerColor.RED
            ? new Color(254, 167, 169)
            : new Color(73, 169, 249);
    cardPanel.setBackground(cardBackground);

    JLabel[] labels = new JLabel[9];
    for (int i = 0; i < 9; i++) {
      labels[i] = new JLabel("", SwingConstants.CENTER);
      labels[i].setOpaque(true);
      labels[i].setBackground(cardBackground);
    }

    labels[1].setText(String.valueOf(card.getNorthValue()));
    labels[3].setText(String.valueOf(card.getWestValue()));
    labels[5].setText(String.valueOf(card.getEastValue()));
    labels[7].setText(String.valueOf(card.getSouthValue()));

    for (JLabel label : labels) {
      label.setFont(new Font("Arial", Font.BOLD, 24));
      label.setForeground(Color.BLACK);
      cardPanel.add(label);
    }

    cardPanel.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        if (eventListener != null) {
          List<Card> hand = owner == PlayerColor.RED
                  ? model.getRedPlayerHand()
                  : model.getBluePlayerHand();
          int index = hand.indexOf(card);
          System.out.println("Clicked " + owner + " player's card at index: " + index);
          eventListener.onCardSelected(card, owner);
        }
      }
    });

    return cardPanel;
  }

  @Override
  public void updateHand(List<Card> cards) {
    handPanel.removeAll();
    for (Card card : cards) {
      JPanel cardPanel = createCardPanel(card, player);
      handPanel.add(cardPanel);
    }
    handPanel.revalidate();
    handPanel.repaint();
  }

  @Override
  public void highlightCard(Card card, boolean highlight) {
    Component[] components = handPanel.getComponents();
    for (Component comp : components) {
      if (comp instanceof JPanel) {
        JPanel cardPanel = (JPanel) comp;
        if (cardPanel.getComponents().length > 0) {
          JLabel northLabel = (JLabel) cardPanel.getComponent(1);
          JLabel westLabel = (JLabel) cardPanel.getComponent(3);
          JLabel eastLabel = (JLabel) cardPanel.getComponent(5);
          JLabel southLabel = (JLabel) cardPanel.getComponent(7);

          if (northLabel.getText().equals(String.valueOf(card.getNorthValue()))
                  && westLabel.getText().equals(String.valueOf(card.getWestValue()))
                  && eastLabel.getText().equals(String.valueOf(card.getEastValue()))
                  && southLabel.getText().equals(String.valueOf(card.getSouthValue()))) {

            cardPanel.setBorder(
                    BorderFactory.createLineBorder(
                            highlight ? Color.YELLOW : Color.BLACK, highlight ? 3 : 1));
            cardPanel.revalidate();
            cardPanel.repaint();
          }
        }
      }
    }
  }

  @Override
  public void setGameEventListener(EventListener listener) {
    this.eventListener = listener;
  }
}
