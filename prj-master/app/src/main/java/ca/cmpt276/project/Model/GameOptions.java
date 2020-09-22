package ca.cmpt276.project.Model;

//This class is responsible for any game modifcations that the user wishes to make,
//which includes changing the order size, changing the amount of draw cards, whether they wish to play
//with images or images mixed in with words, choosing between card sets, and finally,
//using the Flickr implementation to browse and populate a images they find from the internet

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class GameOptions {

    private int[][] order_2_array = {{0, 1, 4}, {2, 3, 4}, {0, 2, 5}, {1, 3, 5}, {0, 3, 6}, {1, 2, 6}, {4, 5, 6}};
    private int[][] order_3_array = {{0, 1, 2, 9}, {9, 3, 4, 5}, {8, 9, 6, 7}, {0, 10, 3, 6}, {1, 10, 4, 7}, {8, 2, 10, 5}, {0, 8, 11, 4}, {1, 11, 5, 6}, {11, 2, 3, 7}, {0, 12, 5, 7}, {8, 1, 3, 12}, {12, 2, 4, 6}, {9, 10, 11, 12}};
    private int[][] order_5_array = {{0, 1, 2, 3, 4, 25}, {5, 6, 7, 8, 9, 25}, {10, 11, 12, 13, 14, 25}, {15, 16, 17, 18, 19, 25}, {20, 21, 22, 23, 24, 25}, {0, 5, 10, 15, 20, 26}, {1, 6, 11, 16, 21, 26}, {2, 7, 12, 17, 22, 26}, {3, 8, 13, 18, 23, 26}, {4, 9, 14, 19, 24, 26}, {0, 6, 12, 18, 24, 27}, {1, 7, 13, 19, 20, 27}, {2, 8, 14, 15, 21, 27}, {3, 9, 10, 16, 22, 27}, {4, 5, 11, 17, 23, 27}, {0, 7, 14, 16, 23, 28}, {1, 8, 10, 17, 24, 28}, {2, 9, 11, 18, 20, 28}, {3, 5, 12, 19, 21, 28}, {4, 6, 13, 15, 22, 28}, {0, 8, 11, 19, 22, 29}, {1, 9, 12, 15, 23, 29}, {2, 5, 13, 16, 24, 29}, {3, 6, 14, 17, 20, 29}, {4, 7, 10, 18, 21, 29}, {0, 9, 13, 17, 21, 30}, {1, 5, 14, 18, 22, 30}, {2, 6, 10, 19, 23, 30}, {3, 7, 11, 15, 24, 30}, {4, 8, 12, 16, 20, 30}, {25, 26, 27, 28, 29, 30}};

    private List<List<String>> drawPileCardsInfo = null;
    private List<List<String>> discardPileCardsInfo = null;

    private List<int[]> difficultyMediumDrawPileCardsInfo;
    private List<int[]> difficultyMediumDiscardPileCardsInfo;

    private List<int[]> difficultyHardDrawPileCardsInfo;
    private List<int[]> difficultyHardDiscardPileCardsInfo;

    private int order = 2;
    private int drawPileSize = 0;
    private int picturesPerCard = 3;
    private int totalCards = 7;
    private int[][] selectedDeck = order_2_array;

    private String gameMode = "classic"; //either "classic" or "words/images"
    private String gameDifficulty = "easy"; //either "easy", "medium", or "hard"

    /*
        Singleton Support
    */
    private static GameOptions instance;
    private GameOptions() {
        // Private to prevent anyone else from instantiating
    }
    public static GameOptions getInstance() {
        if(instance == null) {
            instance = new GameOptions();
        }
        return instance;
    }

    /*
        Normal Object Code
    */
    public int getOrder() {
        return order;
    }

    public String getGameMode() {
        return gameMode;
    }

    public void setGameMode(String gameMode) {
        this.gameMode = gameMode;
    }

    public void setOrder(int order) {
        if(order == 2) {
            this.picturesPerCard = 3;
            this.totalCards = 7;
            this.selectedDeck = order_2_array;
        } else if(order == 3) {
            this.picturesPerCard = 4;
            this.totalCards = 13;
            this.selectedDeck = order_3_array;
        } else if(order == 5) {
            this.picturesPerCard = 6;
            this.totalCards = 31;
            this.selectedDeck = order_5_array;
        }

        this.order = order;
    }

    public List<List<String>> getDrawPileCardsInfo() {
        return drawPileCardsInfo;
    }

    public List<List<String>> getDiscardPileCardsInfo() {
        return discardPileCardsInfo;
    }

    public List<int[]> getDifficultyMediumDrawPileCardsInfo() {
        return difficultyMediumDrawPileCardsInfo;
    }

    public List<int[]> getDifficultyMediumDiscardPileCardsInfo() {
        return difficultyMediumDiscardPileCardsInfo;
    }

    public List<int[]> getDifficultyHardDrawPileCardsInfo() {
        return difficultyHardDrawPileCardsInfo;
    }

    public List<int[]> getDifficultyHardDiscardPileCardsInfo() {
        return difficultyHardDiscardPileCardsInfo;
    }

    public void moveCardsInfo(){
        discardPileCardsInfo.add(drawPileCardsInfo.get(0));
        drawPileCardsInfo.remove(0);
    }

    public void moveMediumDifficultyCardsInfo() {
        difficultyMediumDiscardPileCardsInfo.add(difficultyMediumDrawPileCardsInfo.get(0));
        difficultyMediumDrawPileCardsInfo.remove(0);
    }

    public void moveHardDifficultyCardsInfo() {
        difficultyHardDiscardPileCardsInfo.add(difficultyHardDrawPileCardsInfo.get(0));
        difficultyHardDrawPileCardsInfo.remove(0);
    }

    public int getDrawPileSize() {
        return drawPileSize;
    }

    public void setDrawPileSize(int drawPileSize) {
        this.drawPileSize = drawPileSize;
    }

    public int getPicturesPerCard() {
        return picturesPerCard;
    }

    public void setPicturesPerCard(int picturesPerCard) {
        this.picturesPerCard = picturesPerCard;
    }

    public int[][] getDeck() {
        if(gameMode.equals("words/images")) {
            setupCardsInfo();
        }

        if(gameDifficulty.equals("Medium") || gameDifficulty.equals("Hard")) {
            setupMediumDifficultyCardsInfo();
        }

        if(gameDifficulty.equals("Hard")) {
            setupHardDifficulty();
        }

        if(drawPileSize == 0) {
            return selectedDeck;
        }

        return constructDeck();
    }

    private void setupMediumDifficultyCardsInfo() {
        int numCards = 0;
        if(drawPileSize == 0){
            switch(order){
                case 2:
                    numCards = 7;
                    break;
                case 3:
                    numCards = 13;
                    break;
                case 5:
                    numCards = 31;
                    break;
            }
        } else{
            numCards = drawPileSize;
        }

        difficultyMediumDrawPileCardsInfo = new ArrayList<>();
        difficultyMediumDiscardPileCardsInfo = new ArrayList<>();

        Random rand = new Random();

        for(int i = 0; i < numCards; i++) {
            int[] rotationArray = new int[picturesPerCard];
            for(int j = 0; j < picturesPerCard; j++) {
                int temp = rand.nextInt(4);
                rotationArray[j] = temp;
            }
            System.out.println(Arrays.toString(rotationArray));

            difficultyMediumDrawPileCardsInfo.add(rotationArray);
        }
    }

    private void setupHardDifficulty() {
        int numCards = 0;
        if(drawPileSize == 0){
            switch(order){
                case 2:
                    numCards = 7;
                    break;
                case 3:
                    numCards = 13;
                    break;
                case 5:
                    numCards = 31;
                    break;
            }
        } else{
            numCards = drawPileSize;
        }

        difficultyHardDrawPileCardsInfo = new ArrayList<>();
        difficultyHardDiscardPileCardsInfo = new ArrayList<>();

        Random rand = new Random();

        for(int i = 0; i < numCards; i++) {
            int[] resizeArray = new int[picturesPerCard];
            for(int j = 0; j < picturesPerCard; j++) {
                int temp = rand.nextInt(3);
                resizeArray[j] = temp;
            }

            difficultyHardDrawPileCardsInfo.add(resizeArray);
        }
    }

    private int[][] constructDeck() {
        int numRows = drawPileSize;
        int numCols = picturesPerCard;

        int[][] constructedArray = new int[numRows][numCols];

        for(int i = 0; i < numRows; i++) {
            for(int j = 0; j < numCols; j++) {
                constructedArray[i][j] = selectedDeck[i][j];
            }
        }

        return constructedArray;
    }

    private void setupCardsInfo() {
        drawPileCardsInfo = new ArrayList<>();
        discardPileCardsInfo = new ArrayList<>();

        Random rand = new Random();

        int numCards = 0;
        if(drawPileSize == 0){
            switch(order){
                case 2:
                    numCards = 7;
                    break;
                case 3:
                    numCards = 13;
                    break;
                case 5:
                    numCards = 31;
                    break;
            }
        } else{
            numCards = drawPileSize;
        }

        for(int i = 0; i < numCards; i++){
            List<String> cardInfo = new ArrayList<>();
            cardInfo.add("w");
            cardInfo.add("i");
            for(int j = 0; j < picturesPerCard-2; j++){
                int temp = rand.nextInt(2);
                if(temp == 0){
                    cardInfo.add("w");
                } else{
                    cardInfo.add("i");
                }
            }
            Collections.shuffle(cardInfo);
            drawPileCardsInfo.add(cardInfo);
        }
    }

    public String getGameDifficulty() {
        return gameDifficulty;
    }

    public void setGameDifficulty(String gameDifficulty) {
        this.gameDifficulty = gameDifficulty;
    }

}
