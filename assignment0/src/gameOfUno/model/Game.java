/**
 * A <em>Game</em> keeps track of various game-related states - including, but
 * not limited to, the players participating in the game, cards in the deck,
 * cards in the discarded pile, order of players, and the current turn's player.
 * It also provides methods that allow players to interact with the game state.
 */
package gameOfUno.model;

import gameOfUno.view.CLIView;

import java.util.*;

public class Game {
    /**
     * Enum representing possible play orders of the game.
     */
    public enum Direction {
        LEFT,
        RIGHT
    }


    /**
     * Holds the cards in the deck.
     */
    private ArrayList<Card> deck = new ArrayList<Card>();
    /**
     * Holds the cards in the discarded pile. The card at the last index is considered
     * to be on the very top of the pile.
     */
    private ArrayList<Card> discarded = new ArrayList<Card>();
    /**
     * Holds the participating players.
     */
    private ArrayList<Player> players = new ArrayList<Player>();
    /**
     * Keeps track of total number of each colors played.
     */
    public int[] playCount = {0, 0, 0, 0};
    /**
     * Holds all possible colors of cards.
     */
    public Card.Color[] colorsInDeck = null;
    /**
     * Holds all possible types of cards.
     */
    public Card.Type[] typesInDeck = null;
    /**
     * Holds index of current player in ArrayList players.
     */
    private int currentPlayerIndex = 0;
    /**
     * Holds the current play order.
     */
    private Direction order = Direction.RIGHT;
    /**
     * Holds the pRNG used in a <em>Game</em>.
     */
    public final Random gameRng;


    /**
     * Constructs a default <em>Game</em>.
     */
    public Game() {
        gameRng = new Random();
    }

    /**
     * Constructs and initializes a <em>Game</em> with given #numberOfPlayers.
     * @param numberOfPlayers The number of players participating in the game.
     *
     */
    public Game(int numberOfPlayers) {
        assert (numberOfPlayers >= 2);

        // Get deck specifications
        colorsInDeck = Card.getListOfColors();
        typesInDeck = Card.getListOfTypes();
        gameRng = new Random();

        generateDeck();
        shuffleDeck();
        generatePlayers(numberOfPlayers);
        initializePlayers();
        initializeGame();
    }

    /**
     * Constructs and initializes a <em>Game</em> with given #manual, #baseline, and #strategic.
     * @param manual The number of human players participating in the game.
     * @param baseline The number of baseline AIs participating in the game.
     * @param strategic The number of strategic AIs participating in the game.
     */
    public Game(int manual, int baseline, int strategic) {
        assert (manual + baseline + strategic >= 2);

        colorsInDeck = Card.getListOfColors();
        typesInDeck = Card.getListOfTypes();
        gameRng = new Random();

        generateDeck();
        shuffleDeck();
        generateEntities(manual, baseline, strategic);
        initializePlayers();
        initializeGame();
    }


    /**
     * Gets the color of the card at the top of the discarded pile.
     * @return The color for a colored card, or the declared color for a wild card.
     */
    public Card.Color getCurrentColor() {
        Card currentCard = discarded.get(discarded.size() - 1);

        if(currentCard.getCardColor() == Card.Color.WILD) {
            switch (currentCard.getCardType()) {
                case WILD:
                    return ((WildCard) currentCard).getDeclaredColor();
                case WILDDRAWFOUR:
                    return ((WildDraw4Card) currentCard).getDeclaredColor();
            }

        }

        return currentCard.getCardColor();
    }

    /**
     * Update currentPlayerIndex according to the game order.
     * Increments by 1 if game order is towards the right, decrements by 1 if otherwise.
     * Index is wrapped around the range of [0, players.size() - 1].
     */
    public void updateCurrentPlayer() {
        if(order == Direction.RIGHT) {
            currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        }
        else {
            if(currentPlayerIndex == 0) {
                currentPlayerIndex = players.size() - 1;
            }
            else {
                currentPlayerIndex--;
            }
        }
    }

    /**
     * Gets the player at their current turn.
     * @return The Player object of the player at their current turn.
     */
    public Player getCurrentPlayer() {
        Player player = players.get(currentPlayerIndex);
        return player;
    }

    /**
     * Gets the total number of players participating in the game.
     * @return The total number of players.
     */
    public int getNumberOfPlayers() {
        return players.size();
    }

    /**
     * Adds a card on top of the discarded pile.
     * @param card The card to put on top of the discarded pile.
     */
    public void addCardToDiscarded(Card card) {
        discarded.add(card);
    }

    /**
     * Returns the remaining number of cards in the deck.
     * @return The remaining number of cards in the deck.
     */
    public int sizeOfDeck() {
        return deck.size();
    }

    /**
     * Returns the remaining number of cards in the discarded pile.
     * @return The remaining number of card in the discarded pile.
     */
    public int sizeOfDiscarded() {
        return discarded.size();
    }

    /**
     * Gets the type of the card at the top of the discarded pile.
     * @return The type of a card.
     */
    public Card.Type getCurrentType() {
        return discarded.get(discarded.size() - 1).getCardType();
    }

