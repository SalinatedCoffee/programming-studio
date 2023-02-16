/**
 * A <em>SkipCard</em> is a colored <em>Card</em> that skips the next player's turn when played.
 */
package gameOfUno.model;

import gameOfUno.view.CLIView;

public class SkipCard extends Card{
    /**
     * Constructs a default <em>SkipCard</em>.
     */
    public SkipCard() {
        setCardType(Type.SKIP);
    }

    /**
     * Constructs a <em>SkipCard</em> of a given #color.
     * @param _color The color to assign to the <em>SkipCard</em>.
     */
    public SkipCard(Color _color) {
        setCardColor(_color);
        setCardType(Type.SKIP);
    }


    /**
     * Updates game.currentPlayerIndex twice, and adds itself to the #game's discarded pile.
     * @param game The Game object that the card belongs to.
     */
    @Override
    public void playCard(Game game) {
        game.updateCurrentPlayer();
        Player targetPlayer = game.getCurrentPlayer();
        System.out.println("Skipping player " + targetPlayer.playerID + "'s turn.");
        game.updateCurrentPlayer();
        game.updatePlayCounts(getCardColor());
        CLIView.playerPlayedCard(game.getCurrentPlayer(), this);
        game.addCardToDiscarded(this);
    }
}
