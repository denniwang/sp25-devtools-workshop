package provider;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import provider.Card;
import provider.Cell;
import provider.CellType;
import provider.Grid;
import provider.PlayerColor;
import provider.ReadonlyThreeTriosModel;

/**
 * This class holds the methods to create the actual game board for the game
 * that we see in the view.
 */
public class ThreeTriosBoard implements GameBoard {
  private final ReadonlyThreeTriosModel model;
  private JPanel gridPanel;
  private EventListener eventListener;

  /**
   * Constructor for ThreeTriosBoard.
   *
   * @param model the readonly model of Three Trios game
   */
  public ThreeTriosBoard(ReadonlyThreeTriosModel model) {
    this.model = model;
  }

  @Override
  public JPanel createGridPanel() {
    Grid grid = model.getGridCopy();
    gridPanel = new JPanel(new GridLayout(grid.getRows(), grid.getCols()));

    for (int r = 0; r < grid.getRows(); r++) {
      for (int c = 0; c < grid.getCols(); c++) {
        Cell cell = grid.getCell(r, c);
        JPanel cellPanel = createCellPanel(cell, r, c);
        gridPanel.add(cellPanel);
      }
    }
    return gridPanel;
  }

  private JPanel createCellPanel(Cell cell, int row, int col) {
    JPanel cellPanel = new JPanel();
    cellPanel.setPreferredSize(new Dimension(120, 120));
    cellPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

    if (cell.getType() == CellType.HOLE) {
      cellPanel.setBackground(new Color(192, 189, 187));
    } else if (cell.getCard() != null) {
      Card card = cell.getCard();
      PlayerColor owner = model.getCardOwner(card);
      cellPanel.setBackground(owner == PlayerColor.RED
              ? new Color(254, 167, 169)
              : new Color(73, 169, 249));

      cellPanel.setLayout(new GridLayout(3, 3));
      JLabel[] labels = new JLabel[9];
      for (int i = 0; i < 9; i++) {
        labels[i] = new JLabel("", SwingConstants.CENTER);
      }

      labels[1].setText(String.valueOf(card.getNorthValue()));
      labels[3].setText(String.valueOf(card.getWestValue()));
      labels[5].setText(String.valueOf(card.getEastValue()));
      labels[7].setText(String.valueOf(card.getSouthValue()));

      for (JLabel label : labels) {
        label.setFont(new Font("Arial", Font.BOLD, 24));
        label.setForeground(Color.BLACK);
        cellPanel.add(label);
      }
    } else {
      cellPanel.setBackground(new Color(212, 196, 0));
    }

    cellPanel.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        if (eventListener != null) {
          System.out.println("Clicked grid cell at row: " + row + ", column: " + col);
          eventListener.onCellClicked(row, col);
        }
      }
    });
    return cellPanel;
  }

  @Override
  public void updateCell(int row, int col, Card card) {
    Grid grid = model.getGridCopy();
    if (row >= 0 && row < grid.getRows() && col >= 0 && col < grid.getCols()) {
      Component component = gridPanel.getComponent(row * grid.getCols() + col);
      if (component instanceof JPanel) {
        JPanel cellPanel = (JPanel) component;
        cellPanel.removeAll();

        if (card != null) {
          PlayerColor owner = model.getCardOwner(card);
          JLabel cardLabel = new JLabel(card.getName());
          cardLabel.setForeground(owner == PlayerColor.RED ? Color.RED : Color.BLUE);
          cellPanel.add(cardLabel);
        }

        cellPanel.revalidate();
        cellPanel.repaint();
      }
    }
  }

  @Override
  public void refreshBoard() {
    if (gridPanel != null) {
      gridPanel.removeAll();
      Grid grid = model.getGridCopy();
      for (int r = 0; r < grid.getRows(); r++) {
        for (int c = 0; c < grid.getCols(); c++) {
          Cell cell = grid.getCell(r, c);
          System.out.println("This is what we think is at this spot: " + cell.toString());
          JPanel cellPanel = createCellPanel(cell, r, c);
          gridPanel.add(cellPanel);
        }
      }
      gridPanel.revalidate();
      gridPanel.repaint();
    }
  }

  @Override
  public void setGameEventListener(EventListener listener) {
    this.eventListener = listener;
  }
}
