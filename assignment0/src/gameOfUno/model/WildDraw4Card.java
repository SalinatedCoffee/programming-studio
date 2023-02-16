/**
 * A <em>WildDraw4Card</em> is a wild <em>Card</em> that makes the next player draw four cards, skips
 * their turn, and allows the player playing the card to declare a color for the card.
 * New behavior for Assignment 1.1: Playing a <em>WildDraw4Card</em> also shuffles the play order.
 */
package gameOfUno.model;

public class WildDraw4Card extends Card {
    /**
     * Holds the declared color of the <em>WildDraw4Card</em>.
     */
    private Color declaredColor = Color.NOCOLOR;


    /**
     * Constructs a default <em>WildDraw4Card</em>.
     */
    public WildDraw4Card() {
        setCardColor(Color.WILD);
        setCardType(Type.WILDDRAWFOUR);
    }

    /**
     * Constructs a <em>WildDraw4Card</em> with given #declaredColor.
     * @param _declaredColor The declared color to assign to the <em>WildDraw4Card</em>
     */
    public WildDraw4Card(Color _declaredColor) {
        setCardColor(Color.WILD);
        setCardType(Type.WILDDRAWFOUR);
        declaredColor = _declaredColor;
    }

    /**
     * Sets the declared color.
     * @param newDeclaredColor The color being declared.
     */
    public void setDeclaredColor(Color newDeclaredColor) {
        declaredColor = newDeclaredColor;
    }

    /**
     * Gets the declared color.
     * @return The declared color.
     */
    public Color getDeclaredColor() {
        return declaredColor;
    }

    /**
     * Updates game.currentPlayerIndex to the next player, adds four cards to that player's hand,
     * adds itself to the #game's discarded pile, and shuffles the current play order.
     * The color should be declared here.
     * @param game The Game object that the card belongs to.
     */
    @Override
    public void playCard(Game game) {
        Player targetPlayer;

        game.updateCurrentPlayer();
        targetPlayer = game.getCurrentPlayer();
        System.out.println("Giving player " + targetPlayer.playerID + " 2 cards from the deck.");
        for(int i = 0; i < 4; i++) {
            targetPlayer.addToPlayerHand(game.drawCard());
        }
        this.setDeclaredColor(Card.Color.RED); // Set card to Red by default
        game.addCardToDiscarded(this);
        game.shuffleOrder();
    }
}
