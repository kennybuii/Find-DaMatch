package ca.cmpt276.project.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.UUID;

import ca.cmpt276.project.Flickr.SelectedImages;
import ca.cmpt276.project.Model.CardOptionsData;
import ca.cmpt276.project.Model.GalleryImages;
import ca.cmpt276.project.Model.Game;
import ca.cmpt276.project.Model.GameOptions;
import ca.cmpt276.project.R;

//This class is responsible for the actual game play of the application, meaning
//users are able to select images between two cards in the fastest succession possible

public class GameScreen extends AppCompatActivity {

    private GameOptions gameOptions = GameOptions.getInstance();
    private SelectedImages selectedImages = SelectedImages.getInstance();
    private GalleryImages galleryImages = GalleryImages.getInstance();
    private Game game;

    private Integer drawPileCardIndex = null;
    private Integer discardPileCardIndex = null;

    private Button[] drawPileCardButtons;
    private Button[] discardPileCardButtons;

    private long timeElapsed;
    private int cardSet;
    private final static String cardKey = "Key Chosen";

    private SoundPool soundPool;
    private int match, no_match, win;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_screen);

        // Get card set user selected
        CardOptionsData data = CardOptionsData.getInstance();
        cardSet = data.getCardSet();

        setSounds();
        getSettings();
        populateButtons();
        getDiscardBitmap();
        setupBackButton();
        setTimer();
    }

    private void setSounds() {
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();

        soundPool = new SoundPool.Builder()
                .setMaxStreams(6)
                .setAudioAttributes(audioAttributes)
                .build();

        match = soundPool.load(this, R.raw.match, 1);
        no_match = soundPool.load(this, R.raw.no_match, 1);
        win = soundPool.load(this, R.raw.win, 1);
    }

    private void getSettings() {
        SharedPreferences prefs = getSharedPreferences("Options", MODE_PRIVATE);
        int order = prefs.getInt("Order", 2);
        int drawPileSize = prefs.getInt("drawPileSize", 0);

        gameOptions.setOrder(order);
        gameOptions.setDrawPileSize(drawPileSize);

        game = new Game(gameOptions.getDeck());
        drawPileCardButtons = new Button[gameOptions.getPicturesPerCard()];
        discardPileCardButtons = new Button[gameOptions.getPicturesPerCard()];

        if(gameOptions.getGameDifficulty().equals("Medium") || gameOptions.getGameDifficulty().equals("Hard")) {
            gameOptions.moveMediumDifficultyCardsInfo();
        }

        if(gameOptions.getGameDifficulty().equals("Hard")) {
            gameOptions.moveHardDifficultyCardsInfo();
        }
    }

    private void populateButtons() {

        TableLayout tableForDrawButtons = (TableLayout) findViewById(R.id.tableForDrawButtons);
        for(int i = 0; i < gameOptions.getPicturesPerCard(); i++) {
            TableRow tableRow = new TableRow(this);
            tableForDrawButtons.addView(tableRow);

            Button button = new Button(this);
            drawPileCardButtons[i] = button;

            final int FINAL_I = i;
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawPileCardIndex = FINAL_I;

                    if (discardPileCardIndex != null && game.matchFound(drawPileCardIndex, discardPileCardIndex)) {
                        soundPool.play(match, 1, 1, 0, 0, 1);
                        game.moveCards();
                        getDrawCardBitmap();

                        if(gameOptions.getGameMode().equals("words/images")){
                            gameOptions.moveCardsInfo();
                            resetButtonImagesAndTexts();
                        }


                        if(gameOptions.getGameDifficulty().equals("Medium") || gameOptions.getGameDifficulty().equals("Hard")) {
                            gameOptions.moveMediumDifficultyCardsInfo();
                        }
                        if(gameOptions.getGameDifficulty().equals("Hard")) {
                            gameOptions.moveHardDifficultyCardsInfo();
                        }
                        updateButtonImages();
                        resetIndices();
                    } else if(discardPileCardIndex != null && !game.matchFound(drawPileCardIndex, discardPileCardIndex)) {
                        soundPool.play(no_match, 1, 1, 0, 0, 1);
                    }
                }
            });

            tableRow.addView(button);
        }

        TableLayout tableForDiscardButtons = (TableLayout) findViewById(R.id.tableForDiscardButtons);
        for(int j = 0; j < gameOptions.getPicturesPerCard(); j++) {
            TableRow tableRow = new TableRow(this);
            tableForDiscardButtons.addView(tableRow);

            Button button = new Button(this);
            discardPileCardButtons[j] = button;

            final int FINAL_J = j;
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    discardPileCardIndex = FINAL_J;

                    if (drawPileCardIndex != null && game.matchFound(drawPileCardIndex, discardPileCardIndex)) {
                        soundPool.play(match, 1, 1, 0, 0, 1);

                        game.moveCards();
                        getDrawCardBitmap();

                        if(gameOptions.getGameMode().equals("words/images")){
                            gameOptions.moveCardsInfo();
                            resetButtonImagesAndTexts();
                        }
                        if(gameOptions.getGameDifficulty().equals("Medium") || gameOptions.getGameDifficulty().equals("Hard")) {
                            gameOptions.moveMediumDifficultyCardsInfo();
                        }
                        if(gameOptions.getGameDifficulty().equals("Hard")) {
                            gameOptions.moveHardDifficultyCardsInfo();
                        }
                        updateButtonImages();
                        resetIndices();
                    } else if(drawPileCardIndex != null && !game.matchFound(drawPileCardIndex, discardPileCardIndex)) {
                        soundPool.play(no_match, 1, 1, 0, 0, 1);
                    }
                }
            });
            tableRow.addView(button);
        }
        updateButtonImages();
    }

    private void resetButtonImagesAndTexts() {

        for(int i = 0; i < gameOptions.getPicturesPerCard(); i++){
            drawPileCardButtons[i].setText("");
            discardPileCardButtons[i].setText("");
            updateDrawButton(R.drawable.grey_background, i);
            updateDiscardButton(R.drawable.grey_background, i);
        }
    }

    private void resetIndices() {
        if(!game.getDrawPile().isEmpty()) {
            drawPileCardIndex = null;
            discardPileCardIndex = null;
        } else {
            finishGame();
        }
    }

    private void setTimer() {
        timeElapsed = 0;
        final long totalSeconds = 1000;
        final long intervalSeconds = 1;

        CountDownTimer timer = new CountDownTimer(totalSeconds * 1000, intervalSeconds * 1000) {
            @SuppressLint("SetTextI18n")
            public void onTick(long millisUntilFinished) {
                timeElapsed = (totalSeconds * 1000 - millisUntilFinished) / 1000;

                TextView txtTimer = findViewById(R.id.txtTimer);
                txtTimer.setText("" + timeElapsed);
            }

            public void onFinish() {
            }
        }.start();
    }

    private void finishGame() {
        soundPool.play(win, 1, 1, 0, 0, 1);

        @SuppressLint("SimpleDateFormat")
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Calendar cal = Calendar.getInstance();

        Intent intent = AddNewScoreScreen.makeIntent(GameScreen.this, timeElapsed, dateFormat.format(cal.getTime()));
        startActivity(intent);

        finish();
    }

    private void setupBackButton() {
        Button btn = findViewById(R.id.back);

        int width = btn.getLayoutParams().width;
        int height = btn.getLayoutParams().height;
        Bitmap originalBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.back_text);
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(originalBitmap, width, height, true);
        btn.setBackground(new BitmapDrawable(getResources(), scaledBitmap));

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, GameScreen.class);
    }

    // Iteration 1 Functions
    private void updateButtonImages() {
        if(!game.getDrawPile().isEmpty()) {
            lockButtonSizes();

            if (cardSet == 0) {
                useCardSet0();
            } else if (cardSet == 1) {
                useCardSet1();
            } else if (cardSet == 2){
                useCardSet2();
            } else {
                useCardSet3();
            }
        }
        else {
            finishGame();
        }
    }

    private void lockButtonSizes() {
        // Locking the draw card's buttons
        for (int i = 0; i < gameOptions.getPicturesPerCard(); i++) {
            Button button = drawPileCardButtons[i];

            int width = button.getWidth();
            button.setMinWidth(width);
            button.setMaxWidth(width);
            int height = button.getHeight();
            button.setMinHeight(height);
            button.setMaxHeight(height);
        }
        // Locking the discard card's buttons
        for (int j = 0; j < gameOptions.getPicturesPerCard(); j++) {
            Button button = discardPileCardButtons[j];

            int width = button.getWidth();
            button.setMinWidth(width);
            button.setMaxWidth(width);
            int height = button.getHeight();
            button.setMinHeight(height);
            button.setMaxHeight(height);
        }
    }

    private void displayWordOrImageForDraw(int imageID, int imageNumber, int imageWordID){
        if(gameOptions.getGameMode().equals("words/images") &&
                gameOptions.getDrawPileCardsInfo().get(0).get(imageNumber).equals("w")){
            updateDrawButton(imageWordID, imageNumber);
        } else{
            updateDrawButton(imageID, imageNumber);
        }
    }

    private void displayWordOrImageForDiscard(int imageID, int imageNumber, int imageWordID){
        if(gameOptions.getGameMode().equals("words/images") &&
                gameOptions.getDiscardPileCardsInfo().get(
                        gameOptions.getDiscardPileCardsInfo().size()-1).get(imageNumber).equals("w")){
            updateDiscardButton(imageWordID, imageNumber);
        } else{
            updateDiscardButton(imageID, imageNumber);
        }
    }

    private void useCardSet0() {

        for (int i = 0; i < gameOptions.getPicturesPerCard(); i++) {

            int drawCard = game.getDrawPile().get(0)[i];

            switch (drawCard) {
                case 0:
                    displayWordOrImageForDraw(R.drawable.galaxy, i, R.drawable.galaxy_word);
                    break;
                case 1:
                    displayWordOrImageForDraw(R.drawable.rocket1, i, R.drawable.rocket_word);
                    break;
                case 2:
                    displayWordOrImageForDraw(R.drawable.moon, i, R.drawable.moon_word);
                    break;
                case 3:
                    displayWordOrImageForDraw(R.drawable.sun, i, R.drawable.sun_word);
                    break;
                case 4:
                    displayWordOrImageForDraw(R.drawable.satellite, i, R.drawable.satellite_word);
                    break;
                case 5:
                    displayWordOrImageForDraw(R.drawable.astronaut, i, R.drawable.astronaut_word);
                    break;
                case 6:
                    displayWordOrImageForDraw(R.drawable.asteroid, i, R.drawable.asteroid_word);
                    break;
                case 7:
                    displayWordOrImageForDraw(R.drawable.blue_star, i, R.drawable.blue_star_word);
                    break;
                case 8:
                    displayWordOrImageForDraw(R.drawable.cyan_alien, i, R.drawable.cyan_alien_word);
                    break;
                case 9:
                    displayWordOrImageForDraw(R.drawable.earth, i, R.drawable.earth_word);
                    break;
                case 10:
                    displayWordOrImageForDraw(R.drawable.green_alien, i, R.drawable.green_alien_word);
                    break;
                case 11:
                    displayWordOrImageForDraw(R.drawable.green_star, i, R.drawable.green_star_word);
                    break;
                case 12:
                    displayWordOrImageForDraw(R.drawable.grey_star, i, R.drawable.grey_star_word);
                    break;
                case 13:
                    displayWordOrImageForDraw(R.drawable.jupiter, i, R.drawable.jupiter_word);
                    break;
                case 14:
                    displayWordOrImageForDraw(R.drawable.mars, i, R.drawable.mars_word);
                    break;
                case 15:
                    displayWordOrImageForDraw(R.drawable.mercury, i, R.drawable.mercury_word);
                    break;
                case 16:
                    displayWordOrImageForDraw(R.drawable.neptune, i, R.drawable.neptune_word);
                    break;
                case 17:
                    displayWordOrImageForDraw(R.drawable.orange_alien, i, R.drawable.orange_alien_word);
                    break;
                case 18:
                    displayWordOrImageForDraw(R.drawable.orange_star, i, R.drawable.orange_star_word);
                    break;
                case 19:
                    displayWordOrImageForDraw(R.drawable.pink_alien, i, R.drawable.pink_alien_word);
                    break;
                case 20:
                    displayWordOrImageForDraw(R.drawable.pink_star, i, R.drawable.pink_star_word);
                    break;
                case 21:
                    displayWordOrImageForDraw(R.drawable.purple_alien, i, R.drawable.purple_alien_word);
                    break;
                case 22:
                    displayWordOrImageForDraw(R.drawable.purple_star, i, R.drawable.purple_star_word);
                    break;
                case 23:
                    displayWordOrImageForDraw(R.drawable.red_alien, i, R.drawable.red_alien_word);
                    break;
                case 24:
                    displayWordOrImageForDraw(R.drawable.red_star, i, R.drawable.red_star_word);
                    break;
                case 25:
                    displayWordOrImageForDraw(R.drawable.saturn, i, R.drawable.saturn_word);
                    break;
                case 26:
                    displayWordOrImageForDraw(R.drawable.shooting_star, i, R.drawable.shooting_star_word);
                    break;
                case 27:
                    displayWordOrImageForDraw(R.drawable.ufo, i, R.drawable.ufo_word);
                    break;
                case 28:
                    displayWordOrImageForDraw(R.drawable.uranus, i, R.drawable.uranus_word);
                    break;
                case 29:
                    displayWordOrImageForDraw(R.drawable.venus, i, R.drawable.venus_word);
                    break;
                case 30:
                    displayWordOrImageForDraw(R.drawable.yellow_star, i, R.drawable.yellow_star_word);
                    break;
            }
        }

        for (int i = 0; i < gameOptions.getPicturesPerCard(); i++) {

            int discardCard = game.getDiscardPile().get(game.getDiscardPile().size() - 1)[i];

            switch (discardCard) {
                case 0:
                    displayWordOrImageForDiscard(R.drawable.galaxy, i, R.drawable.galaxy_word);
                    break;
                case 1:
                    displayWordOrImageForDiscard(R.drawable.rocket1, i, R.drawable.rocket_word);
                    break;
                case 2:
                    displayWordOrImageForDiscard(R.drawable.moon, i, R.drawable.moon_word);
                    break;
                case 3:
                    displayWordOrImageForDiscard(R.drawable.sun, i, R.drawable.sun_word);
                    break;
                case 4:
                    displayWordOrImageForDiscard(R.drawable.satellite, i, R.drawable.satellite_word);
                    break;
                case 5:
                    displayWordOrImageForDiscard(R.drawable.astronaut, i, R.drawable.astronaut_word);
                    break;
                case 6:
                    displayWordOrImageForDiscard(R.drawable.asteroid, i, R.drawable.asteroid_word);
                    break;
                case 7:
                    displayWordOrImageForDiscard(R.drawable.blue_star, i, R.drawable.blue_star_word);
                    break;
                case 8:
                    displayWordOrImageForDiscard(R.drawable.cyan_alien, i, R.drawable.cyan_alien_word);
                    break;
                case 9:
                    displayWordOrImageForDiscard(R.drawable.earth, i, R.drawable.earth_word);
                    break;
                case 10:
                    displayWordOrImageForDiscard(R.drawable.green_alien, i, R.drawable.green_alien_word);
                    break;
                case 11:
                    displayWordOrImageForDiscard(R.drawable.green_star, i, R.drawable.green_star_word);
                    break;
                case 12:
                    displayWordOrImageForDiscard(R.drawable.grey_star, i, R.drawable.grey_star_word);
                    break;
                case 13:
                    displayWordOrImageForDiscard(R.drawable.jupiter, i, R.drawable.jupiter_word);
                    break;
                case 14:
                    displayWordOrImageForDiscard(R.drawable.mars, i, R.drawable.mars_word);
                    break;
                case 15:
                    displayWordOrImageForDiscard(R.drawable.mercury, i, R.drawable.mercury_word);
                    break;
                case 16:
                    displayWordOrImageForDiscard(R.drawable.neptune, i, R.drawable.neptune_word);
                    break;
                case 17:
                    displayWordOrImageForDiscard(R.drawable.orange_alien, i, R.drawable.orange_alien_word);
                    break;
                case 18:
                    displayWordOrImageForDiscard(R.drawable.orange_star, i, R.drawable.orange_star_word);
                    break;
                case 19:
                    displayWordOrImageForDiscard(R.drawable.pink_alien, i, R.drawable.pink_alien_word);
                    break;
                case 20:
                    displayWordOrImageForDiscard(R.drawable.pink_star, i, R.drawable.pink_star_word);
                    break;
                case 21:
                    displayWordOrImageForDiscard(R.drawable.purple_alien, i, R.drawable.purple_alien_word);
                    break;
                case 22:
                    displayWordOrImageForDiscard(R.drawable.purple_star, i, R.drawable.purple_star_word);
                    break;
                case 23:
                    displayWordOrImageForDiscard(R.drawable.red_alien, i, R.drawable.red_alien_word);
                    break;
                case 24:
                    displayWordOrImageForDiscard(R.drawable.red_star, i, R.drawable.red_star_word);
                    break;
                case 25:
                    displayWordOrImageForDiscard(R.drawable.saturn, i, R.drawable.saturn_word);
                    break;
                case 26:
                    displayWordOrImageForDiscard(R.drawable.shooting_star, i, R.drawable.shooting_star_word);
                    break;
                case 27:
                    displayWordOrImageForDiscard(R.drawable.ufo, i, R.drawable.ufo_word);
                    break;
                case 28:
                    displayWordOrImageForDiscard(R.drawable.uranus, i, R.drawable.uranus_word);
                    break;
                case 29:
                    displayWordOrImageForDiscard(R.drawable.venus, i, R.drawable.venus_word);
                    break;
                case 30:
                    displayWordOrImageForDiscard(R.drawable.yellow_star, i, R.drawable.yellow_star_word);
                    break;
            }
        }
    }

    private void useCardSet1() {

        for (int i = 0; i < gameOptions.getPicturesPerCard(); i++) {

            int drawCard = game.getDrawPile().get(0)[i];

            switch (drawCard) {
                case 0:
                    displayWordOrImageForDraw(R.drawable.banana, i, R.drawable.banana_word);
                    break;
                case 1:
                    displayWordOrImageForDraw(R.drawable.blueberry, i, R.drawable.blue_berry_word);
                    break;
                case 2:
                    displayWordOrImageForDraw(R.drawable.cake, i, R.drawable.cake_word);
                    break;
                case 3:
                    displayWordOrImageForDraw(R.drawable.carrot, i, R.drawable.carrot_word);
                    break;
                case 4:
                    displayWordOrImageForDraw(R.drawable.cherry, i, R.drawable.cherry_word);
                    break;
                case 5:
                    displayWordOrImageForDraw(R.drawable.chocolate, i, R.drawable.chocolate_word);
                    break;
                case 6:
                    displayWordOrImageForDraw(R.drawable.cookie, i, R.drawable.cookie_word);
                    break;
                case 7:
                    displayWordOrImageForDraw(R.drawable.donut, i, R.drawable.donut_word);
                    break;
                case 8:
                    displayWordOrImageForDraw(R.drawable.drumstick, i, R.drawable.drumstick_word);
                    break;
                case 9:
                    displayWordOrImageForDraw(R.drawable.egg, i, R.drawable.egg_word);
                    break;
                case 10:
                    displayWordOrImageForDraw(R.drawable.eggplant, i, R.drawable.eggplant_word);
                    break;
                case 11:
                    displayWordOrImageForDraw(R.drawable.grapes, i, R.drawable.grapes_word);
                    break;
                case 12:
                    displayWordOrImageForDraw(R.drawable.green_apple, i, R.drawable.green_apple_word);
                    break;
                case 13:
                    displayWordOrImageForDraw(R.drawable.hamburger, i, R.drawable.hamburger_word);
                    break;
                case 14:
                    displayWordOrImageForDraw(R.drawable.hotdog, i, R.drawable.hotdog_word);
                    break;
                case 15:
                    displayWordOrImageForDraw(R.drawable.lemon, i, R.drawable.lemon_word);
                    break;
                case 16:
                    displayWordOrImageForDraw(R.drawable.orange, i, R.drawable.orange_word);
                    break;
                case 17:
                    displayWordOrImageForDraw(R.drawable.pancake, i, R.drawable.pancake_word);
                    break;
                case 18:
                    displayWordOrImageForDraw(R.drawable.peach, i, R.drawable.peach_word);
                    break;
                case 19:
                    displayWordOrImageForDraw(R.drawable.pie, i, R.drawable.pie_word);
                    break;
                case 20:
                    displayWordOrImageForDraw(R.drawable.pineapple, i, R.drawable.pineapple_word);
                    break;
                case 21:
                    displayWordOrImageForDraw(R.drawable.pizza, i, R.drawable.pizza_word);
                    break;
                case 22:
                    displayWordOrImageForDraw(R.drawable.popcorn, i, R.drawable.popcorn_word);
                    break;
                case 23:
                    displayWordOrImageForDraw(R.drawable.raspberry, i, R.drawable.raspberry_word);
                    break;
                case 24:
                    displayWordOrImageForDraw(R.drawable.red_apple, i, R.drawable.red_apple_word);
                    break;
                case 25:
                    displayWordOrImageForDraw(R.drawable.sandwich, i, R.drawable.sandwich_word);
                    break;
                case 26:
                    displayWordOrImageForDraw(R.drawable.strawberry, i, R.drawable.strawberry_word);
                    break;
                case 27:
                    displayWordOrImageForDraw(R.drawable.taco, i, R.drawable.taco_word);
                    break;
                case 28:
                    displayWordOrImageForDraw(R.drawable.tomato, i, R.drawable.tomato_word);
                    break;
                case 29:
                    displayWordOrImageForDraw(R.drawable.watermelon, i, R.drawable.watermelon_word);
                    break;
                case 30:
                    displayWordOrImageForDraw(R.drawable.yellow_pepper, i, R.drawable.yellow_pepper_word);
                    break;

            }
        }

        for (int i = 0; i < gameOptions.getPicturesPerCard(); i++) {

            int discardCard = game.getDiscardPile().get(game.getDiscardPile().size() - 1)[i];

            switch (discardCard) {
                case 0:
                    displayWordOrImageForDiscard(R.drawable.banana, i, R.drawable.banana_word);
                    break;
                case 1:
                    displayWordOrImageForDiscard(R.drawable.blueberry, i, R.drawable.blue_berry_word);
                    break;
                case 2:
                    displayWordOrImageForDiscard(R.drawable.cake, i, R.drawable.cake_word);
                    break;
                case 3:
                    displayWordOrImageForDiscard(R.drawable.carrot, i, R.drawable.carrot_word);
                    break;
                case 4:
                    displayWordOrImageForDiscard(R.drawable.cherry, i, R.drawable.cherry_word);
                    break;
                case 5:
                    displayWordOrImageForDiscard(R.drawable.chocolate, i, R.drawable.chocolate_word);
                    break;
                case 6:
                    displayWordOrImageForDiscard(R.drawable.cookie, i, R.drawable.cookie_word);
                    break;
                case 7:
                    displayWordOrImageForDiscard(R.drawable.donut, i, R.drawable.donut_word);
                    break;
                case 8:
                    displayWordOrImageForDiscard(R.drawable.drumstick, i, R.drawable.drumstick_word);
                    break;
                case 9:
                    displayWordOrImageForDiscard(R.drawable.egg, i, R.drawable.egg_word);
                    break;
                case 10:
                    displayWordOrImageForDiscard(R.drawable.eggplant, i, R.drawable.eggplant_word);
                    break;
                case 11:
                    displayWordOrImageForDiscard(R.drawable.grapes, i, R.drawable.grapes_word);
                    break;
                case 12:
                    displayWordOrImageForDiscard(R.drawable.green_apple, i, R.drawable.green_apple_word);
                    break;
                case 13:
                    displayWordOrImageForDiscard(R.drawable.hamburger, i, R.drawable.hamburger_word);
                    break;
                case 14:
                    displayWordOrImageForDiscard(R.drawable.hotdog, i, R.drawable.hotdog_word);
                    break;
                case 15:
                    displayWordOrImageForDiscard(R.drawable.lemon, i, R.drawable.lemon_word);
                    break;
                case 16:
                    displayWordOrImageForDiscard(R.drawable.orange, i, R.drawable.orange_word);
                    break;
                case 17:
                    displayWordOrImageForDiscard(R.drawable.pancake, i, R.drawable.pancake_word);
                    break;
                case 18:
                    displayWordOrImageForDiscard(R.drawable.peach, i, R.drawable.peach_word);
                    break;
                case 19:
                    displayWordOrImageForDiscard(R.drawable.pie, i, R.drawable.pie_word);
                    break;
                case 20:
                    displayWordOrImageForDiscard(R.drawable.pineapple, i, R.drawable.pineapple_word);
                    break;
                case 21:
                    displayWordOrImageForDiscard(R.drawable.pizza, i, R.drawable.pizza_word);
                    break;
                case 22:
                    displayWordOrImageForDiscard(R.drawable.popcorn, i, R.drawable.popcorn_word);
                    break;
                case 23:
                    displayWordOrImageForDiscard(R.drawable.raspberry, i, R.drawable.raspberry_word);
                    break;
                case 24:
                    displayWordOrImageForDiscard(R.drawable.red_apple, i, R.drawable.red_apple_word);
                    break;
                case 25:
                    displayWordOrImageForDiscard(R.drawable.sandwich, i, R.drawable.sandwich_word);
                    break;
                case 26:
                    displayWordOrImageForDiscard(R.drawable.strawberry, i, R.drawable.strawberry_word);
                    break;
                case 27:
                    displayWordOrImageForDiscard(R.drawable.taco, i, R.drawable.taco_word);
                    break;
                case 28:
                    displayWordOrImageForDiscard(R.drawable.tomato, i, R.drawable.tomato_word);
                    break;
                case 29:
                    displayWordOrImageForDiscard(R.drawable.watermelon, i, R.drawable.watermelon_word);
                    break;
                case 30:
                    displayWordOrImageForDiscard(R.drawable.yellow_pepper, i, R.drawable.yellow_pepper_word);
                    break;

            }
        }
    }

    private void useCardSet2() {

        for (int i = 0; i < gameOptions.getPicturesPerCard(); i++) {

            int drawCard = game.getDrawPile().get(0)[i];

            switch (drawCard) {
                case 0:
                    Drawable drawable = selectedImages.getSelectedView().get(0);
                    displayImageFlickrOrGalleryForDraw(drawable, i);
                    break;
                case 1:
                    drawable = selectedImages.getSelectedView().get(1);
                    displayImageFlickrOrGalleryForDraw(drawable, i);
                    break;
                case 2:
                    drawable = selectedImages.getSelectedView().get(2);
                    displayImageFlickrOrGalleryForDraw(drawable, i);
                    break;
                case 3:
                    drawable = selectedImages.getSelectedView().get(3);
                    displayImageFlickrOrGalleryForDraw(drawable, i);
                    break;
                case 4:
                    drawable = selectedImages.getSelectedView().get(4);
                    displayImageFlickrOrGalleryForDraw(drawable, i);
                    break;
                case 5:
                    drawable = selectedImages.getSelectedView().get(5);
                    displayImageFlickrOrGalleryForDraw(drawable, i);
                    break;
                case 6:
                    drawable = selectedImages.getSelectedView().get(6);
                    displayImageFlickrOrGalleryForDraw(drawable, i);
                    break;
                case 7:
                    drawable = selectedImages.getSelectedView().get(7);
                    displayImageFlickrOrGalleryForDraw(drawable, i);
                    break;
                case 8:
                    drawable = selectedImages.getSelectedView().get(8);
                    displayImageFlickrOrGalleryForDraw(drawable, i);
                    break;
                case 9:
                    drawable = selectedImages.getSelectedView().get(9);
                    displayImageFlickrOrGalleryForDraw(drawable, i);
                    break;
                case 10:
                    drawable = selectedImages.getSelectedView().get(10);
                    displayImageFlickrOrGalleryForDraw(drawable, i);
                    break;
                case 11:
                    drawable = selectedImages.getSelectedView().get(11);
                    displayImageFlickrOrGalleryForDraw(drawable, i);
                    break;
                case 12:
                    drawable = selectedImages.getSelectedView().get(12);
                    displayImageFlickrOrGalleryForDraw(drawable, i);
                    break;
                case 13:
                    drawable = selectedImages.getSelectedView().get(13);
                    displayImageFlickrOrGalleryForDraw(drawable, i);
                    break;
                case 14:
                    drawable = selectedImages.getSelectedView().get(14);
                    displayImageFlickrOrGalleryForDraw(drawable, i);
                    break;
                case 15:
                    drawable = selectedImages.getSelectedView().get(15);
                    displayImageFlickrOrGalleryForDraw(drawable, i);
                    break;
                case 16:
                    drawable = selectedImages.getSelectedView().get(16);
                    displayImageFlickrOrGalleryForDraw(drawable, i);
                    break;
                case 17:
                    drawable = selectedImages.getSelectedView().get(17);
                    displayImageFlickrOrGalleryForDraw(drawable, i);
                    break;
                case 18:
                    drawable = selectedImages.getSelectedView().get(18);
                    displayImageFlickrOrGalleryForDraw(drawable, i);
                    break;
                case 19:
                    drawable = selectedImages.getSelectedView().get(19);
                    displayImageFlickrOrGalleryForDraw(drawable, i);
                    break;
                case 20:
                    drawable = selectedImages.getSelectedView().get(20);
                    displayImageFlickrOrGalleryForDraw(drawable, i);
                    break;
                case 21:
                    drawable = selectedImages.getSelectedView().get(21);
                    displayImageFlickrOrGalleryForDraw(drawable, i);
                    break;
                case 22:
                    drawable = selectedImages.getSelectedView().get(22);
                    displayImageFlickrOrGalleryForDraw(drawable, i);
                    break;
                case 23:
                    drawable = selectedImages.getSelectedView().get(23);
                    displayImageFlickrOrGalleryForDraw(drawable, i);
                    break;
                case 24:
                    drawable = selectedImages.getSelectedView().get(24);
                    displayImageFlickrOrGalleryForDraw(drawable, i);
                    break;
                case 25:
                    drawable = selectedImages.getSelectedView().get(25);
                    displayImageFlickrOrGalleryForDraw(drawable, i);
                    break;
                case 26:
                    drawable = selectedImages.getSelectedView().get(26);
                    displayImageFlickrOrGalleryForDraw(drawable, i);
                    break;
                case 27:
                    drawable = selectedImages.getSelectedView().get(27);
                    displayImageFlickrOrGalleryForDraw(drawable, i);
                    break;
                case 28:
                    drawable = selectedImages.getSelectedView().get(28);
                    displayImageFlickrOrGalleryForDraw(drawable, i);
                    break;
                case 29:
                    drawable = selectedImages.getSelectedView().get(29);
                    displayImageFlickrOrGalleryForDraw(drawable, i);
                    break;
                case 30:
                    drawable = selectedImages.getSelectedView().get(30);
                    displayImageFlickrOrGalleryForDraw(drawable, i);
                    break;

            }
        }

        for (int i = 0; i < gameOptions.getPicturesPerCard(); i++) {

            int discardCard = game.getDiscardPile().get(game.getDiscardPile().size() - 1)[i];

            switch (discardCard) {
                case 0:
                    Drawable drawable = selectedImages.getSelectedView().get(0);
                    displayImageFlickrOrGalleryForDiscard(drawable, i);
                    break;
                case 1:
                    drawable = selectedImages.getSelectedView().get(1);
                    displayImageFlickrOrGalleryForDiscard(drawable, i);
                    break;
                case 2:
                    drawable = selectedImages.getSelectedView().get(2);
                    displayImageFlickrOrGalleryForDiscard(drawable, i);
                    break;
                case 3:
                    drawable = selectedImages.getSelectedView().get(3);
                    displayImageFlickrOrGalleryForDiscard(drawable, i);
                    break;
                case 4:
                    drawable = selectedImages.getSelectedView().get(4);
                    displayImageFlickrOrGalleryForDiscard(drawable, i);
                    break;
                case 5:
                    drawable = selectedImages.getSelectedView().get(5);
                    displayImageFlickrOrGalleryForDiscard(drawable, i);
                    break;
                case 6:
                    drawable = selectedImages.getSelectedView().get(6);
                    displayImageFlickrOrGalleryForDiscard(drawable, i);
                    break;
                case 7:
                    drawable = selectedImages.getSelectedView().get(7);
                    displayImageFlickrOrGalleryForDiscard(drawable, i);
                    break;
                case 8:
                    drawable = selectedImages.getSelectedView().get(8);
                    displayImageFlickrOrGalleryForDiscard(drawable, i);
                    break;
                case 9:
                    drawable = selectedImages.getSelectedView().get(9);
                    displayImageFlickrOrGalleryForDiscard(drawable, i);
                    break;
                case 10:
                    drawable = selectedImages.getSelectedView().get(10);
                    displayImageFlickrOrGalleryForDiscard(drawable, i);
                    break;
                case 11:
                    drawable = selectedImages.getSelectedView().get(11);
                    displayImageFlickrOrGalleryForDiscard(drawable, i);
                    break;
                case 12:
                    drawable = selectedImages.getSelectedView().get(12);
                    displayImageFlickrOrGalleryForDiscard(drawable, i);
                    break;
                case 13:
                    drawable = selectedImages.getSelectedView().get(13);
                    displayImageFlickrOrGalleryForDiscard(drawable, i);
                    break;
                case 14:
                    drawable = selectedImages.getSelectedView().get(14);
                    displayImageFlickrOrGalleryForDiscard(drawable, i);
                    break;
                case 15:
                    drawable = selectedImages.getSelectedView().get(15);
                    displayImageFlickrOrGalleryForDiscard(drawable, i);
                    break;
                case 16:
                    drawable = selectedImages.getSelectedView().get(16);
                    displayImageFlickrOrGalleryForDiscard(drawable, i);
                    break;
                case 17:
                    drawable = selectedImages.getSelectedView().get(17);
                    displayImageFlickrOrGalleryForDiscard(drawable, i);
                    break;
                case 18:
                    drawable = selectedImages.getSelectedView().get(18);
                    displayImageFlickrOrGalleryForDiscard(drawable, i);
                    break;
                case 19:
                    drawable = selectedImages.getSelectedView().get(19);
                    displayImageFlickrOrGalleryForDiscard(drawable, i);
                    break;
                case 20:
                    drawable = selectedImages.getSelectedView().get(20);
                    displayImageFlickrOrGalleryForDiscard(drawable, i);
                    break;
                case 21:
                    drawable = selectedImages.getSelectedView().get(21);
                    displayImageFlickrOrGalleryForDiscard(drawable, i);
                    break;
                case 22:
                    drawable = selectedImages.getSelectedView().get(22);
                    displayImageFlickrOrGalleryForDiscard(drawable, i);
                    break;
                case 23:
                    drawable = selectedImages.getSelectedView().get(23);
                    displayImageFlickrOrGalleryForDiscard(drawable, i);
                    break;
                case 24:
                    drawable = selectedImages.getSelectedView().get(24);
                    displayImageFlickrOrGalleryForDiscard(drawable, i);
                    break;
                case 25:
                    drawable = selectedImages.getSelectedView().get(25);
                    displayImageFlickrOrGalleryForDiscard(drawable, i);
                    break;
                case 26:
                    drawable = selectedImages.getSelectedView().get(26);
                    displayImageFlickrOrGalleryForDiscard(drawable, i);
                    break;
                case 27:
                    drawable = selectedImages.getSelectedView().get(27);
                    displayImageFlickrOrGalleryForDiscard(drawable, i);
                    break;
                case 28:
                    drawable = selectedImages.getSelectedView().get(28);
                    displayImageFlickrOrGalleryForDiscard(drawable, i);
                    break;
                case 29:
                    drawable = selectedImages.getSelectedView().get(29);
                    displayImageFlickrOrGalleryForDiscard(drawable, i);
                    break;
                case 30:
                    drawable = selectedImages.getSelectedView().get(30);
                    displayImageFlickrOrGalleryForDiscard(drawable, i);
                    break;

            }
        }
    }

    private void useCardSet3() {
        for (int i = 0; i < gameOptions.getPicturesPerCard(); i++) {

            int drawCard = game.getDrawPile().get(0)[i];

            switch (drawCard) {
                case 0:
                    Drawable drawable = galleryImages.getGalleryListDrawable().get(0);
                    displayImageFlickrOrGalleryForDraw(drawable, i);
                    break;
                case 1:
                    drawable = galleryImages.getGalleryListDrawable().get(1);
                    displayImageFlickrOrGalleryForDraw(drawable, i);
                    break;
                case 2:
                    drawable = galleryImages.getGalleryListDrawable().get(2);
                    displayImageFlickrOrGalleryForDraw(drawable, i);
                    break;
                case 3:
                    drawable = galleryImages.getGalleryListDrawable().get(3);
                    displayImageFlickrOrGalleryForDraw(drawable, i);
                    break;
                case 4:
                    drawable = galleryImages.getGalleryListDrawable().get(4);
                    displayImageFlickrOrGalleryForDraw(drawable, i);
                    break;
                case 5:
                    drawable = galleryImages.getGalleryListDrawable().get(5);
                    displayImageFlickrOrGalleryForDraw(drawable, i);
                    break;
                case 6:
                    drawable = galleryImages.getGalleryListDrawable().get(6);
                    displayImageFlickrOrGalleryForDraw(drawable, i);
                    break;
                case 7:
                    drawable = galleryImages.getGalleryListDrawable().get(7);
                    displayImageFlickrOrGalleryForDraw(drawable, i);
                    break;
                case 8:
                    drawable = galleryImages.getGalleryListDrawable().get(8);
                    displayImageFlickrOrGalleryForDraw(drawable, i);
                    break;
                case 9:
                    drawable = galleryImages.getGalleryListDrawable().get(9);
                    displayImageFlickrOrGalleryForDraw(drawable, i);
                    break;
                case 10:
                    drawable = galleryImages.getGalleryListDrawable().get(10);
                    displayImageFlickrOrGalleryForDraw(drawable, i);
                    break;
                case 11:
                    drawable = galleryImages.getGalleryListDrawable().get(11);
                    displayImageFlickrOrGalleryForDraw(drawable, i);
                    break;
                case 12:
                    drawable = galleryImages.getGalleryListDrawable().get(12);
                    displayImageFlickrOrGalleryForDraw(drawable, i);
                    break;
                case 13:
                    drawable = galleryImages.getGalleryListDrawable().get(13);
                    displayImageFlickrOrGalleryForDraw(drawable, i);
                    break;
                case 14:
                    drawable = galleryImages.getGalleryListDrawable().get(14);
                    displayImageFlickrOrGalleryForDraw(drawable, i);
                    break;
                case 15:
                    drawable = galleryImages.getGalleryListDrawable().get(15);
                    displayImageFlickrOrGalleryForDraw(drawable, i);
                    break;
                case 16:
                    drawable = galleryImages.getGalleryListDrawable().get(16);
                    displayImageFlickrOrGalleryForDraw(drawable, i);
                    break;
                case 17:
                    drawable = galleryImages.getGalleryListDrawable().get(17);
                    displayImageFlickrOrGalleryForDraw(drawable, i);
                    break;
                case 18:
                    drawable = galleryImages.getGalleryListDrawable().get(18);
                    displayImageFlickrOrGalleryForDraw(drawable, i);
                    break;
                case 19:
                    drawable = galleryImages.getGalleryListDrawable().get(19);
                    displayImageFlickrOrGalleryForDraw(drawable, i);
                    break;
                case 20:
                    drawable = galleryImages.getGalleryListDrawable().get(20);
                    displayImageFlickrOrGalleryForDraw(drawable, i);
                    break;
                case 21:
                    drawable = galleryImages.getGalleryListDrawable().get(21);
                    displayImageFlickrOrGalleryForDraw(drawable, i);
                    break;
                case 22:
                    drawable = galleryImages.getGalleryListDrawable().get(22);
                    displayImageFlickrOrGalleryForDraw(drawable, i);
                    break;
                case 23:
                    drawable = galleryImages.getGalleryListDrawable().get(23);
                    displayImageFlickrOrGalleryForDraw(drawable, i);
                    break;
                case 24:
                    drawable = galleryImages.getGalleryListDrawable().get(24);
                    displayImageFlickrOrGalleryForDraw(drawable, i);
                    break;
                case 25:
                    drawable = galleryImages.getGalleryListDrawable().get(25);
                    displayImageFlickrOrGalleryForDraw(drawable, i);
                    break;
                case 26:
                    drawable = galleryImages.getGalleryListDrawable().get(26);
                    displayImageFlickrOrGalleryForDraw(drawable, i);
                    break;
                case 27:
                    drawable = galleryImages.getGalleryListDrawable().get(27);
                    displayImageFlickrOrGalleryForDraw(drawable, i);
                    break;
                case 28:
                    drawable = galleryImages.getGalleryListDrawable().get(28);
                    displayImageFlickrOrGalleryForDraw(drawable, i);
                    break;
                case 29:
                    drawable = galleryImages.getGalleryListDrawable().get(29);
                    displayImageFlickrOrGalleryForDraw(drawable, i);
                    break;
                case 30:
                    drawable = galleryImages.getGalleryListDrawable().get(30);
                    displayImageFlickrOrGalleryForDraw(drawable, i);
                    break;

            }
        }

        for (int i = 0; i < gameOptions.getPicturesPerCard(); i++) {

            int discardCard = game.getDiscardPile().get(game.getDiscardPile().size() - 1)[i];

            switch (discardCard) {
                case 0:
                    Drawable drawable = galleryImages.getGalleryListDrawable().get(0);
                    displayImageFlickrOrGalleryForDiscard(drawable, i);
                    break;
                case 1:
                    drawable = galleryImages.getGalleryListDrawable().get(1);
                    displayImageFlickrOrGalleryForDiscard(drawable, i);
                    break;
                case 2:
                    drawable = galleryImages.getGalleryListDrawable().get(2);
                    displayImageFlickrOrGalleryForDiscard(drawable, i);
                    break;
                case 3:
                    drawable = galleryImages.getGalleryListDrawable().get(3);
                    displayImageFlickrOrGalleryForDiscard(drawable, i);
                    break;
                case 4:
                    drawable = galleryImages.getGalleryListDrawable().get(4);
                    displayImageFlickrOrGalleryForDiscard(drawable, i);
                    break;
                case 5:
                    drawable = galleryImages.getGalleryListDrawable().get(5);
                    displayImageFlickrOrGalleryForDiscard(drawable, i);
                    break;
                case 6:
                    drawable = galleryImages.getGalleryListDrawable().get(6);
                    displayImageFlickrOrGalleryForDiscard(drawable, i);
                    break;
                case 7:
                    drawable = galleryImages.getGalleryListDrawable().get(7);
                    displayImageFlickrOrGalleryForDiscard(drawable, i);
                    break;
                case 8:
                    drawable = galleryImages.getGalleryListDrawable().get(8);
                    displayImageFlickrOrGalleryForDiscard(drawable, i);
                    break;
                case 9:
                    drawable = galleryImages.getGalleryListDrawable().get(9);
                    displayImageFlickrOrGalleryForDiscard(drawable, i);
                    break;
                case 10:
                    drawable = galleryImages.getGalleryListDrawable().get(10);
                    displayImageFlickrOrGalleryForDiscard(drawable, i);
                    break;
                case 11:
                    drawable = galleryImages.getGalleryListDrawable().get(11);
                    displayImageFlickrOrGalleryForDiscard(drawable, i);
                    break;
                case 12:
                    drawable = galleryImages.getGalleryListDrawable().get(12);
                    displayImageFlickrOrGalleryForDiscard(drawable, i);
                    break;
                case 13:
                    drawable = galleryImages.getGalleryListDrawable().get(13);
                    displayImageFlickrOrGalleryForDiscard(drawable, i);
                    break;
                case 14:
                    drawable = galleryImages.getGalleryListDrawable().get(14);
                    displayImageFlickrOrGalleryForDiscard(drawable, i);
                    break;
                case 15:
                    drawable = galleryImages.getGalleryListDrawable().get(15);
                    displayImageFlickrOrGalleryForDiscard(drawable, i);
                    break;
                case 16:
                    drawable = galleryImages.getGalleryListDrawable().get(16);
                    displayImageFlickrOrGalleryForDiscard(drawable, i);
                    break;
                case 17:
                    drawable = galleryImages.getGalleryListDrawable().get(17);
                    displayImageFlickrOrGalleryForDiscard(drawable, i);
                    break;
                case 18:
                    drawable = galleryImages.getGalleryListDrawable().get(18);
                    displayImageFlickrOrGalleryForDiscard(drawable, i);
                    break;
                case 19:
                    drawable = galleryImages.getGalleryListDrawable().get(19);
                    displayImageFlickrOrGalleryForDiscard(drawable, i);
                    break;
                case 20:
                    drawable = galleryImages.getGalleryListDrawable().get(20);
                    displayImageFlickrOrGalleryForDiscard(drawable, i);
                    break;
                case 21:
                    drawable = galleryImages.getGalleryListDrawable().get(21);
                    displayImageFlickrOrGalleryForDiscard(drawable, i);
                    break;
                case 22:
                    drawable = galleryImages.getGalleryListDrawable().get(22);
                    displayImageFlickrOrGalleryForDiscard(drawable, i);
                    break;
                case 23:
                    drawable = galleryImages.getGalleryListDrawable().get(23);
                    displayImageFlickrOrGalleryForDiscard(drawable, i);
                    break;
                case 24:
                    drawable = galleryImages.getGalleryListDrawable().get(24);
                    displayImageFlickrOrGalleryForDiscard(drawable, i);
                    break;
                case 25:
                    drawable = galleryImages.getGalleryListDrawable().get(25);
                    displayImageFlickrOrGalleryForDiscard(drawable, i);
                    break;
                case 26:
                    drawable = galleryImages.getGalleryListDrawable().get(26);
                    displayImageFlickrOrGalleryForDiscard(drawable, i);
                    break;
                case 27:
                    drawable = galleryImages.getGalleryListDrawable().get(27);
                    displayImageFlickrOrGalleryForDiscard(drawable, i);
                    break;
                case 28:
                    drawable = galleryImages.getGalleryListDrawable().get(28);
                    displayImageFlickrOrGalleryForDiscard(drawable, i);
                    break;
                case 29:
                    drawable = galleryImages.getGalleryListDrawable().get(29);
                    displayImageFlickrOrGalleryForDiscard(drawable, i);
                    break;
                case 30:
                    drawable = galleryImages.getGalleryListDrawable().get(30);
                    displayImageFlickrOrGalleryForDiscard(drawable, i);
                    break;

            }
        }
    }

    //code snippet taken from
    // https://stackoverflow.com/questions/3035692/how-to-convert-a-drawable-to-a-bitmap
    public static Bitmap drawableToBitmap (Drawable drawable) {
        Bitmap bitmap = null;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if(bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if(drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    private void addImageToList(Drawable drawable) {
        CardOptionsData cardOptionsData = CardOptionsData.getInstance();
        cardOptionsData.getImagesList().add(drawable);
    }

    private void displayImageFlickrOrGalleryForDraw(Drawable drawable, int imageNumber) {

        addImageToList(drawable);
        Bitmap originalBitMap = drawableToBitmap(drawable);

        int width = 100;
        int height = 100;

        if(gameOptions.getGameDifficulty().equals("Hard")) {
            switch(gameOptions.getDifficultyHardDrawPileCardsInfo().get(0)[imageNumber]){
                case 0:
                    break;
                case 1:
                    width += 50;
                    height += 50;
                    break;
                case 2:
                    width -= 50;
                    height -= 50;
                    break;
            }
        }

        Bitmap scaledBitMap = Bitmap.createScaledBitmap(originalBitMap, width, height, true);

        if(gameOptions.getGameDifficulty().equals("Medium") || gameOptions.getGameDifficulty().equals("Hard")) {
            switch(gameOptions.getDifficultyMediumDiscardPileCardsInfo().get(0)[imageNumber]){
                case 0:
                    // Rotate 0 degrees
                    scaledBitMap = RotateBitmap(scaledBitMap, 0);
                    break;
                case 1:
                    // Rotate 90 degrees
                    scaledBitMap = RotateBitmap(scaledBitMap, 90);
                    break;
                case 2:
                    // Rotate 180 degrees
                    scaledBitMap = RotateBitmap(scaledBitMap, 180);
                    break;
                case 3:
                    // Rotate 270 degrees
                    scaledBitMap = RotateBitmap(scaledBitMap, 270);
                    break;
            }
        }

        Resources resource = getResources();
        drawPileCardButtons[imageNumber].setBackground(new BitmapDrawable(resource,scaledBitMap));

    }

    private void displayImageFlickrOrGalleryForDiscard(Drawable drawable, int imageNumber) {

        addImageToList(drawable);
        Bitmap originalBitMap = drawableToBitmap(drawable);

        int width = 100;
        int height = 100;

        if(gameOptions.getGameDifficulty().equals("Hard")) {
            int len = gameOptions.getDifficultyHardDiscardPileCardsInfo().size() - 1;

            switch(gameOptions.getDifficultyHardDiscardPileCardsInfo().get(len)[imageNumber]){
                case 0:
                    break;
                case 1:
                    width += 50;
                    height += 50;
                    break;
                case 2:
                    width -= 50;
                    height -= 50;
                    break;
            }
        }

        Bitmap scaledBitMap = Bitmap.createScaledBitmap(originalBitMap, width, height, true);

        if(gameOptions.getGameDifficulty().equals("Medium") || gameOptions.getGameDifficulty().equals("Hard")) {
            int len = gameOptions.getDifficultyMediumDiscardPileCardsInfo().size() - 1;

            switch(gameOptions.getDifficultyMediumDiscardPileCardsInfo().get(len)[imageNumber]){
                case 0:
                    // Rotate 0 degrees
                    scaledBitMap = RotateBitmap(scaledBitMap, 0);
                    break;
                case 1:
                    // Rotate 90 degrees
                    scaledBitMap = RotateBitmap(scaledBitMap, 90);
                    break;
                case 2:
                    // Rotate 180 degrees
                    scaledBitMap = RotateBitmap(scaledBitMap, 180);
                    break;
                case 3:
                    // Rotate 270 degrees
                    scaledBitMap = RotateBitmap(scaledBitMap, 270);
                    break;
            }
        }

        Resources resource = getResources();
        discardPileCardButtons[imageNumber].setBackground(new BitmapDrawable(resource,scaledBitMap));

    }

    private void updateDrawButton(int imageID, int buttonNumber) {
        Bitmap originalBitMap = BitmapFactory.decodeResource(getResources(), imageID);

        int width = 100;
        int height = 100;

        if(gameOptions.getGameDifficulty().equals("Hard")) {
            switch(gameOptions.getDifficultyHardDrawPileCardsInfo().get(0)[buttonNumber]){
                case 0:
                    break;
                case 1:
                    width += 50;
                    height += 50;
                    break;
                case 2:
                    width -= 50;
                    height -= 50;
                    break;
            }
        }

        Bitmap scaledBitMap = Bitmap.createScaledBitmap(originalBitMap, width, height, true);

        if(gameOptions.getGameDifficulty().equals("Medium") || gameOptions.getGameDifficulty().equals("Hard")) {
            switch(gameOptions.getDifficultyMediumDrawPileCardsInfo().get(0)[buttonNumber]) {
                case 0:
                    // Rotate 0 degrees
                    scaledBitMap = RotateBitmap(scaledBitMap, 0);
                    break;
                case 1:
                    // Rotate 90 degrees
                    scaledBitMap = RotateBitmap(scaledBitMap, 90);
                    break;
                case 2:
                    // Rotate 180 degrees
                    scaledBitMap = RotateBitmap(scaledBitMap, 180);
                    break;
                case 3:
                    // Rotate 270 degrees
                    scaledBitMap = RotateBitmap(scaledBitMap, 270);
                    break;
            }
        }

        Resources resource = getResources();
        drawPileCardButtons[buttonNumber].setBackground(new BitmapDrawable(resource,scaledBitMap));
    }

    private void updateDiscardButton(int imageID, int buttonNumber) {
        Bitmap originalBitMap = BitmapFactory.decodeResource(getResources(), imageID);

        int width = 100;
        int height = 100;

        if(gameOptions.getGameDifficulty().equals("Hard")) {
            int len = gameOptions.getDifficultyHardDiscardPileCardsInfo().size() - 1;

            switch(gameOptions.getDifficultyHardDiscardPileCardsInfo().get(len)[buttonNumber]){
                case 0:
                    break;
                case 1:
                    width += 50;
                    height += 50;
                    break;
                case 2:
                    width -= 50;
                    height -= 50;
                    break;
            }
        }

        Bitmap scaledBitMap = Bitmap.createScaledBitmap(originalBitMap, width, height, true);

        if(gameOptions.getGameDifficulty().equals("Medium") || gameOptions.getGameDifficulty().equals("Hard")) {
            int len = gameOptions.getDifficultyMediumDiscardPileCardsInfo().size() - 1;

            switch(gameOptions.getDifficultyMediumDiscardPileCardsInfo().get(len)[buttonNumber]){
                case 0:
                    // Rotate 0 degrees
                    scaledBitMap = RotateBitmap(scaledBitMap, 0);
                    break;
                case 1:
                    // Rotate 90 degrees
                    scaledBitMap = RotateBitmap(scaledBitMap, 90);
                    break;
                case 2:
                    // Rotate 180 degrees
                    scaledBitMap = RotateBitmap(scaledBitMap, 180);
                    break;
                case 3:
                    // Rotate 270 degrees
                    scaledBitMap = RotateBitmap(scaledBitMap, 270);
                    break;
            }
        }

        Resources resource = getResources();
        discardPileCardButtons[buttonNumber].setBackground(new BitmapDrawable(resource,scaledBitMap));
    }

    private void getDrawCardBitmap() {
        CardOptionsData cardOptionsData = CardOptionsData.getInstance();

        for (int i = 0; i < gameOptions.getPicturesPerCard(); i++) {
            Button btn = drawPileCardButtons[i];
             Bitmap bmp = drawableToBitmap(btn.getBackground());
             cardOptionsData.getBitmapList().add(bmp);
      }

            Bitmap firstBitmap = cardOptionsData.getBitmapList().get(0);
            int width = firstBitmap.getWidth();
            int height = firstBitmap.getHeight() * gameOptions.getPicturesPerCard();
            Bitmap result = Bitmap.createBitmap(width, height,Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(result);

        for (int i=0; i < gameOptions.getPicturesPerCard(); i++) {
            canvas.drawBitmap(cardOptionsData.getBitmapList().get(i), 0f, firstBitmap.getHeight()*i,null);
        }

        cardOptionsData.addCardlist(result);
        cardOptionsData.getBitmapList().clear();

    }

    private void getDiscardBitmap() {
        CardOptionsData cardOptionsData = CardOptionsData.getInstance();

        for (int i = 0; i < gameOptions.getPicturesPerCard(); i++) {
            Button btn = discardPileCardButtons[i];
            Bitmap bmp = drawableToBitmap(btn.getBackground());
            cardOptionsData.getBitmapList().add(bmp);
        }

        Bitmap firstBitmap = cardOptionsData.getBitmapList().get(0);
        int width = firstBitmap.getWidth();
        int height = firstBitmap.getHeight() * gameOptions.getPicturesPerCard();
        Bitmap result = Bitmap.createBitmap(width, height,Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(result);

        for (int i=0; i < gameOptions.getPicturesPerCard(); i++) {
            canvas.drawBitmap(cardOptionsData.getBitmapList().get(i), 0f, firstBitmap.getHeight()*i,null);
        }

        cardOptionsData.addCardlist(result);
        cardOptionsData.getBitmapList().clear();
    }


    // Code taken from https://stackoverflow.com/a/16219591
    public static Bitmap RotateBitmap(Bitmap source, float angle)
    {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }
}