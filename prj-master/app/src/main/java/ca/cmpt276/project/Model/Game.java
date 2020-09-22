package ca.cmpt276.project.Model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

//This class is involved with the actual logic of the game, which
//means it is responsible for the functions of the gameplay

public class Game {

    private List<int[]> drawPile;
    private List<int[]> discardPile = new ArrayList<>();

    public Game(int[][] unshuffledDeck) {
        // Shuffle the deck
        List<int[]> shuffledDeck = new ArrayList<>(Arrays.asList(unshuffledDeck));
        Collections.shuffle(shuffledDeck);
        drawPile = shuffledDeck;

        // Move the cards
        moveCards();

        GameOptions gameOptions = GameOptions.getInstance();
        if(gameOptions.getGameMode().equals("words/images")) {
            gameOptions.moveCardsInfo();
        }
    }

    public Boolean matchFound(int drawPileCardIndex, int discardPileCardIndex) {
        return drawPile.get(0)[drawPileCardIndex] == discardPile.get(discardPile.size() - 1)[discardPileCardIndex];
    }

    public void moveCards() {
        discardPile.add(drawPile.get(0));
        drawPile.remove(0);
    }

    public List<int[]> getDrawPile() {
        return drawPile;
    }

    public List<int[]> getDiscardPile() {
        return discardPile;
    }
}
