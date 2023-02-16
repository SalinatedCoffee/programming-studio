/**
 * The <em>CLIController</em> is a utility class that allows a player to interact with the game.
 */
package gameOfUno.controller;

import gameOfUno.model.*;
import java.util.*;

public class CLIController {
    /**
     * Asks a player to declare a color of a wild card.
     * @param game The game being played.
     * @param card The card being played.
     */
    public static void declareWildCardColor(Game game, Card card) {
        int rawInput;
        Card.Color selectedColor;
        Scanner in = new Scanner(System.in);

        System.out.println("Choose a color to declare.");
        for(int i = 0; i < 4; i++) {
            System.out.print((i + 1) + ") " + game.colorsInDeck[i].label + "  ");
        }
        System.out.println();

        rawInput = in.nextInt();
        selectedColor = game.colorsInDeck[rawInput - 1];

        System.out.println("Declaring Wild card as " + selectedColor.label + ".");

        switch (card.getCardType()) {
            case WILD:
                ((WildCard) card).setDeclaredColor(selectedColor);
                break;
            case WILDDRAWFOUR:
                ((WildDraw4Card) card).setDeclaredColor(selectedColor);
        }
    }

    /**
     * Asks a player to select a card.
     * @return Integer representing player's choice.
     */
    public static int selectCardFromList() {
        int rawInput;
        Scanner in = new Scanner(System.in);

        System.out.print("Select a card to play. (0 to draw a card) ");
        rawInput = in.nextInt();

        return rawInput - 1;
    }

    /**
     * Asks for the number of players and AIs to participate in a game.
     * @return Array of integers each representing the number of entities.
     */
    public static int[] askNumberOfPlayers() {
        int manualPlayers;
        int baselinePlayers;
        int strategicPlayers;
        Scanner in = new Scanner(System.in);

        System.out.print("How many human players? ");
        manualPlayers = in.nextInt();
        System.out.print("How many baseline AIs? ");
        baselinePlayers = in.nextInt();
        System.out.print("How many strategic AIs? ");
        strategicPlayers = in.nextInt();

        return new int[] {manualPlayers, baselinePlayers, strategicPlayers};
    }

    /**
     * Asks the next player to confirm readiness.
     */
    public static void stallForNextPlayer() {
        Scanner in = new Scanner(System.in);

        System.out.print("Waiting for next player. Press Enter when ready.");
        in.nextLine();
    }
}
