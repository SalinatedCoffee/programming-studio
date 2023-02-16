/**
 * A <em>Player</em> keeps track of various player-related data - including, but
 * not limited to, the players on either side of them, the Game the player
 * is currently participating in, and the cards in the player's hand.
 */
package gameOfUno.model;

import gameOfUno.view.*;
import java.util.*;

public class Player {
    /**
     * Enum of available player modes.
     */
    public enum Mode {
        BASELINE,   // Baseline AI; plays legal cards at random.
        STRATEGIC,  // Strategic AI; plays aggressively by playing draw cards and most popular colors first.
        MANUAL      // No AI
    }


    /**
     * Holds the unique ID of a player.
     */
    public int playerID = -1;
    /**
     * Holds a reference to the game the player is participating in.
     */
    public Game playerSession = null;
    /**
     * Holds the cards in the player's hand.
     */
    private ArrayList<Card> playerHand = new ArrayList<Card>();
    /**
     * Holds the mode of the player.
     */
    public Mode mode = Mode.MANUAL;


    /**
     * Constructs a default <em>Player</em>.
     */
    public Player() {
    }

    /**
     * Constructs a <em>Player</em> with given #playerID.
     * @param _playerID The unique ID to give the player.
     */
    public Player(int _playerID) {
        playerID = _playerID;
    }


    /**
     * Constructs a <em>Player</em> with given #playerID, #playerSession, and #playerHand.
     * @param _playerID The unique ID to give the player.
     * @param _playerSession The game the player is participating in.
     * @param _playerHand The cards to give to a player's hand.
     */
    public Player(int _playerID, Game _playerSession, ArrayList<Card> _playerHand) {
        playerID = _playerID;
        playerSession = _playerSession;
        playerHand = _playerHand;
    }

    /**
     * Constructs a <em>Player</em> with given #playerID, #playerSession, #playerHand, and #mode.
     * @param _playerID The unique ID to give the player.
     * @param _playerSession The game the player is participating in.
     * @param _playerHand The cards to give to a player's hand.
     * @param _mode The operating mode to give the player.
     */
    public Player(int _playerID, Game _playerSession, ArrayList<Card> _playerHand, Mode _mode) {
        playerID = _playerID;
        playerSession = _playerSession;
        playerHand = _playerHand;
        mode = _mode;
    }


    /**
     * Adds a card to the player's hand.
     * @param newCard The card to add to the player's hand.
     */
    public void addToPlayerHand(Card newCard) {
        playerHand.add(newCard);
    }

    /**
     * Removes a card from the player's hand.
     * @param cardIdx The index of the card to be removed.
     */
    public void removeFromPlayerHand(int cardIdx) { playerHand.remove(cardIdx); }

    /**
     * Gets a card from the player's hand.
     * @param cardIdx The index of the card to be retrieved.
     * @return Reference of card at index cardIdx.
     */
    public Card getFromPlayerHand(int cardIdx) { return playerHand.get(cardIdx); }

    /**
     * Checks whether a player has exhausted their hand.
     * @return True if a player has no cards in their hand, false if otherwise.
     */
    public boolean checkForWin() {
        if(playerHand.size() == 0) {
            return true;
        }
        return false;
    }

    /**
     * Gets the number of remaining cards in the player's hand.
     * @return The number of cards in the player'a hand.
     */
    public int remainingCards() {
        return playerHand.size();
    }

    /**
     * Baseline AI that chooses a legal card at random.
     */
    public void baselineAITurn() {
        ArrayList<Integer> legalIdx = new ArrayList<Integer>();

        for(int i = 0; i < playerHand.size(); i++) {    // Check hand for all playable cards
            if(checkLegality(i)) {
                legalIdx.add(i);
            }
        }

        if(legalIdx.size() == 0) {  // If none was found, draw and attempt to play that card
            playerHand.add(playerSession.drawCard());
            CLIView.announceAIDraw(this);
            if(checkLegality(playerHand.size() - 1)) {
                baselineAIPlayCard(playerHand.remove(playerHand.size() - 1));
            }
            playerSession.updateCurrentPlayer();
        }
        else {  // If playable cards exist, select a card to play at random
            int targetIdx = legalIdx.get(playerSession.gameRng.nextInt(legalIdx.size()));
            baselineAIPlayCard(playerHand.remove(targetIdx));
        }
    }

