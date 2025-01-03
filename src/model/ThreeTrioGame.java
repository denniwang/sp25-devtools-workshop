package model;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import controller.ThreeTrioControllerFeatures;
import player.Player;

/**
 * Represents a game of Three Trio. Includes board, deck, player hands, and game state information.
 *
 * @invariant: playerOneTurn cannot be true if playerOne played their last card.
 */
public class ThreeTrioGame implements ThreeTrioModel<ThreeTrioCard> {

  /**
   * Index (0,0) is the top left corner of the board.
   * Index (0,numCols-1) is the top right corner of the board.
   * Index (numRows-1,0) is the bottom left corner of the board.
   * Index (numRows-1, numCols-1) is the bottom right corner of the board.
   * Generally speaking all the rows are horizontal, and all the columns are vertical
   * and zero indexed.
   */
  protected ThreeTrioCard[][] board;
  protected List<ThreeTrioCard> deck;
  protected List<ThreeTrioCard> playerOneHand;
  protected List<ThreeTrioCard> playerTwoHand;
  protected boolean playerOneTurn;
  protected boolean winnersNotified;
  protected ConfigReader configReader;
  protected boolean gameStarted;
  protected boolean gameOver;
  protected List<ThreeTrioControllerFeatures> controllerListeners;
  protected String boardConfig;
  protected String deckConfig;
  private Player player1;
  private Player player2;


  /**
   * Constructs a new game of Three Trio with an empty game.
   * In order to actually start the game and initialize the deck
   * board, and playerHands, the startGame method must be called.
   * This constructor simply initializes the game itself,
   * not it's contents.
   */
  public ThreeTrioGame() {
    this.deck = new ArrayList<>();
    this.playerOneTurn = true;
    this.playerOneHand = new ArrayList<>();
    this.playerTwoHand = new ArrayList<>();
    this.gameStarted = false;
    this.gameOver = false;
    controllerListeners = new ArrayList<>();
    this.winnersNotified = false;
  }

  /**
   * Constructs a new game of Three Trio with the given board and deck configurations.
   *
   * @param boardConfig the path to the board configuration file
   * @param deckConfig  the path to the deck configuration file
   * @throws NullPointerException if either boardConfig or deckConfig is null
   */
  public ThreeTrioGame(String boardConfig, String deckConfig) {
    this.deck = new ArrayList<>();
    this.playerOneTurn = true;
    this.configReader = new ConfigReader(Objects.requireNonNull(boardConfig),
            Objects.requireNonNull(deckConfig));
    this.playerOneHand = new ArrayList<>();
    this.playerTwoHand = new ArrayList<>();
    this.gameStarted = false;
    this.gameOver = false;
    controllerListeners = new ArrayList<>();
    this.boardConfig = boardConfig;
    this.deckConfig = deckConfig;
  }

  /**
   * Constructs a new game of Three Trio with the given board and deck configurations.
   * Used only for strategies that need a temporary model.
   * Note: this sets the first hand parameter as Player one, should ONLY BE USED FOR MIMICKING
   * REAL GAME
   *
   * @param curHand       hand of currentPlayer
   * @param otherHand     hand of not current Player
   * @param board         preset board
   * @param playerOneTurn true if it is player 1's turn, false if it is player 2's turn
   */
  public ThreeTrioGame(List<ThreeTrioCard> curHand, List<ThreeTrioCard> otherHand,
                       ThreeTrioCard[][] board, boolean playerOneTurn) {

    this.playerOneTurn = true;
    this.playerOneHand = playerOneTurn ? curHand : otherHand;
    this.playerTwoHand = playerOneTurn ? otherHand : curHand;
    this.board = board;
    this.gameStarted = true;
    this.gameOver = false;
    controllerListeners = new ArrayList<>();
    this.winnersNotified = false;
  }

  @Override
  public void setPlayers(Player p1, Player p2) {
    this.player1 = p1;
    this.player2 = p2;
  }

  @Override
  public Player getActivePlayer() {
    return this.playerOneTurn ? player1 : player2;
  }


  /**
   * Add a controller listener to react to model changes.
   *
   * @param listener the controller listener to add
   */
  public void addControllerListener(ThreeTrioControllerFeatures listener) {
    this.controllerListeners.add(listener);
  }

  /**
   * Set a custom deck of cards for this specific model.
   *
   * @param deck the deck of cards to use for the game
   */
  public void setDeck(List<ThreeTrioCard> deck) {
    this.deck = deck;
  }

