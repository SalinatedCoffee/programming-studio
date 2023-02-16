package gameOfUno.tests;

import gameOfUno.model.*;

import java.util.*;
import junit.framework.*;

public class PlayerTests extends TestCase {

    public void testConstructors() {
        ArrayList<Card> cards = new ArrayList<Card>();
        cards.add(new NumberCard());
        Game game = new Game();
        Player defaultPlayer = new Player();
        Player customPIDPlayer = new Player(21);
        Player customPlayer = new Player(7, game, cards);
        Player customAIPlayer = new Player(9, game, cards, Player.Mode.BASELINE);

        assertEquals(-1, defaultPlayer.playerID);
        assertNull(defaultPlayer.playerSession);
        assertEquals(0, defaultPlayer.remainingCards());
        assertEquals(21, customPIDPlayer.playerID);
        assertEquals(7, customPlayer.playerID);
        assertEquals(game, customPlayer.playerSession);
        assertEquals(1, customPlayer.remainingCards());
        assertEquals(Player.Mode.BASELINE, customAIPlayer.mode);
    }

    public void testCardLegality() {
        Game game = new Game();
        Player player = new Player();
        player.playerSession = game;
        NumberCard card = new NumberCard(Card.Color.RED, Card.Type.ZERO);
        NumberCard falseCard = new NumberCard(Card.Color.BLUE, Card.Type.ONE);
        WildCard wildCard = new WildCard();
        Card.Color[] colors = Card.getListOfColors();
        Card.Type[] types = Card.getListOfTypes();

        game.addCardToDiscarded(card);
        player.addToPlayerHand(card);

        assertTrue(player.checkLegality(0));

        player.addToPlayerHand(falseCard);
        assertFalse(player.checkLegality(1));

        player.addToPlayerHand(wildCard);

        for(Card.Color c : colors) {
            for(Card.Type t : types) {
                assertTrue(player.checkLegality(2));
            }
        }
    }

    public void testPlayerHandInfo() {
        Player player = new Player();
        NumberCard card = new NumberCard();

        assertTrue(player.checkForWin());

        player.addToPlayerHand(card);

        assertEquals(1, player.remainingCards());
        assertFalse(player.checkForWin());
    }
}
