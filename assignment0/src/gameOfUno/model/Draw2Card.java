/**
 * A <em>Draw2Card</em> is a colored <em>Card</em> that makes the next player draw two cards and skip when played.
 */
package gameOfUno.model;

import gameOfUno.view.CLIView;

public class Draw2Card extends Card{
    /**
     * Constructs a default <em>Draw2Card</em>.
     */
    public Draw2Card() {
        setCardType(Type.DRAWTWO);
    }

    /**
     * Constructs a <em>Draw2Card</em> of a given #color.
     * @param _color The color to assign to the <em>Draw2Card</em>.
     */
    public Draw2Card(Color _color) {
        setCardColor(_color);
        setCardType(Type.DRAWTWO);
    }


    /**
     * Updates game.currentPlayerIndex to the next player, adds two cards to that player's hand,
     * adds itself to the #game's discarded pile, and updates game.currentPlayerIndex to the next player.
     * @param game The Game object that the card belongs to.
     */
    @Override
    public void playCard(Game game) {
        Player targetPlayer;

        game.updateCurrentPlayer();
        targetPlayer = game.getCurrentPlayer();
        System.out.println("Giving player " + targetPlayer.playerID + " 2 cards from the deck.");
        targetPlayer.addToPlayerHand(game.drawCard());
        targetPlayer.addToPlayerHand(game.drawCard());
        game.addCardToDiscarded(this);
        game.updatePlayCounts(getCardColor());
        CLIView.playerPlayedCard(game.getCurrentPlayer(), this);
        game.updateCurrentPlayer();
    }
}