  @Override
  public int getBoardW() {
    if (board == null) {
      throw new IllegalStateException("Board has not been initialized");
    }
    return board[0].length;
  }

  /**
   * Get board height.
   *
   * @return board height.
   */
  @Override
  public int getBoardH() {
    if (board == null) {
      throw new IllegalStateException("Board has not been initialized");
    }
    return board.length;
  }

  @Override
  public List<ThreeTrioCard> getPlayerOneHand() {

    return new ArrayList<>(playerOneHand);
  }

  @Override
  public List<ThreeTrioCard> getPlayerTwoHand() {
    return new ArrayList<>(playerTwoHand);
  }

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
  @Override
  public void startGame(List<ThreeTrioCard> deck, ThreeTrioCard[][] board) {
    if (deck == null || board == null) {
      throw new IllegalArgumentException("The deck and board must not be null");
    }
    for (ThreeTrioCard[] row : board) {
      for (ThreeTrioCard card : row) {
        if (card == null) {
          throw new IllegalArgumentException("The board must not contain any null elements");
        }
      }
    }
    if (board.length % 2 == 0 || board[0].length % 2 == 0) {
      throw new IllegalArgumentException("The board must have odd number of tiles");
    }
    this.board = board;
    if (deck.size() < getNumTiles() + 1) {
      throw new IllegalArgumentException("There must be enough cards to start the game, deck size:"
              + deck.size() + " numTiles: " + getNumTiles());
    }
    if (this.gameStarted || this.gameOver) {
      throw new IllegalStateException("The game is already started or is already over");
    }
    List<ThreeTrioCard> deckCopy = new ArrayList<>();
    for (ThreeTrioCard card : deck) {
      deckCopy.add(card);
    }
    this.deck = deckCopy;
    // Collections.shuffle(this.deck);
    this.dealCards();
    this.gameStarted = true;
    if (this.controllerListeners.size() > 0) {
      this.controllerListeners.get(0).notifyPlay();
    }
  }

  @Override
  public void dealCards() {
    if (this.gameStarted) {
      throw new IllegalStateException("The game has already started");
    }
    int count = 0;
    for (ThreeTrioCard curCard : deck) {
      if (count % 2 == 0) {
        curCard.setColor(Color.RED);
        playerOneHand.add(curCard);
      } else {
        curCard.setColor(Color.BLUE);
        playerTwoHand.add(curCard);
      }
      count++;
      // make sure to not over deal cards.
      if (count == getNumTiles() + 1) {
        return;
      }
    }
  }

  @Override
  public void playToBoard(int row, int col, int handIdx) {
    if (row < 0 || row >= board.length || col < 0 || col >= board[0].length) {
      throw new IllegalArgumentException("Invalid row or column");
    }
    if (board[row][col].isHole()) {
      throw new IllegalArgumentException("Must play to a tile");
    }
    if (board[row][col].getColor() != null) {
      throw new IllegalArgumentException("Cannot play to a tile that already has a card");
    }
    if (this.playerOneTurn) {
      if (handIdx < 0 || handIdx >= playerOneHand.size()) {
        throw new IllegalArgumentException("Invalid hand index for P1: " + handIdx);
      }
      this.board[row][col] = playerOneHand.remove(handIdx);
      this.playerOneTurn = false;
    } else {
      if (handIdx < 0 || handIdx >= playerTwoHand.size()) {
        throw new IllegalArgumentException("Invalid hand index for P2: " + handIdx);
      }
      this.board[row][col] = playerTwoHand.remove(handIdx);
      this.playerOneTurn = true;
    }
    startBattle(row, col);

    //System.out.println(this.controllerListeners.size());
    for (ThreeTrioControllerFeatures listener : controllerListeners) {
      if (listener.getUsername().equals(this.getActivePlayer().getName())) {
        listener.notifyPlay();
      }
      listener.refresh();
    }
    if (this.isGameOver()) {
      for (ThreeTrioControllerFeatures listener : controllerListeners) {
        if (!this.winnersNotified) {
          listener.notifyWinner();
        }
      }
      this.winnersNotified = true;
    }
  }

  @Override
  public void startBattle(int row, int col) {
    if (!this.gameStarted || this.gameOver) {
      throw new IllegalStateException("The game is not started or is already over");
    }
    ThreeTrioCard lastCard = board[row][col];
    List<ThreeTrioCard> flippedCards = new ArrayList<>();
    flipAdjacentCards(row, col, lastCard, flippedCards);
  }

