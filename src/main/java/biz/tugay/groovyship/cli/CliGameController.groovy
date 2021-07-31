package biz.tugay.groovyship.cli

import biz.tugay.groovyship.modal.Game
import biz.tugay.groovyship.service.GameService

import java.nio.charset.StandardCharsets

import static java.lang.Integer.parseInt

class CliGameController
{

  static GameService gameService = new GameService()

  static Scanner scanner = new Scanner(System.in, StandardCharsets.UTF_8.toString())

  static BoardCommandLinePrinter boardCommandLinePrinter = new BoardCommandLinePrinter()

  static void main(String[] args) {
    Game game = null
    println "Type ng to create a new game at any time or exit to stop playing."
    println "Send missile at coordinates in the format: columnIndex,rowIndex."
    println "Top left corner is coordinates 0,0"
    println "Type ng to start a new game now."

    while (true) {
      if (game) {
        boardCommandLinePrinter.print game.board
        if (gameService.allShipsSank(game)) {
          println "Congratulations, you win. Your game has ended. "
          game = null
        }
      }

      if (!game) {
        println "Enter one of: ng | exit"
      }
      else {
        println "Enter coordinates: 'column,row' to send a missile, 'ng' for a new game, 'exit' to exit."
        println "Enter 'undo' or 'redo' to move back or forward in the game timeline."
      }

      def userInput = getUserInput()
      if ("exit" == userInput) {
        System.exit 1
      }

      if ("ng" == userInput) {
        game = newGameBasedOnUserInput()
        if (!game) {
          println "We could not generate a random board with the provided number of ships in the provided board."
          println "Enter a bigger board and less number of ships."
        }
        continue
      }

      if (game == null) {
        println "You either entered and unknown command or there is no active game."
        continue
      }

      if ("undo" == userInput) {
        game = gameService.undo(game)
        continue
      }

      if ("redo" == userInput) {
        game = gameService.redo(game)
        continue
      }

      game = sendMissile(userInput, game)
    }
  }

  private static Game newGameBasedOnUserInput() {
    try {
      println "Board size?"
      def boardSize = parseInt getUserInput()

      println "How many ships?"
      def numberOfShips = parseInt getUserInput()

      return gameService.createNewGame(boardSize, numberOfShips)
    }
    catch (Exception e) {
      println "Something went wrong: $e.message. Try again."
      return newGameBasedOnUserInput()
    }
  }

  private static String getUserInput() {
    try {
      return scanner.nextLine()
    }
    catch (Exception ignored) {
      println "That is not a known command. Try again."
      return getUserInput()
    }
  }

  private static Game sendMissile(String userInput, Game game) {
    try {
      def column = parseInt userInput[0]
      def row = parseInt userInput[-1]
      def (updatedGame, isHit) = gameService.sendMissile(game, column, row)
      if (isHit) {
        println "Hit."
      }
      else {
        println "Missed."
      }
      return updatedGame
    }
    catch (Exception ignored) {
      println "Please provide coordinates in the format: 0,0"
    }
    return game
  }
}
