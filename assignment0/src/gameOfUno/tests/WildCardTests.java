package gameOfUno.tests;

import gameOfUno.model.*;
import java.util.*;
import junit.framework.*;

public class WildCardTests extends TestCase {

    public void testConstructors() {
        WildCard defaultCard = new WildCard();
        WildCard customCard = new WildCard(Card.Color.RED);

        assertEquals(Card.Color.WILD, defaultCard.getCardColor());
        assertEquals(Card.Type.WILD, defaultCard.getCardType());
        assertEquals(Card.Color.WILD, customCard.getCardColor());
        assertEquals(Card.Type.WILD, customCard.getCardType());
        assertEquals(Card.Color.RED, customCard.getDeclaredColor());
    }

    public void testBehavior() {
        Game game = new Game(4);
        WildCard wildCard = new WildCard();
        ArrayList<Player> players = new ArrayList<Player>();

        for(int i = 0; i < 4; i++) {
            players.add(game.getCurrentPlayer());
            game.updateCurrentPlayer();
        }

        wildCard.playCard(game);

        assertEquals(Card.Color.RED, wildCard.getDeclaredColor());

        wildCard.setDeclaredColor(Card.Color.BLUE);

        assertEquals(Card.Color.BLUE, wildCard.getDeclaredColor());
    }
}