  /**
   * Flips adjacent cards to the given card.
   *
   * @param row          the row of the card
   * @param col          the column of the card
   * @param lastCard     the card to flip adjacent cards from
   * @param flippedCards the list of flipped cards
   */
  protected void flipAdjacentCards(int row, int col, ThreeTrioCard lastCard,
                                   List<ThreeTrioCard> flippedCards) {
    // Check NORTH
    checkAndFlip(row - 1, col, Direction.NORTH, Direction.SOUTH, row, col, lastCard, flippedCards);
    // Check SOUTH
    checkAndFlip(row + 1, col, Direction.SOUTH, Direction.NORTH, row, col, lastCard, flippedCards);
    // Check WEST
    checkAndFlip(row, col - 1, Direction.WEST, Direction.EAST, row, col, lastCard, flippedCards);
    // Check EAST
    checkAndFlip(row, col + 1, Direction.EAST, Direction.WEST, row, col, lastCard, flippedCards);
  }

  // Updated flipAdjacentCards to take a board as a parameter

  /**
   * Flips adjacent cards to the given card, given a board in progress.
   *
   * @param board        the board in progress
   * @param row          the row of the card
   * @param col          the column of the card
   * @param lastCard     the card to flip adjacent cards from
   * @param flippedCards the list of flipped cards
   */
  protected void flipAdjacentCards(ThreeTrioCard[][] board, int row, int col,
                                   ThreeTrioCard lastCard, List<ThreeTrioCard> flippedCards) {
    // Check NORTH
    checkAndFlip(board, row - 1, col, Direction.NORTH,
            Direction.SOUTH, row, col, lastCard, flippedCards);
    // Check SOUTH
    checkAndFlip(board, row + 1, col, Direction.SOUTH,
            Direction.NORTH, row, col, lastCard, flippedCards);
    // Check WEST
    checkAndFlip(board, row, col - 1, Direction.WEST,
            Direction.EAST, row, col, lastCard, flippedCards);
    // Check EAST
    checkAndFlip(board, row, col + 1, Direction.EAST,
            Direction.WEST, row, col, lastCard, flippedCards);
  }

  /**
   * Recursively flips cards in a given direction.
   *
   * @param row          row of the last card placed
   * @param col          column of the last card placed
   * @param lastCard     the last card placed
   * @param flippedCards the list of flipped cards
   * @param lastDir      the direction from which the last card was placed
   */
  protected void recursivelyFlip(int row, int col, ThreeTrioCard lastCard,
                                 List<ThreeTrioCard> flippedCards, Direction lastDir) {
    // Check NORTH
    if (lastDir != Direction.NORTH) {
      checkAndFlip(row - 1, col, Direction.NORTH,
              Direction.SOUTH, row, col, lastCard, flippedCards);
    }
    // Check SOUTH
    if (lastDir != Direction.SOUTH) {
      checkAndFlip(row + 1, col, Direction.SOUTH,
              Direction.NORTH, row, col, lastCard, flippedCards);
    }
    // Check WEST
    if (lastDir != Direction.WEST) {
      checkAndFlip(row, col - 1, Direction.WEST,
              Direction.EAST, row, col, lastCard, flippedCards);
    }
    // Check EAST
    if (lastDir != Direction.EAST) {
      checkAndFlip(row, col + 1, Direction.EAST,
              Direction.WEST, row, col, lastCard, flippedCards);
    }
  }

  /**
   * Recursively flips cards in a given direction, given a board in progress.
   *
   * @param board        the board in progress
   * @param row          row of the last card placed
   * @param col          column of the last card placed
   * @param lastCard     the last card placed
   * @param flippedCards the list of flipped cards
   * @param lastDir      the direction from which the last card was placed
   */
  protected void recursivelyFlip(ThreeTrioCard[][] board, int row, int col, ThreeTrioCard lastCard,
                                 List<ThreeTrioCard> flippedCards, Direction lastDir) {
    // Check NORTH
    if (lastDir != Direction.NORTH) {
      checkAndFlip(board, row - 1, col, Direction.NORTH,
              Direction.SOUTH, row, col, lastCard, flippedCards);
    }
    // Check SOUTH
    if (lastDir != Direction.SOUTH) {
      checkAndFlip(board, row + 1, col, Direction.SOUTH,
              Direction.NORTH, row, col, lastCard, flippedCards);
    }
    // Check WEST
    if (lastDir != Direction.WEST) {
      checkAndFlip(board, row, col - 1, Direction.WEST,
              Direction.EAST, row, col, lastCard, flippedCards);
    }
    // Check EAST
    if (lastDir != Direction.EAST) {
      checkAndFlip(board, row, col + 1, Direction.EAST,
              Direction.WEST, row, col, lastCard, flippedCards);
    }
  }


