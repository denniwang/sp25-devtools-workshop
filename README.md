## Overview

This codebase is designed to implement a card game called "ThreeTrioGame". The game involves
creating a board and a deck of cards from configuration files, and players take turns to play the
game until a winner is determined. The codebase assumes that users have a basic understanding of
Java programming and familiarity with concepts like file I/O, collections, and object-oriented
programming. The codebase is designed to be extensible, allowing for modifications to the game
rules, card properties, and board configurations.
hello

## New Features for Part 5

1. Hints: If you're struggling to beat our AI, you can now start your game in easy mode to get hints
   on what moves to make.
2. New modes: You can now play the game in reverse mode, fallen angel mode, or both!
3. New rules: You can add one rule to the game such as the Same and Plus rules.

## Changes for Part 5

- Added decorator classes for ThreeTrioModel to add new rules and modes to the game.
- Changed the main function to take in more arguments to allow for new modes, rules, and hints to be added
  to the game.
- Moved some functions from the ThreeTrioGame class to the ThreeTrioModel class to streamline
  developer experience for decorators.

## Quick start (Part 4)

1. `cd` into the directory containing the jar file
2. run `java -jar three-trios.jar human human` to start the game with two human players (player 1
   will have our view implementation, player 2 will have the provider's view implementation)

## Provider View Features (HW 8 - Implementation)

- There were no changes to the command line interface (see above for quick start). The provider view
  is hard coded in for player 2 as per assignment instructions
- We were able to get all view functions working. It is able to display the game state and interact
  with the model the same way our original view is able to
- Some difficulties we ran into with our provider is that some classes that should be interfaces
  were concrete classes. Our providers were kind enough to gut and refactor their code to turn them
  into interfaces.

## Changes for Customer (HW 8 - Views)

- Turned Move class intro an interface
- Player getMove should return the Move interface
- ThreeTrioCard interface deepCopy() returns type ThreeTrioCard instead of concrete class
  PlayingCard

## Changes for Part 3

- Created interface `ThreeTrioGuiFeatures` for player actions
- Created interface `ControllerFeatures` for model updates / status
- Both interfaces are implemented in the controller.
- Models can add controllers as subscribers to listen for updates.
- Controller is able to tell view to notify players and let them know if its their turn.
- Controller is able to listen to view for player actions and update the model and view accordingly.
- Game can now be played in two windows in various settings: (AI is powered by our strategy classes)
    - Player vs Player
    - Player vs AI
    - AI vs AI
- Updated main function to take in command line args for the two players playing the game.
- No Major design changes were needed to be made.

## Changes for Part 2

- Added public helper function getOtherPlayerHand(), isLegalMove(), getCard() in our model to make
  it easier for strategies to interact with the game
- Created a ReadOnly interface to ensure no unnecessary classes can modify the game state

## Extra credit

- Created `LeastLikelyToFlipStrategy.java` (strategy #3), `MinimaxStrategy.java` (strategy #4),
  and `ComplexPlayerStrategy.java` to chain together strategies
- Tests for the LeastLikelyToFlip, MinMax, and Complex Strategy can be found
  in `test/StrategyTests.java`

## Quick start

1. Direct yourself to the test directory to run the program.
2. Setup the model and view
    - game = new ThreeTrioGame("board.config", "deck.config");
    - this.deck = game.createDeck();
    - this.board = game.createBoard();
    - game.setBoard(this.board);
    - game.setDeck(this.deck);
    - view = new GameView(game);
3. Play the game to your liking
    - try { <br>
      &nbsp;&nbsp;&nbsp;&nbsp;game.startGame(game.createDeck(), game.createBoard()); <br>
      } catch (FileNotFoundException e) { <br>
      &nbsp;&nbsp;&nbsp;&nbsp;e.printStackTrace(); <br>
      }
    - game.playToBoard(0,0,0); <br>
      game.playToBoard(0,1,0); <br>
      whatever other commands you want...
    - game.isGameOver(); <br>
      game.getWinner(); <br>
      whatever else you would want to see

## Key Components

- **ThreeTrioGame**: This is the main class that drives the control flow of the game. It handles the
  creation of the deck and board, manages the game state, and determines the winner.
- **ThreeTrioCard**: Represents a card in the game. Each card has a name, attack values for four
  directions, and a color.
- **ConfigReader**: Responsible for reading the configuration files (`deck.config`
  and `board.config`) and creating the deck and board based on the configurations.

## Key Subcomponents

### ThreeTrioGame

- `createDeck()`: Reads the `deck.config` file and creates a list of `ThreeTrioCard` objects.
- `createBoard()`: Reads the `board.config` file and creates a 2D array of `ThreeTrioCard` objects.
- `isGameOver()`: Checks if the game is over by verifying if all cards have been played.
- `getWinner()`: Determines the winner based on the scores of the players.
- `boardToString()`, `deckToString()`, `handToString()`: Utility methods to convert the board, deck,
  and hand to string representations.

### ThreeTrioCard

- `ThreeTrioCard(String name, Attack north, Attack south, Attack east, Attack west)`: Constructor to
  initialize a card with a name and attack values.
- `isHole()`: Checks if the card is a hole.
- `getColor()`: Returns the color of the card.
- `shortString()`, `toString()`: Utility methods to convert the card to string representations.

### ConfigReader

- `createDeck()`: Reads the `deck.config` file and creates a list of `ThreeTrioCard` objects.
- `createBoard()`: Reads the `board.config` file and creates a 2D array of `ThreeTrioCard` objects.

### Source Organization

#### Directories and Components

- **src/model**: Contains the core game logic and data structures.
    - `ThreeTrioGame.java`: Main class that drives the control flow of the game.
    - `ThreeTrioCard.java`: Represents a card in the game.
    - `ConfigReader.java`: Responsible for reading the configuration files and creating the deck and
      board.

- **src/playerstrategy**: Contains the core game logic and data structures.
    - `playerstrategy.java`: Strategy interface that all other strategies implement.
    - `StrategyType.java`: Enum for all the types of strategies. Used in the minmax strategy.
    - `TieBreaker.java`: Helper class to break ties between Moves in a strategy.
    - `Move.java`: Represents a move for a ThreeTrios game.

- **src/view**: Contains the classes related to the game's user interface.
    - `ThreeTrioGuiView.java`: View interface to list out functionality of the GUI.
    - `ThreeTrioGuiFeatures.java`: Features interface to allow for easy implementation of new
      features in the GUI.
    - `GameView.java`: Manages the overall display of the game state and integrates the
      ThreeTrioPanel for rendering the game.
    - `ThreeTrioPanel.java`: Handles the graphical representation of the game board and player
      hands, and manages user interactions with the game.
    - `GuiGameView.java`: Class that extends JFrame. Takes in a model to render using JPanel and
      provides functionality for panel.

- **/test/**: Contains the test cases for the game. Including a folder specifically for model tests.
    - `ModelTests.java`: Tests for the core game logic in `ThreeTrioGame`.
    - `PlayingCardTests.java`: Tests for the cards used in`ThreeTrioGame`.
    - `Gameplay.java`: Tests for more complex game logic, including full games.
    - `ModelTests.java`: Tests for the core game logic in `ThreeTrioGame`.
