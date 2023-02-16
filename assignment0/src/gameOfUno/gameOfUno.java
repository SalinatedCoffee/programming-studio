package gameOfUno;

import java.io.IOException;
import gameOfUno.model.*;
import gameOfUno.view.*;
import gameOfUno.controller.*;

public class gameOfUno {

    public static void main(String[] args) throws IOException  {
        Game game;
        boolean endGame = false;
        Player currentPlayer = null;

        int[] entityConfig = CLIController.askNumberOfPlayers();
        game = new Game(entityConfig[0], entityConfig[1], entityConfig[2]);

        System.out.println("Game Start!");  // Announce beginning of game with initial play order
        CLIView.playerOrder(game);
        System.out.println();

        while(!endGame) {                   // Loop until a player has no cards left to play
            currentPlayer = game.getCurrentPlayer();
            promptPlayerTurn(currentPlayer);
            System.out.println();
            if(currentPlayer.checkForWin()) {
                endGame = true;
            }
        }

        System.out.println("Player " + currentPlayer.playerID + " wins!");
    }

    /**
     * Prompt the player for input, or run AI.
     * @param player The current player.
     */
    private static void promptPlayerTurn(Player player) {
        Player.Mode mode = player.mode;

        System.out.println();

        switch(mode) {
            case BASELINE:
                player.baselineAITurn();
                break;
            case STRATEGIC:
                player.strategicAITurn();
                break;
            case MANUAL:
                CLIController.stallForNextPlayer();                     // Check if player is ready
                CLIView.currentCardInfo(player.playerSession);          // Show topmost card on discard pile
                CLIView.cardsInDeck(player.playerSession);              // Show number of cards left in deck

                while(true) {
                    player.showContentsOfHand();                        // Show cards in hand
                    int choice = CLIController.selectCardFromList();    // Get player selection

                    if (choice == -1) {                                 // Draw a card
                        Card drawn = player.playerSession.drawCard();

                        CLIView.announceDraw(drawn);
                        player.addToPlayerHand(drawn);
                        player.playerSession.updateCurrentPlayer();
                        break;
                    }
                    else {                                              // Play a card
                        if(!player.checkLegality(choice)) {             // Played illegal card
                            System.out.println("That card can't be played. Choose a different one or draw.");
                        }
                        else {                                          // Played legal card
                            Card played = player.getFromPlayerHand(choice);

                            player.removeFromPlayerHand(choice);
                            played.playCard(player.playerSession);

                            if(played.getCardColor() == Card.Color.WILD) {  // Played wild card
                                CLIController.declareWildCardColor(player.playerSession, played);
                            }
                            break;
                        }
                    }
                }
        }
        CLIView.clearScreen();
    }
}
