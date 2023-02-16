/**
 * The <em>CLIView</em> is a utility class that provides various methods that displays information
 * in a CLI.
 */
package gameOfUno.view;

import gameOfUno.model.*;
import java.util.*;

import static gameOfUno.model.Card.Color.WILD;

public class CLIView {
    /**
     * Announces a card played by a player.
     * @param player The player playing the card.
     * @param played The card that was just played.
     */
    public static void playerPlayedCard(Player player, Card played) {
        System.out.print("Player " + player.playerID + " played a " + played.getCardColor().label
                         + " " + played.getCardType().label + " card.");
        if(played.getCardColor() == WILD) {
            if(played.getCardType() == Card.Type.WILD) {
                System.out.println(" The declared color is "
                        + ((WildCard) played).getDeclaredColor().label + ".");
            }
            else {
                System.out.println(" The declared color is "
                        + ((WildDraw4Card) played).getDeclaredColor().label + ".");
            }
        }
        else {
            System.out.println();
        }
    }

    /**
     * Announces the total number of players in the game, and the current play order.
     * @param game The game of which the play order will be announced.
     */
    public static void playerOrder(Game game) {
        System.out.print("The current play order is ");

        for(int i = 0; i < game.getNumberOfPlayers(); i++) {
            System.out.print("player " + game.getCurrentPlayer().playerID + ", ");
            game.updateCurrentPlayer();
        }
        System.out.println();
    }

    /**
     * Announces that a player had drawn a card.
     * @param player The player that drew a card.
     */
    public static void announceAIDraw(Player player) {
        System.out.println("Player " + player.playerID + " drew a card.");
    }

    /**
     * Announces the number of cards remaining in the deck.
     * @param game The game of which the deck card count will be announced.
     */
    public static void cardsInDeck(Game game) {
        System.out.println("There are " + game.sizeOfDeck() + " cards remaining in the deck.");
    }

    /**
     * Announces the color and type of the top-most card on the discarded pile.
     * @param game The game of which the recent discarded information will be announced.
     */
    public static void currentCardInfo(Game game) {
        Card.Color currentColor = game.getCurrentColor();
        Card.Type currentType = game.getCurrentType();

        switch (currentType) {
            case WILD:
                System.out.println("The current card is a Wild card, with a declared color of "
                                   + currentColor.label);
                break;
            case WILDDRAWFOUR:
                System.out.println("The current card is a Wild Draw 4 card, with a declared color of "
                                   + currentColor.label);
                break;
            default:
                System.out.println("The current card is a " + currentColor.label + " "
                                   + currentType.label + " card.");

        }
    }

    /**
     * Prints the contents of an ArrayList of cards in a numbered list.
     * @param cards The ArrayList of which the contents will be printed.
     */
    public static void printListOfCards(ArrayList<Card> cards) {
        Card temp;

        System.out.println("The cards currently in your hand are:");
        for(int i = 0; i < cards.size(); i++) {
            System.out.print((i + 1) + ") ");
            temp = cards.get(i);
            switch (temp.getCardType()) {
                case WILD:
                    System.out.print("[Wild]  ");
                    break;
                case WILDDRAWFOUR:
                    System.out.print("[Wild Draw 4]  ");
                    break;
                default:
                    System.out.print("[" + temp.getCardColor().label + " "
                                     + temp.getCardType().label + "]  ");
            }
        }
        System.out.println();
    }

    /**
     * Announces the card that was drawn.
     * @param card The card that was drawn from the deck.
     */
    public static void announceDraw(Card card) {
            System.out.println("You drew a " + card.getCardColor().label + " " + card.getCardType().label
                               + " card.");
    }

    /**
     * Clears the console using ANSI escape codes. Will only work in linux-like terminals.
     * ACKNOWLEDGEMENTS: code taken from https://stackoverflow.com/questions/2979383/java-clear-the-console
     */
    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}