  @Override
  public void checkAndFlip(int adjRow, int adjCol, Direction dirFrom, Direction dirTo, int row,
                           int col, ThreeTrioCard lastCard, List<ThreeTrioCard> flippedCards) {
    // Ensure the adjacent row and col are within bounds
    if (adjRow >= 0 && adjRow < board.length && adjCol >= 0 && adjCol < board[0].length) {
      ThreeTrioCard adjacentCard = board[adjRow][adjCol];

      // Check conditions for flipping the card
      if (adjacentCard.getColor() != lastCard.getColor()
              && !adjacentCard.isHole()
              && adjacentCard.getName() != null
              && compareAttacks(adjacentCard, lastCard, dirFrom, dirTo)) {
        flippedCards.add(adjacentCard);
        adjacentCard.setColor(lastCard.getColor());
        // recursively flip from this card we just flipped
        // note we pass the dirTo because to the new card, that is the direction we are coming from
        recursivelyFlip(adjRow, adjCol, adjacentCard, flippedCards, dirTo);
      }
    }
  }

  // Update checkAndFlip to take a board as a parameter
  @Override
  public void checkAndFlip(ThreeTrioCard[][] board, int adjRow, int adjCol,
                           Direction dirFrom, Direction dirTo, int row, int col,
                           ThreeTrioCard lastCard, List<ThreeTrioCard> flippedCards) {
    // Ensure the adjacent row and col are within bounds
    if (adjRow >= 0 && adjRow < board.length && adjCol >= 0 && adjCol < board[0].length) {
      ThreeTrioCard adjacentCard = board[adjRow][adjCol];

      // Check conditions for flipping the card
      if (adjacentCard.getColor() != lastCard.getColor()
              && !adjacentCard.isHole()
              && adjacentCard.getName() != null
              && compareAttacks(adjacentCard, lastCard, dirFrom, dirTo)) {
        flippedCards.add(adjacentCard);
        adjacentCard.setColor(lastCard.getColor());
        // recursively flip from this card we just flipped
        recursivelyFlip(board, adjRow, adjCol, adjacentCard, flippedCards, dirTo);
      }
    }
  }

  /**
   * Checks to see if the game is over.
   *
   * @return true if the game is over, false otherwise
   */
  @Override
  public boolean isGameOver() {
    if (!this.gameStarted) {
      throw new IllegalStateException("The game has not started yet");
    }
    for (ThreeTrioCard[] row : board) {
      for (ThreeTrioCard card : row) {
        if (!card.isHole() && card.getName() == null) {
          return false;
        }
      }
    }
    this.gameOver = true;
    return true;
  }

  @Override
  public Color getWinner() {
    if (!this.gameStarted || !this.gameOver) {
      throw new IllegalStateException("The game is not finished or started");
    }
    int redScore = 0;
    int blueScore = 0;
    for (ThreeTrioCard[] row : board) {
      for (ThreeTrioCard card : row) {
        if (card.getColor() == Color.RED) {
          redScore++;
        } else if (card.getColor() == Color.BLUE) {
          blueScore++;
        }
      }
    }
    redScore += this.playerOneHand.size();
    blueScore += this.playerTwoHand.size();
    if (redScore > blueScore) {
      return Color.RED;
    } else if (blueScore > redScore) {
      return Color.BLUE;
    } else {
      return null;
    }
  }

  @Override
  public List<ThreeTrioCard> createDeck() throws FileNotFoundException {
    try {
      return configReader.createDeck();
    } catch (NullPointerException e) {
      throw new IllegalArgumentException("Deck config path not given");
    }
  }

  @Override
  public ThreeTrioCard[][] createBoard() throws FileNotFoundException {
    System.out.println("CR : " + configReader);
    try {
      return configReader.createBoard();
    } catch (NullPointerException e) {
      throw new IllegalArgumentException("Board config path not given");
    }
  }

  @Override
  public boolean compareAttacks(ThreeTrioCard adjacentCard, ThreeTrioCard lastCard,
                                Direction dirFrom, Direction dirTo) {
    return (lastCard.getAttacks().get(dirFrom).getValue()
            > adjacentCard.getAttacks().get(dirTo).getValue());
  }

