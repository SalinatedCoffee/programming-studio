package gameOfUno.tests;

import gameOfUno.model.*;
import java.util.*;
import junit.framework.*;

public class ReverseCardTests extends TestCase {

    public void testConstructors() {
        ReverseCard defaultCard = new ReverseCard();
        ReverseCard customCard = new ReverseCard(Card.Color.BLUE);

        assertEquals(Card.Color.NOCOLOR, defaultCard.getCardColor());
        assertEquals(Card.Type.REVERSE, defaultCard.getCardType());
        assertEquals(Card.Color.BLUE, customCard.getCardColor());
        assertEquals(Card.Type.REVERSE, customCard.getCardType());
    }

    public void testBehavior() {
        Game game = new Game(4);
        ReverseCard reverseCard = new ReverseCard();
        ArrayList<Player> players = new ArrayList<Player>();

        for(int i = 0; i < 4; i++) {
            players.add(game.getCurrentPlayer());
            game.updateCurrentPlayer();
        }

        reverseCard.playCard(game);

        assertEquals(players.get(3), game.getCurrentPlayer());

        game.updateCurrentPlayer();
        game.updateCurrentPlayer();

        assertEquals(players.get(1), game.getCurrentPlayer());

        reverseCard.playCard(game);

        assertEquals(players.get(2), game.getCurrentPlayer());

        game.updateCurrentPlayer();

        assertEquals(players.get(3), game.getCurrentPlayer());
    }
}
