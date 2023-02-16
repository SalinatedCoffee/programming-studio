/**
 * A <em>WildCard</em> is a <em>Card</em> that can take on any color when it is played.
 * New behavior for Assignment 1.1: Playing a <em>WildCard</em> also shuffles the play order.
 */
package gameOfUno.model;

public class WildCard extends Card {
    /**
     * Holds the declared color of the wild card.
     */
    private Color declaredColor = Color.NOCOLOR;


    /**
     * Constructs a default <em>WildCard</em>.
     */
    public WildCard() {
        setCardColor(Color.WILD);
        setCardType(Type.WILD);
    }

    /**
     * Constructs a <em>WildCard</em> with given #declaredColor.
     * @param _declaredColor The declared color to assign to the <em>WildCard</em>.
     */
    public WildCard(Color _declaredColor) {
        setCardColor(Color.WILD);
        setCardType(Type.WILD);
        declaredColor = _declaredColor;
    }


    /**
     * Sets the declared color .
     * @param newDeclaredColor The color being declared.
     */
    public void setDeclaredColor(Color newDeclaredColor) {
        declaredColor = newDeclaredColor;
    }

    /**
     * Gets the declared color.
     * @return The declared color of a wild card.
     */
    public Color getDeclaredColor() {
        return declaredColor;
    }

    /**
     * Adds itself to the #game's discarded pile, and shuffles the current play order.
     * The color should be declared here.
     * @param game The Game object that the card belongs to.
     */
    @Override
    public void playCard(Game game) {
        this.setDeclaredColor(Card.Color.RED); // Set card to Red by default
        game.addCardToDiscarded(this);
        game.shuffleOrder();
    }
}
