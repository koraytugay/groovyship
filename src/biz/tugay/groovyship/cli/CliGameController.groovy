package biz.tugay.groovyship.cli

import biz.tugay.groovyship.modal.Board
import biz.tugay.groovyship.service.GameService

import java.nio.charset.StandardCharsets

import static java.lang.Integer.parseInt

class CliGameController
{

  static GameService gameService = new GameService()

  static Scanner scanner = new Scanner(System.in, StandardCharsets.UTF_8.toString())

  static BoardCommandLinePrinter boardCommandLinePrinter = new BoardCommandLinePrinter()

  static Board board = null

  static void main(String[] args) {
    println "Type ng to create a new game at any time or exit to stop playing."
    println "Send missile at coordinates in the format: columnIndex,rowIndex."
    println "Top left corner is coordinates 0,0"
    println "Type ng to start a new game now."

    while (true) {
      if (board) {
        boardCommandLinePrinter.print board
        if (gameService.allShipsSank board) {
          println "Congratulations, you win. Your game has ended. "
          board = null
        }
      }

      if (!board) {
        println "Enter one of: ng | exit"
      }
      else {
        println "Enter coordinates: 'column,row' to send a missile, 'ng' for a new game, 'exit' to exit."
      }

      def userInput = getUserInput()
      if ("exit" == userInput) {
        System.exit 1
      }

      if ("ng" == userInput) {
        board = newBoardBasedOnUserInput()
        if (!board) {
          println "We could not generate a random board with the provided number of ships in the provided board."
          println "Enter a bigger board and less number of ships."
        }
        continue
      }

      if (board == null) {
        println "You either entered and unknown command or there is no active game."
        continue
      }

      sendMissile userInput, board
    }
  }

  private static Board newBoardBasedOnUserInput() {
    try {
      println "Board size?"
      def boardSize = parseInt getUserInput()

      println "How many ships?"
      def numberOfShips = parseInt getUserInput()

      return gameService.createNewGame(boardSize, numberOfShips)
    }
    catch (Exception e) {
      println "Something went wrong: $e.message. Try again."
      return newBoardBasedOnUserInput()
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

  private static void sendMissile(String userInput, Board board) {
    try {
      def column = parseInt userInput[0]
      def row = parseInt userInput[-1]
      if (gameService.sendMissile board, column, row) {
        println "Hit."
      }
      else {
        println "Missed."
      }
    }
    catch (Exception ignored) {
      println "Please provide coordinates in the format: 0,0"
    }
  }
}
