package gameOfUno.tests;

import gameOfUno.model.*;
import junit.framework.*;

public class CardTests extends TestCase {

    public void testColorEnumToArray() {
        Card.Color[] expected = {Card.Color.RED, Card.Color.YELLOW, Card.Color.GREEN, Card.Color.BLUE,
                Card.Color.WILD, Card.Color.NOCOLOR};
        Card.Color[] got = Card.getListOfColors();

        for(int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], got[i]);
        }
    }

    public void testTypeEnumToArray() {
        Card.Type[] expected = {Card.Type.ZERO, Card.Type.ONE, Card.Type.TWO, Card.Type.THREE, Card.Type.FOUR,
                Card.Type.FIVE, Card.Type.SIX, Card.Type.SEVEN, Card.Type.EIGHT, Card.Type.NINE,
                Card.Type.SKIP, Card.Type.REVERSE, Card.Type.DRAWTWO, Card.Type.WILD,
                Card.Type.WILDDRAWFOUR};
        Card.Type[] got = Card.getListOfTypes();

        for(int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], got[i]);
        }
    }

}
