package gameOfUno.tests;

import gameOfUno.model.*;
import java.util.*;
import junit.framework.*;

public class WildDraw4CardTests extends TestCase {

    public void testConstructors() {
        WildDraw4Card defaultCard = new WildDraw4Card();
        WildDraw4Card customCard = new WildDraw4Card(Card.Color.YELLOW);

        assertEquals(Card.Color.WILD, defaultCard.getCardColor());
        assertEquals(Card.Type.WILDDRAWFOUR, defaultCard.getCardType());
        assertEquals(Card.Color.WILD, customCard.getCardColor());
        assertEquals(Card.Type.WILDDRAWFOUR, customCard.getCardType());
        assertEquals(Card.Color.YELLOW, customCard.getDeclaredColor());
    }

    public void testBehavior() {
        Game game = new Game(4);
        WildDraw4Card wildDraw4Card = new WildDraw4Card();
        ArrayList<Player> players = new ArrayList<Player>();

        for(int i = 0; i < 4; i++) {
            players.add(game.getCurrentPlayer());
            game.updateCurrentPlayer();
        }

        wildDraw4Card.playCard(game);

        assertEquals(Card.Color.RED, wildDraw4Card.getDeclaredColor());

        int maxNumCards = 0;
        for (Player p : players) {
            if(maxNumCards < p.remainingCards()) {
                maxNumCards = p.remainingCards();
            }
        }

        assertEquals(11, maxNumCards);

        wildDraw4Card.setDeclaredColor(Card.Color.GREEN);

        assertEquals(Card.Color.GREEN, wildDraw4Card.getDeclaredColor());
    }
}
