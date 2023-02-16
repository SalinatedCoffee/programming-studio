package gameOfUno.tests;

import gameOfUno.model.*;
import java.util.*;
import junit.framework.*;

public class NumberCardTests extends TestCase {

    public void testConstructors() {
        NumberCard defaultCard = new NumberCard();
        NumberCard customCard = new NumberCard(Card.Color.YELLOW, Card.Type.SEVEN);

        assertEquals(Card.Color.NOCOLOR, defaultCard.getCardColor());
        assertEquals(Card.Type.NOTYPE, defaultCard.getCardType());
        assertEquals(Card.Color.YELLOW, customCard.getCardColor());
        assertEquals(Card.Type.SEVEN, customCard.getCardType());
    }

    public void testBehavior() {
        Game game = new Game(4);
        NumberCard numberCard = new NumberCard();
        ArrayList<Player> players = new ArrayList<Player>();

        for(int i = 0; i < 4; i++) {
            players.add(game.getCurrentPlayer());
            game.updateCurrentPlayer();
        }

        numberCard.playCard(game);

        assertEquals(players.get(1), game.getCurrentPlayer());

        game.updateCurrentPlayer();
        game.updateCurrentPlayer();
        numberCard.playCard(game);

        assertEquals(players.get(0), game.getCurrentPlayer());
    }
}
