/**
 * A <em>NumberCard</em> is a colored card with a number assigned to it.
 */
package gameOfUno.model;

import gameOfUno.view.CLIView;

public class NumberCard extends Card {
    /**
     * Constructs a default <em>NumberCard</em>.
     */
    public NumberCard() {
    }

    /**
     * Constructs a <em>NumberCard</em> of a given #color and #number.
     * @param _color The color to assign to the <em>NumberCard</em>.
     * @param _number The number to assign to the <em>NumberCard</em>.
     */
    public NumberCard(Color _color, Type _number) {
        setCardColor(_color);
        setCardType(_number);
    }


    /**
     * Adds itself to the #game's discarded pile,
     * and updates game.currentPlayerIndex to the next player.
     * @param game The Game object that the card belongs to.
     */
    @Override
    public void playCard (Game game) {
        game.addCardToDiscarded(this);
        game.updatePlayCounts(getCardColor());
        CLIView.playerPlayedCard(game.getCurrentPlayer(), this);
        game.updateCurrentPlayer();
    }
}