    /**
     * Plays a given card according to baseline AI behavior. Wild card colors will be declared randomly.
     * @param card The card being played by a baseline AI.
     */
    private void baselineAIPlayCard(Card card) {

        if(card.getCardType() == Card.Type.WILDDRAWFOUR) {
            card.playCard(playerSession);
            ((WildDraw4Card) card).setDeclaredColor(playerSession
                                                    .colorsInDeck[playerSession.gameRng.nextInt(4)]);
            CLIView.playerPlayedCard(this, card);
        }
        else if(card.getCardType() == Card.Type.WILD) {
            card.playCard(playerSession);
            ((WildCard) card).setDeclaredColor(playerSession.colorsInDeck[playerSession.gameRng.nextInt(4)]);
            CLIView.playerPlayedCard(this, card);
        }
        else {
            card.playCard(playerSession);
        }
    }

    /**
     * Strategic AI that plays aggressively by first prioritizing draw cards, then popular colored cards.
     */
    public void strategicAITurn() {
        ArrayList<Integer> legalIdx = new ArrayList<Integer>();
        int temp = 0;
        int maxIdx = -1;
        Card.Color popular;

        for(int i = 0; i < 4; i++) {    // Get most played color
            if(playerSession.playCount[i] >= temp) {
                temp = playerSession.playCount[i];
                maxIdx = i;
            }
        }

        if(temp == -1) {
            popular = playerSession.colorsInDeck[playerSession.gameRng.nextInt(4)];
        }
        else {
            popular = playerSession.colorsInDeck[maxIdx];
        }

        for(int i = 0; i < playerHand.size(); i++) {    // Check hand for all playable cards
            if(checkLegality(i)) {
                legalIdx.add(i);
            }
        }

        if(legalIdx.size() == 0) {  // If none was found, draw and attempt to play that card
            playerHand.add(playerSession.drawCard());
            CLIView.announceAIDraw(this);
            if(checkLegality(playerHand.size() - 1)) {  // Drawn card is legal
                strategicAIPlayCard(playerHand.remove(playerHand.size() - 1), popular);
            }
            playerSession.updateCurrentPlayer();
        }
        else {  // If playable cards exist, select a card to play based on strategic priorities
            strategicAISelectCard(legalIdx, popular);
        }
    }

    /**
     * Selects a card according to the strategic AI.
     * @param candidates Indices of cards in the player's hand that can be played.
     * @param popular The most played color in the game.
     */
    private void strategicAISelectCard(ArrayList<Integer> candidates, Card.Color popular) {

        for(Integer i : candidates) {   // Prioritize draw 4, draw 2, then popular colored cards, in that order
            Card candidateCard = playerHand.get(i.intValue());
            if(candidateCard.getCardType() == Card.Type.WILDDRAWFOUR) {
                strategicAIPlayCard(playerHand.remove(i.intValue()), popular);
                return;
            }
            if(candidateCard.getCardType() == Card.Type.DRAWTWO) {
                strategicAIPlayCard(playerHand.remove(i.intValue()), popular);
                return;
            }
            if(candidateCard.getCardColor() == popular) {
                strategicAIPlayCard(playerHand.remove(i.intValue()), popular);
                return;
            }
        }

        // If there are no cards to prioritize, play at random
        int targetIdx = candidates.get(playerSession.gameRng.nextInt(candidates.size()));
        strategicAIPlayCard(playerHand.remove(targetIdx), popular);
    }

    /**
     * Plays a card according to the strategic AI. Declares wild card colors as the most played color.
     * @param card The card being played by a strategic AI.
     * @param popular The most popular color in the current game.
     */
    private void strategicAIPlayCard(Card card, Card.Color popular) {

        if(card.getCardType() == Card.Type.WILDDRAWFOUR) {
            card.playCard(playerSession);
            ((WildDraw4Card) card).setDeclaredColor(popular);
            CLIView.playerPlayedCard(this, card);
        }
        else if(card.getCardType() == Card.Type.WILD) {
            card.playCard(playerSession);
            ((WildCard) card).setDeclaredColor(popular);
            CLIView.playerPlayedCard(this, card);
        }
        else {
            card.playCard(playerSession);
        }
    }

    /**
     * Checks whether a card is able to be played.
     * @param targetCardIndex The index of the card in #playerHand to be checked.
     * @return True if the card can be played, false otherwise.
     */
    public boolean checkLegality(int targetCardIndex) {
        assert (targetCardIndex >= 0) && (targetCardIndex < playerHand.size());
        Card.Color currentColor = playerSession.getCurrentColor();
        Card.Type currentType = playerSession.getCurrentType();

        Card targetCard = playerHand.get(targetCardIndex);

        if(targetCard.getCardColor() == Card.Color.WILD) {
            return true;
        }
        else if(targetCard.getCardColor() == currentColor) {
            return true;
        }
        else if(targetCard.getCardType() == currentType) {
            return true;
        }

        return false;
    }

    /**
     * Requests the view to print out the contents of the player's hand.
     */
    public void showContentsOfHand() {
        CLIView.printListOfCards(playerHand);
    }
}