  @Override
  public int getNumTiles() {
    if (board == null) {
      throw new IllegalStateException("Board has not been initialized");
    }
    int numTiles = 0;
    for (int row = 0; row < board.length; row++) {
      for (int col = 0; col < board[row].length; col++) {
        if (!board[row][col].isHole()) {
          numTiles++;
        }
      }
    }
    return numTiles;
  }

  /**
   * Returns the player's hand.
   *
   * @return the player's hand
   */
  @Override
  public List<ThreeTrioCard> getPlayerHand() {
    if (this.playerOneTurn) {
      return new ArrayList<>(playerOneHand);
    } else {
      return new ArrayList<>(playerTwoHand);
    }
  }

  /**
   * Returns the player's hand.
   *
   * @return the player's hand
   */
  @Override
  public List<ThreeTrioCard> getOtherPlayerHand() {
    if (this.playerOneTurn) {
      return new ArrayList<>(playerTwoHand);
    } else {
      return new ArrayList<>(playerOneHand);
    }
  }

  /**
   * Returns a new copy of this board.
   *
   * @return the board
   */
  @Override
  public ThreeTrioCard[][] getBoard() {
    if (this.board == null) {
      //System.out.println("Board is null :(((");
      try {
        setBoard(createBoard());
      } catch (FileNotFoundException e) {
        throw new IllegalArgumentException("Board config path not given");
      }
      return null;
    }
    ThreeTrioCard[][] boardCopy = new ThreeTrioCard[this.board.length][this.board[0].length];
    for (int i = 0; i < this.board.length; i++) {
      for (int j = 0; j < this.board[i].length; j++) {
        boardCopy[i][j] = this.board[i][j].deepCopy();
      }
    }
    return boardCopy;
  }

  /**
   * Sets the board to the given board.
   *
   * @param board the board to set
   */
  public void setBoard(ThreeTrioCard[][] board) {
    this.board = board;
  }

  /**
   * Returns the current turn.
   *
   * @return the current turn
   */
  @Override
  public boolean getTurn() {
    return this.playerOneTurn;
  }

  @Override
  public ThreeTrioCard getCard(int row, int col) {
    if (row < 0 || row >= this.board.length || col < 0 || col >= this.board[0].length) {
      throw new IllegalArgumentException("Out of bounds error");
    }
    return this.board[row][col].deepCopy();
  }

  @Override
  public Color getCardColor(int row, int col) {
    ThreeTrioCard card = this.getCard(row, col);
    if (card.isHole()) {
      throw new IllegalArgumentException("Cannot get color of a hole card");
    }
    if (card.getName() == null) {
      throw new IllegalArgumentException("There is not a card played at the given row and col");
    }
    return card.getColor();
  }

  @Override
  public int getScore(Color color) {
    int score = 0;
    for (ThreeTrioCard[] row : this.board) {
      for (ThreeTrioCard card : row) {
        if (card.getColor() == color) {
          score++;
        }
      }
    }
    if (color == Color.RED) {
      score += this.playerOneHand.size();
    } else {
      score += this.playerTwoHand.size();
    }
    return score;
  }

  @Override
  public int countPossibleFlips(int row, int col, ThreeTrioCard card) {
    if (!this.gameStarted || this.gameOver) {
      throw new IllegalStateException("The game is not started or is already over");
    }
    if (board[row][col].isHole()) {
      throw new IllegalArgumentException("Must play to a tile");
    }

    // Create a temporary deep copy of the board for simulation
    ThreeTrioCard[][] tempBoard = this.getBoard();

    List<ThreeTrioCard> flippedCards = new ArrayList<>();
    flipAdjacentCards(tempBoard, row, col, card, flippedCards);

    return flippedCards.size();
  }


  /**
   * Checks if a move is valid (aka if it's a tile).
   *
   * @param row the row of the card
   * @param col the column of the card
   * @return true if the board location is a tile, false otherwise
   */
  public boolean isValidMove(int row, int col) {
    if (row < 0 || row >= board.length || col < 0 || col >= board[0].length) {
      return false;
    } else if (board[row][col].isHole()) {
      return false;
    } else {
      return board[row][col].getName() == null;
    }
  }

  @Override
  public boolean hasGameStarted() {
    return gameStarted;
  }

  @Override
  public String getBoardConfig() {
    return boardConfig;
  }

  @Override
  public String getDeckConfig() {
    return deckConfig;
  }
}