    /**
     * Creates and populates the deck according to the deck specifications.
     */
    public void generateDeck() {
        for(Card.Color c : colorsInDeck) {
            switch (c) {
                case WILD:
                    for(int i = 0; i < 4; i++) {
                        deck.add(new WildCard());
                        deck.add(new WildDraw4Card());
                    }
                    break;
                case NOCOLOR:
                    break;
                default:
                    for (Card.Type ct : typesInDeck) {
                        switch (ct) {
                            case ZERO:
                                deck.add(new NumberCard(c, ct));
                                break;
                            case DRAWTWO:
                                deck.add(new Draw2Card(c));
                                break;
                            case REVERSE:
                                deck.add(new ReverseCard(c));
                                break;
                            case SKIP:
                                deck.add(new SkipCard(c));
                                break;
                            case WILD:
                            case WILDDRAWFOUR:
                            case NOTYPE:
                                break;
                            default:
                                deck.add(new NumberCard(c, ct));
                                deck.add(new NumberCard(c, ct));
                        }
                    }
            }
        }
    }

    /**
     * Shuffles the deck.
     */
    public void shuffleDeck() {
        Collections.shuffle(deck);
    }

    /**
     * Takes all but the top-most card in the discarded pile, then adds them to the deck.
     * Wild cards will have their declared color reset to Card.Color.NOCOLOR.
     */
    public void repopulateDeck() {
        Card temp;

        System.out.println("No cards left in the deck, reshuffling discard pile.");

        while(discarded.size() > 1) {
            temp = discarded.remove(0);

            if(temp.getCardColor() == Card.Color.WILD) {
                switch(temp.getCardType()) {
                    case WILD:
                        ((WildCard) temp).setDeclaredColor(Card.Color.NOCOLOR);
                        break;
                    case WILDDRAWFOUR:
                        ((WildDraw4Card) temp).setDeclaredColor(Card.Color.NOCOLOR);
                }
            }

            deck.add(temp);
        }
    }

    /**
     * Generates players participating in the game.
     * @param numberOfPlayers The number of total players to generate.
     */
    public void generatePlayers(int numberOfPlayers) {
        assert (numberOfPlayers >= 2);

        for(int i = 0; i < numberOfPlayers; i++) {
            players.add(new Player(i));
        }
    }

    /**
     * Generates players and AIs participating in the game.
     * @param manual The number of human players to generate.
     * @param baseline The number of baseline AIs to generate.
     * @param strategic The number of strategic AIs to generate.
     */
    public void generateEntities(int manual, int baseline, int strategic) {
        int playerID = 0;

        for(int i = 0; i < manual; i++) {
            players.add(new Player(playerID));
            playerID++;
        }
        for(int i = 0; i < baseline; i++) {
            Player temp = new Player(playerID);
            temp.mode = Player.Mode.BASELINE;
            players.add(temp);
            playerID++;
        }
        for(int i = 0; i < strategic; i++) {
            Player temp = new Player(playerID);
            temp.mode = Player.Mode.STRATEGIC;
            players.add(temp);
            playerID++;
        }
    }

    /**
     * Attaches the players to the current game and deals the initial hand of seven cards to each player.
     */
    public void initializePlayers() {
        assert (!players.isEmpty()) & (!deck.isEmpty());

        for(Player p : players) {
            p.playerSession = this;

            for(int cards = 7; cards > 0; cards--) {
                p.addToPlayerHand(drawCard());
            }
        }
    }

    /**
     * Draws a card from the deck to the discarded pile, and randomly selects
     * a player to play first. If the drawn card is a wild card, a color will
     * be randomly declared.
     */
    public void initializeGame() {
        Card startingCard = drawCard();

        if(startingCard.getCardColor() == Card.Color.WILD) {
            switch (startingCard.getCardType()) {
                case WILD:
                    ((WildCard) startingCard).setDeclaredColor(colorsInDeck[gameRng.nextInt(4)]);
                    break;
                case WILDDRAWFOUR:
                    ((WildDraw4Card) startingCard).setDeclaredColor(colorsInDeck[gameRng.nextInt(4)]);
            }

        }

        addCardToDiscarded(startingCard);
        currentPlayerIndex = gameRng.nextInt(players.size());
    }

    /**
     * Draws a card from a deck. If drawing the card leaves no cards in the deck,
     * repopulates the deck with cards in the discarded pile.
     * @return The drawn card.
     */
    public Card drawCard() {
        Card drawnCard = deck.remove(0);

        if(deck.size() == 0) {
            repopulateDeck();
            shuffleDeck();
        }

        return drawnCard;
    }

    /**
     * Shuffles the current play order.
     */
    public void shuffleOrder() {
        Collections.shuffle(players);
        System.out.print("The play order has been shuffled. ");
        CLIView.playerOrder(this);
        currentPlayerIndex = 0;
    }

    /**
     * Reverses the current play order.
     */
    public void reverseOrder() {
        switch(order) {
            case LEFT:
                order = Direction.RIGHT;
                break;
            case RIGHT:
                order = Direction.LEFT;
        }
        CLIView.playerOrder(this);
    }

    /**
     * Gets the current play order.
     * @return The current play order.
     */
    public Direction getOrder() {
        return order;
    }

    /**
     * Increments the play count of a given color.
     * @param color The color of the card that was played.
     */
    public void updatePlayCounts(Card.Color color) {
        switch(color) {
            case RED:
                playCount[0]++;
            case YELLOW:
                playCount[1]++;
            case GREEN:
                playCount[2]++;
            case BLUE:
                playCount[3]++;
        }
    }
}