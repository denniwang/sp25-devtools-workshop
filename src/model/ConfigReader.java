package model;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Class to read the configuration files and create the board and deck.
 */
public class ConfigReader {
  private final String boardConfig;
  private final String deckConfig;

  /**
   * Constructor for a ConfigReader that sets the configuration for both the games board and deck.
   * Post initialization, the board and deck can be created using the createBoard and createDeck
   * but are not created via the constructor.
   *
   * @param boardConfig the file path for the board configuration
   * @param deckConfig  the file path for the deck configuration
   */
  public ConfigReader(String boardConfig, String deckConfig) {
    this.boardConfig = boardConfig;
    this.deckConfig = deckConfig;
  }

  /**
   * Creates the board from the configuration file.
   *
   * @return a 2D array of cards representing the board
   * @throws IllegalStateException if board does not have an even number of spaces
   * @throws FileNotFoundException if the file is not found
   */
  public ThreeTrioCard[][] createBoard() throws FileNotFoundException {
    File boardF = new File(boardConfig);
    Scanner sc = new Scanner(boardF);
    int rows = sc.nextInt();
    int cols = sc.nextInt();
    if (rows * cols % 2 != 1) {
      throw new IllegalStateException("Board must have an even number of spaces");
    }
    sc.nextLine();
    PlayingCard[][] board = new PlayingCard[rows][cols];
    int rowIdx = 0;
    while (sc.hasNextLine()) {
      String line = sc.nextLine();
      for (int col = 0; col < cols; col++) {
        if (line.charAt(col) == 'X') {
          board[rowIdx][col] = new PlayingCard(true);
        } else {
          board[rowIdx][col] = new PlayingCard();
        }
      }
      rowIdx++;
    }
    sc.close();
    return board;
  }

  /**
   * Creates a deck of cards from the configuration file.
   *
   * @return a list of all the cards in the deck
   * @throws FileNotFoundException if the file is not found
   */
  public List<ThreeTrioCard> createDeck() throws FileNotFoundException {
    File deckF = new File(deckConfig);
    Scanner sc = new Scanner(deckF);
    List<ThreeTrioCard> allCards = new ArrayList<>();
    while (sc.hasNext()) {
      String name = sc.next();
      String north = sc.next();
      String south = sc.next();
      String east = sc.next();
      String west = sc.next();
      //System.out.println(name + " " + north + " " + south + " " + east + " " + west);
      PlayingCard card = new PlayingCard(name,
              mapToAttack(north),
              mapToAttack(south),
              mapToAttack(east),
              mapToAttack(west));
      allCards.add(card);
      if (sc.hasNextLine()) {
        sc.nextLine();
      }
    }

    sc.close();
    return allCards;
  }

  private Attack mapToAttack(String value) {
    switch (value) {
      case "1":
        return Attack.ONE;
      case "2":
        return Attack.TWO;
      case "3":
        return Attack.THREE;
      case "4":
        return Attack.FOUR;
      case "5":
        return Attack.FIVE;
      case "6":
        return Attack.SIX;
      case "7":
        return Attack.SEVEN;
      case "8":
        return Attack.EIGHT;
      case "9":
        return Attack.NINE;
      case "A":
        //System.out.println("A");
        return Attack.A;
      default:
        throw new IllegalArgumentException("Invalid attack value: " + value);
    }
  }
}
