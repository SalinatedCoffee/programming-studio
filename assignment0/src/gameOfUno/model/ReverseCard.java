/**
 * A <em>ReverseCard</em> is a colored <em>Card</em> that reverses the play order when played.
 */
package gameOfUno.model;

import gameOfUno.view.CLIView;

public class ReverseCard extends Card{
    /**
     * Constructs a default <em>ReverseCard</em>.
     */
    public ReverseCard() {
        setCardType(Type.REVERSE);
    }

    /**
     * Constructs a <em>ReverseCard</em> of a given #color.
     * @param _color The color to assign to the <em>ReverseCard</em>.
     */
    public ReverseCard(Color _color) {
        setCardColor(_color);
        setCardType(Type.REVERSE);
    }


    /**
     * Reverses the current play order, adds itself to the #game's discarded pile,
     * and updates game.currentPlayerIndex to the next player.
     * @param game The Game object that the card belongs to.
     */
    @Override
    public void playCard(Game game) {
        game.reverseOrder();
        game.addCardToDiscarded(this);
        game.updatePlayCounts(getCardColor());
        CLIView.playerPlayedCard(game.getCurrentPlayer(), this);
        game.updateCurrentPlayer();
    }
}
