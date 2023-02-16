package gameOfUno.tests;

import gameOfUno.model.*;
import java.util.*;
import junit.framework.*;

public class SkipCardTests extends TestCase {

    public void testConstructors() {
        SkipCard defaultCard = new SkipCard();
        SkipCard customCard = new SkipCard(Card.Color.RED);

        assertEquals(Card.Color.NOCOLOR, defaultCard.getCardColor());
        assertEquals(Card.Type.SKIP, defaultCard.getCardType());
        assertEquals(Card.Color.RED, customCard.getCardColor());
        assertEquals(Card.Type.SKIP, customCard.getCardType());
    }

    public void testBehavior() {
        Game game = new Game(4);
        SkipCard skipCard = new SkipCard();
        ArrayList<Player> players = new ArrayList<Player>();

        for(int i = 0; i < 4; i++) {
            players.add(game.getCurrentPlayer());
            game.updateCurrentPlayer();
        }

        skipCard.playCard(game);

        assertEquals(players.get(2), game.getCurrentPlayer());

        skipCard.playCard(game);

        assertEquals(players.get(0), game.getCurrentPlayer());
    }
}
