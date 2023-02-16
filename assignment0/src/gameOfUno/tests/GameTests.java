package gameOfUno.tests;

import gameOfUno.model.*;
import java.util.*;
import junit.framework.*;

public class GameTests extends TestCase {

    public void testEntityGeneration() {
        Game game = new Game(2, 4, 6);
        int manual = 0;
        int baseline = 0;
        int strategic = 0;

        Player init = game.getCurrentPlayer();

        switch(init.mode) {
            case MANUAL:
                manual++;
                break;
            case BASELINE:
                baseline++;
                break;
            case STRATEGIC:
                strategic++;
        }

        game.updateCurrentPlayer();

        while(init != game.getCurrentPlayer()) {
            switch(game.getCurrentPlayer().mode) {
                case MANUAL:
                    manual++;
                    break;
                case BASELINE:
                    baseline++;
                    break;
                case STRATEGIC:
                    strategic++;
            }
            game.updateCurrentPlayer();
        }

        assertEquals(2, manual);
        assertEquals(4, baseline);
        assertEquals(6, strategic);
    }

    public void testDeckGeneration() {
        Game game = new Game();

        game.colorsInDeck = Card.getListOfColors();
        game.typesInDeck = Card.getListOfTypes();
        game.generateDeck();

        assertEquals(96, game.sizeOfDeck());
    }

    public void testFromDeckToDiscarded() {
        Game game = new Game(2);

        assertEquals(81, game.sizeOfDeck());
        assertEquals(1, game.sizeOfDiscarded());

        game.addCardToDiscarded(game.drawCard());

        assertEquals(80, game.sizeOfDeck());
        assertEquals(2, game.sizeOfDiscarded());

        game.addCardToDiscarded(game.drawCard());

        assertEquals(79, game.sizeOfDeck());
        assertEquals(3, game.sizeOfDiscarded());
    }

    public void testDeckRepopulation() {
        Game game = new Game(2);
        Card.Color discardColor;
        Card.Type discardType;

        assertEquals(81, game.sizeOfDeck());
        assertEquals(1, game.sizeOfDiscarded());

        // Test manual repopulation
        for(int i = 0; i < 20; i++) {
            game.addCardToDiscarded(game.drawCard());
        }

        discardColor = game.getCurrentColor();
        discardType = game.getCurrentType();

        game.repopulateDeck();

        assertEquals(discardColor, game.getCurrentColor());
        assertEquals(discardType, game.getCurrentType());
        assertEquals(81, game.sizeOfDeck());
        assertEquals(1, game.sizeOfDiscarded());

        // Test automatic repopulation
        for(int i = 0; i < 81; i++) {
            game.addCardToDiscarded(game.drawCard());
        }

        assertEquals(80, game.sizeOfDeck());
        assertEquals(2, game.sizeOfDiscarded());
    }

    public void testReverseOrder() {
        Game game = new Game();

        assertEquals(Game.Direction.RIGHT, game.getOrder());

        game.reverseOrder();

        assertEquals(Game.Direction.LEFT, game.getOrder());
    }

    public void testUpdateNextPlayer() {
        Game game = new Game(4);
        int startingPlayerID = game.getCurrentPlayer().playerID;

        game.updateCurrentPlayer();

        assertEquals((startingPlayerID + 1) % 4, game.getCurrentPlayer().playerID);

        game.updateCurrentPlayer();

        assertEquals((startingPlayerID + 2) % 4, game.getCurrentPlayer().playerID);

        game.updateCurrentPlayer();

        assertEquals((startingPlayerID + 3) % 4, game.getCurrentPlayer().playerID);
    }

    public void testPlayerInitialization() {
        Game game = new Game(4);

        for(int i = 0; i < 4; i++) {
            assertEquals(game, game.getCurrentPlayer().playerSession);
            assertEquals(7, game.getCurrentPlayer().remainingCards());
            game.updateCurrentPlayer();
        }

        assertEquals(4, game.getNumberOfPlayers());
    }

    public void testDiscardInfo() {
        Game game = new Game(4);
        NumberCard numberCard = new NumberCard(Card.Color.BLUE, Card.Type.FIVE);
        WildCard wildCard = new WildCard();

        numberCard.playCard(game);

        assertEquals(Card.Color.BLUE, game.getCurrentColor());
        assertEquals(Card.Type.FIVE, game.getCurrentType());

        wildCard.playCard(game);

        assertEquals(Card.Color.RED, game.getCurrentColor());
        assertEquals(Card.Type.WILD, game.getCurrentType());

        wildCard.setDeclaredColor(Card.Color.YELLOW);

        assertEquals(Card.Color.YELLOW, game.getCurrentColor());
    }
}
