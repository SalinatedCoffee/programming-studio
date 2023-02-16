/**
 * Includes UI tests, which will be tested manually.
 */
package gameOfUno.UITests;

import gameOfUno.controller.*;
import gameOfUno.model.*;
import gameOfUno.view.CLIView;

import java.io.IOException;
import java.util.*;

public class UITests {

    public static void main(String[] args) throws IOException {
        // Test suite for CLIView.
        System.out.println("# ======================= Testing CLIView ======================= #");
        // Should report Red 0, Green Wild, and Blue Wild Draw 4 cards played.
        testPlayerPlaysCard();
        System.out.println();
        // Should report list of 6 players, with unshuffled play order.
        testPlayerOrder();
        System.out.println();
        // Should report deck size of 96.
        testCardsInDeck();
        System.out.println();
        // Should report a Yellow Skip, Blue Wild, and Green Wild Draw 4 cards as current cards.
        testCurrentCardInfo();
        System.out.println();
        // Should report a Yellow 5, a Wild Draw 4, and a Wild card.
        testPrintListOfCards();
        System.out.println();
        // Should report playerID of 777.
        testPlayerDraw();
        System.out.println();
        // Should report a Yellow 5 card.
        testDrawCardAnnouncement();
        System.out.println();


        // Test suite for CLIController.
        System.out.println("# ==================== Testing CLIController ==================== #");
        // Test should exit normally with inputs 2, then 3.
        testDeclareWildCardColor();
        System.out.println();
        // Test should exit normally with inputs 1, 3, then 7.
        testSelectCardFromList();
        // Test should exit normally with inputs 3, 6, then 9.
        testNumberOfPlayers();
        // Test should exit normally after hitting return.
        testStallForPlayer();
    }

    public static void testPlayerPlaysCard() {
        Player player = new Player(7);
        NumberCard numberCard = new NumberCard(Card.Color.RED, Card.Type.ZERO);
        WildCard wildCard = new WildCard(Card.Color.GREEN);
        WildDraw4Card wildDraw4Card = new WildDraw4Card(Card.Color.BLUE);

        CLIView.playerPlayedCard(player, numberCard);
        CLIView.playerPlayedCard(player, wildCard);
        CLIView.playerPlayedCard(player, wildDraw4Card);
    }

    public static void testPlayerOrder() {
        Game game = new Game(6);

        CLIView.playerOrder(game);
    }

    public static void testPlayerDraw() {
        Player player = new Player(777);

        CLIView.announceAIDraw(player);
    }

    public static void testCardsInDeck() {
        Game game = new Game();
        game.colorsInDeck = Card.getListOfColors();
        game.typesInDeck = Card.getListOfTypes();
        game.generateDeck();

        CLIView.cardsInDeck(game);
    }

    public static void testCurrentCardInfo() {
        Game game = new Game(2);
        NumberCard numberCard = new NumberCard(Card.Color.YELLOW, Card.Type.SKIP);
        WildCard wildCard = new WildCard();
        WildDraw4Card wildDraw4Card = new WildDraw4Card();

        numberCard.playCard(game);

        CLIView.currentCardInfo(game);

        wildCard.playCard(game);
        wildCard.setDeclaredColor(Card.Color.BLUE);

        CLIView.currentCardInfo(game);

        wildDraw4Card.playCard(game);
        wildDraw4Card.setDeclaredColor(Card.Color.GREEN);

        CLIView.currentCardInfo(game);
    }

    public static void testPrintListOfCards() {
        ArrayList<Card> cards = new ArrayList<Card>();

        cards.add(new NumberCard(Card.Color.YELLOW, Card.Type.FIVE));
        cards.add(new WildDraw4Card());
        cards.add(new WildCard());

        CLIView.printListOfCards(cards);
    }

    public static void testDeclareWildCardColor() {
        Game game = new Game(2);
        WildCard wildCard = new WildCard();
        WildDraw4Card wildDraw4Card = new WildDraw4Card();

        CLIController.declareWildCardColor(game, wildCard);

        assert (Card.Color.YELLOW == wildCard.getDeclaredColor());

        CLIController.declareWildCardColor(game, wildDraw4Card);

        assert (Card.Color.GREEN == wildDraw4Card.getDeclaredColor());
    }

    public static void testSelectCardFromList() {
        int out = CLIController.selectCardFromList();

        assert (0 == out);
        out = CLIController.selectCardFromList();
        assert (2 == out);
        out = CLIController.selectCardFromList();
        assert (6 == out);
    }

    public static void testDrawCardAnnouncement() {
        NumberCard card = new NumberCard(Card.Color.YELLOW, Card.Type.FIVE);

        CLIView.announceDraw(card);
    }

    public static void testNumberOfPlayers() {
        int[] out = CLIController.askNumberOfPlayers();

        assert(out[0] == 3);
        assert(out[1] == 6);
        assert(out[2] == 9);
    }

    public static void testStallForPlayer() {
        CLIController.stallForNextPlayer();
    }
}
