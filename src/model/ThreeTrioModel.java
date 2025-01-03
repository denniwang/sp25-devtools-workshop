package model;

import java.io.FileNotFoundException;
import java.util.List;

import player.Player;

/**
 * Interface for the Three Trio game model.
 * Index (0,0) is the top left corner of the board.
 * Index (0,numCols-1) is the top right corner of the board.
 * Index (numRows-1,0) is the bottom left corner of the board.
 * Index (numRows-1, numCols-1) is the bottom right corner of the board.
 * Indexed row/col
 */
public interface ThreeTrioModel<C extends ThreeTrioCard> extends ReadonlyThreeTrioModel<C> {
  /**
   * Start the game of Three Trio.
   *
   * @param deck  the deck of cards to use for the game
   * @param board the board to use for the game
   * @throws IllegalArgumentException if the deck is not large enough to start the game
   * @throws IllegalStateException    if the game has already started or is already over
   * @throws IllegalArgumentException if the deck or board are null
   * @throws IllegalArgumentException if the board is not the correct size
   * @throws IllegalArgumentException if the board contains any null elements
   */
  void startGame(List<ThreeTrioCard> deck, ThreeTrioCard[][] board);

  /**
   * Play a card to the board.
   *
   * @param row     the row to play the card to
   * @param col     the column to play the card to
   * @param handIdx the index of the card in the player's hand
   * @throws IllegalArgumentException if the row or column are out of bounds
   * @throws IllegalArgumentException if the card is not played to a tile
   * @throws IllegalArgumentException if handIdx is out of bounds
   */
  void playToBoard(int row, int col, int handIdx);

  /**
   * Create the deck of cards for the game.
   *
   * @return a list of all the cards in the deck
   * @throws FileNotFoundException if the deck config file is not found.
   */
  List<C> createDeck() throws FileNotFoundException;

  /**
   * Create the board for the game.
   *
   * @return a 2D array of cards representing the board
   * @throws FileNotFoundException if the board config file is not found.
   */
  C[][] createBoard() throws FileNotFoundException;

  /**
   * Start a battle between the last card placed and those cards cardinally adjacent to it.
   *
   * @param row the row of the card
   * @param col the column of the card
   * @throws IllegalStateException if the game has not started or if the game is over.
   */
  void startBattle(int row, int col);

  /**
   * Deal the cards to the players at the start of the game.
   *
   * @throws IllegalStateException if game has already started.
   */
  void dealCards();

  /**
   * Set the deck of cards for the game.
   *
   * @param deck the deck of cards to use for the game
   */
  void setDeck(List<ThreeTrioCard> deck);

  /**
   * Set the board of cards for the game.
   *
   * @param board the board of cards to use for the game
   */
  void setBoard(ThreeTrioCard[][] board);

  /**
   * Set the players for the game.
   *
   * @param p1 the first player, who will go first
   * @param p2 the second player
   */
  void setPlayers(Player p1, Player p2);

  /**
   * Get the current player.
   *
   * @return either player 1 or player 2 based on game state
   */
  Player getActivePlayer();

  /**
   * Check if the move is valid. And flips the cards (necessary for decorators)
   *
   * @param adjRow       the row of the card to check
   * @param adjCol       the column of the card to check
   * @param dirFrom      the direction from which the card is being flipped
   * @param dirTo        the direction to which the card is being flipped
   * @param row          the row of the card
   * @param col          the column of the card
   * @param lastCard     the last card placed
   * @param flippedCards the list of cards to flip
   */
  void checkAndFlip(int adjRow, int adjCol, Direction dirFrom, Direction dirTo, int row,
                    int col, ThreeTrioCard lastCard, List<ThreeTrioCard> flippedCards);


  /**
   * Check if the move is valid. And flips the cards (necessary for decorators)
   *
   * @param board        the board of cards
   * @param adjRow       the row of the card to check
   * @param adjCol       the column of the card to check
   * @param dirFrom      the direction from which the card is being flipped
   * @param dirTo        the direction to which the card is being flipped
   * @param row          the row of the card
   * @param col          the column of the card
   * @param lastCard     the last card placed
   * @param flippedCards the list of cards to flip
   */
  void checkAndFlip(ThreeTrioCard[][] board, int adjRow, int adjCol,
                    Direction dirFrom, Direction dirTo, int row, int col,
                    ThreeTrioCard lastCard, List<ThreeTrioCard> flippedCards);

  /**
   * Return a boolean on whether flip the card or not.
   *
   * @param adjacentCard the target neighboring card.
   * @param lastCard     the last card we looked at.
   * @param dirFrom      the direction from which the card is being flipped
   * @param dirTo        the direction to which the card is being flipped
   * @return true if the card should be flipped, false otherwise (based on attacks).
   */
  boolean compareAttacks(ThreeTrioCard adjacentCard, ThreeTrioCard lastCard,
                         Direction dirFrom, Direction dirTo);

  /**
   * Returns the deck configuration.
   *
   * @return the deck configuration
   */
  String getDeckConfig();

  /**
   * Returns the board configuration.
   *
   * @return the board configuration
   */
  String getBoardConfig();

}
