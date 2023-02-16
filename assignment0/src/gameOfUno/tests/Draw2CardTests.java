package gameOfUno.tests;

import gameOfUno.model.*;
import java.util.*;
import junit.framework.*;

public class Draw2CardTests extends TestCase {

    public void testConstructors() {
        Draw2Card defaultCard = new Draw2Card();
        Draw2Card customCard = new Draw2Card(Card.Color.GREEN);

        assertEquals(Card.Color.NOCOLOR, defaultCard.getCardColor());
        assertEquals(Card.Type.DRAWTWO, defaultCard.getCardType());
        assertEquals(Card.Color.GREEN, customCard.getCardColor());
        assertEquals(Card.Type.DRAWTWO, customCard.getCardType());
    }

    public void testBehavior() {
        Game game = new Game(4);
        Draw2Card draw2Card = new Draw2Card();
        ArrayList<Player> players = new ArrayList<Player>();

        for(int i = 0; i < 4; i++) {
            players.add(game.getCurrentPlayer());
            game.updateCurrentPlayer();
        }

        draw2Card.playCard(game);

        assertEquals(players.get(2), game.getCurrentPlayer());
        assertEquals(9, players.get(1).remainingCards());
    }
}
