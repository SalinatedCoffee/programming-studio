/**
 * A <em>Card</em> represents a card that can be played in the game.
 * Deck specifications are defined here.
 */
package gameOfUno.model;

public abstract class Card {
    /**
     * Enumeration representing all possible colors in a deck. Note that wild cards are
     * considered to be of a separate color.
     */
    public enum Color {
        RED("Red"),
        YELLOW("Yellow"),
        GREEN("Green"),
        BLUE("Blue"),
        WILD("Wild"),
        NOCOLOR("No color");

        public final String label;

        private Color(String label) {
            this.label = label;
        }
    }

    /**
     * Enumeration representing all possible types of cards.
     */
    public enum Type {
        ZERO("0"),
        ONE("1"),
        TWO("2"),
        THREE("3"),
        FOUR("4"),
        FIVE("5"),
        SIX("6"),
        SEVEN("7"),
        EIGHT("8"),
        NINE("0"),
        SKIP("Skip"),
        REVERSE("Reverse"),
        DRAWTWO("Draw 2"),
        WILD(""),
        WILDDRAWFOUR("Draw 4"),
        NOTYPE("No type");

        public final String label;

        private Type(String label) {
            this.label = label;
        }
    }

    /**
     * Holds the card's color.
     */
    private Color cardColor = Color.NOCOLOR;
    /**
     * Holds the card's type.
     */
    private Type cardType = Type.NOTYPE;

    /**
     * Sets a card's color.
     * @param newColor The color to give a card.
     */
    public void setCardColor(Color newColor) {
        cardColor = newColor;
    }

    /**
     * Gets a card's color.
     * @return The color of a card.
     */
    public Color getCardColor() {
        return cardColor;
    }

    /**
     * Sets a card's type.
     * @param newType The type to give a card.
     */
    public void setCardType(Type newType) {
        cardType = newType;
    }

    /**
     * Gets a card's type.
     * @return The type of a card.
     */
    public Type getCardType() {
        return cardType;
    }

    /**
     * Constructs an array of the #Color enumeration.
     * @return The array containing all items in the #Color enumeration.
     */
    public static Color[] getListOfColors() {
        return Color.values();
    }

    /**
     * Constructs an array of the #Type enumeration.
     * @return The array containing all items in the #Type enumeration.
     */
    public static Type[] getListOfTypes() {
        return Type.values();
    }

    /**
     * Play a card and perform appropriate actions based on what card was played.
     * Updates game.currentPlayerIndex before returning.
     * @param game The Game object that the card belongs to.
     */
    public abstract void playCard(Game game);
}
