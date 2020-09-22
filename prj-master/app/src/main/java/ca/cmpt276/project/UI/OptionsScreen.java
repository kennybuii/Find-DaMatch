package ca.cmpt276.project.UI;

//This class is used to setup all the other classes that are involved in modifying the game,
//which includes changing the order size, the amount of draw cards, whether or not to play
//with images or images mixed with texts, which card set to use, and finally the Flickr implementation

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import ca.cmpt276.project.Model.CardOptionsData;
import ca.cmpt276.project.Model.GalleryImages;
import ca.cmpt276.project.Model.GameOptions;

import ca.cmpt276.project.Flickr.SelectedImages;
import ca.cmpt276.project.R;

public class OptionsScreen extends AppCompatActivity {

    CardOptionsData cardOptionsData;
    private final static String APP_PREFS = "AppPrefs";
    private final static String cardKey = "Key Chosen";

    private int order;
    private int drawPileSize;
    private String gameMode;
    private String gameDifficulty;

    private GameOptions gameOptions = GameOptions.getInstance();
    private SelectedImages selectedImages = SelectedImages.getInstance();
    private GalleryImages galleryImages = GalleryImages.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options_screen);

        cardOptionsData = CardOptionsData.getInstance();
        createCardSetOption();
        updateCardOptionsData();



        setupFlickrButton();
        setupUploadImgButton();

        createOrderOptions();
        createDrawPileSizeOptions();
        createGameModeOptions();
        createGameDifficultyOptions();

        setupSaveButton();
        setupBackButton();
    }

    private void createGameDifficultyOptions() {
        RadioGroup group = findViewById(R.id.radio_group_game_difficulty);

        for(int i = 0; i < 3; i++) {
            String text = "Difficulty: ";
            final String difficulty;
            if(i == 0) {
                difficulty = "Easy";
            } else if (i == 1) {
                difficulty = "Medium";
            }  else {
                difficulty = "Hard";
            }

            RadioButton button = new RadioButton(this);

            String displayText = text + difficulty;
            button.setText(displayText);

            button.setTextColor(Color.rgb(255,165,0));
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    gameDifficulty = difficulty;
                }
            });

            group.addView(button);
        }
    }

    private void updateCardOptionsData() {

        cardOptionsData.setCardSet(getSelectCardOption(this));

    }

    static public int getSelectCardOption(Context context) {

        int DEFAULT_VALUE = 1;
        SharedPreferences prefs = context.getSharedPreferences(APP_PREFS, MODE_PRIVATE);
        return prefs.getInt(cardKey, DEFAULT_VALUE);

    }

    private void createCardSetOption() {

        final RadioGroup group = findViewById(R.id.radio_group_card_set);

        for (int i = 0; i < 2; i ++) {

            final int chosenCard = i;
            RadioButton button = new RadioButton(this);
            button.setText("Card Set " + (chosenCard+1));
            button.setTextColor(Color.rgb(255,165,0));
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cardOptionsData.requestCardSet(chosenCard);
                    saveSelectCardOption(chosenCard);
                }
            });

            group.addView(button);
        }

        //3rd image set from flickr
        RadioButton button = new RadioButton(this);
        button.setText("Flickr Images Set");
        button.setTextColor(Color.rgb(255,165,0));
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardOptionsData.requestCardSet(2);
            }
        });
        group.addView(button);

        //4th image set from Gallery
        RadioButton button1 = new RadioButton(this);
        button1.setText("Gallery Images Set");
        button1.setTextColor(Color.rgb(255,165,0));
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardOptionsData.requestCardSet(3);
            }
        });
        group.addView(button1);

    }

    private void saveSelectCardOption(int chosenCard) {

        SharedPreferences prefs = this.getSharedPreferences(APP_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(cardKey, chosenCard);
        editor.apply();

    }


    private void createOrderOptions() {
        RadioGroup group = (RadioGroup) findViewById(R.id.radio_group_order);

        int[] order_options = getResources().getIntArray(R.array.order_options);

        // Create the buttons
        for(int i = 0; i < order_options.length; i++) {
            final int orderOption = order_options[i];

            RadioButton button = new RadioButton(this);
            button.setText("Order: " + orderOption);
            button.setTextColor(Color.rgb(255,165,0));

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    order = orderOption;
                }
            });

            // Add to radio group
            group.addView(button);
        }
    }

    private void createDrawPileSizeOptions() {
        RadioGroup group = (RadioGroup) findViewById(R.id.radio_group_draw_pile_size);

        final int[] draw_pile_size_options = getResources().getIntArray(R.array.draw_pile_size_options);

        // Create the buttons
        for(int i = 0; i < draw_pile_size_options.length; i++) {
            final int drawPileSizeOption = draw_pile_size_options[i];

            RadioButton button = new RadioButton(this);
            button.setText("Draw-pile Size: " + drawPileSizeOption);
            button.setTextColor(Color.rgb(255,165,0));
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawPileSize = drawPileSizeOption;
                }
            });

            // Add to radio group
            group.addView(button);
        }

        // Statically create the "all" button
        RadioButton button = new RadioButton(this);
        button.setText("Draw-pile Size: All");
        button.setTextColor(Color.rgb(255,165,0));
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawPileSize = 0;
            }
        });
        group.addView(button);
    }

    private void createGameModeOptions() {
        final RadioGroup group = findViewById(R.id.radio_group_game_mode);

        for (int i = 0; i < 2; i ++) {

            RadioButton button = new RadioButton(this);
            button.setTextColor(Color.rgb(255,165,0));
            if(i == 0) {
                button.setText("Game Mode: Classic");
            } else{
                button.setText("Game Mode: Words/Images");
            }

            final int finalI = i;
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(finalI == 0){
                        gameMode = "classic";
                    } else {
                        gameMode = "words/images";
                    }
                }
            });

            group.addView(button);

        }
    }

    private void setupSaveButton() {

        final String success = "Changes saved successfully.";
        final String failure = "Error: Changes were unable to be saved.";

        Button btn = findViewById(R.id.save_button);

        int width = btn.getLayoutParams().width;
        int height = btn.getLayoutParams().height;
        Bitmap originalBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.save_text);
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(originalBitmap, width, height, true);
        btn.setBackground(new BitmapDrawable(getResources(), scaledBitmap));

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (order == 2 && drawPileSize <= 7 &&
                        (cardOptionsData.getRequestedCardSet() == 0 || cardOptionsData.getRequestedCardSet() == 1) &&
                        (gameMode.equals("classic") || gameMode.equals("words/images"))) {

                    setValues();
                    cardOptionsData.setCardSet(cardOptionsData.getRequestedCardSet());
                    gameOptions.setGameMode(gameMode);
                    gameOptions.setGameDifficulty(gameDifficulty);
                    Toast.makeText(OptionsScreen.this, success, Toast.LENGTH_SHORT).show();

                } else if (order == 3 && drawPileSize <= 13 &&
                        (cardOptionsData.getRequestedCardSet() == 0 || cardOptionsData.getRequestedCardSet() == 1) &&
                        (gameMode.equals("classic") || gameMode.equals("words/images"))) {

                    setValues();
                    cardOptionsData.setCardSet(cardOptionsData.getRequestedCardSet());
                    gameOptions.setGameMode(gameMode);
                    gameOptions.setGameDifficulty(gameDifficulty);
                    Toast.makeText(OptionsScreen.this, success, Toast.LENGTH_SHORT).show();

                } else if (order == 5 && drawPileSize <= 31 &&
                        (cardOptionsData.getRequestedCardSet() == 0 || cardOptionsData.getRequestedCardSet() == 1) &&
                        (gameMode.equals("classic") || gameMode.equals("words/images"))) {

                    setValues();
                    cardOptionsData.setCardSet(cardOptionsData.getRequestedCardSet());
                    gameOptions.setGameMode(gameMode);
                    gameOptions.setGameDifficulty(gameDifficulty);
                    Toast.makeText(OptionsScreen.this, success, Toast.LENGTH_SHORT).show();
                    //------------------------------------------------------------------------------
                } else if (order == 2 && drawPileSize <= 7 &&               //FLICKR
                        (cardOptionsData.getRequestedCardSet() == 2) &&
                        (gameMode.equals("classic")) &&
                        (selectedImages.getSelectedView().size() >= 7)) {

                    setValues();
                    cardOptionsData.setCardSet(cardOptionsData.getRequestedCardSet());
                    gameOptions.setGameMode(gameMode);
                    gameOptions.setGameDifficulty(gameDifficulty);
                    Toast.makeText(OptionsScreen.this, success, Toast.LENGTH_SHORT).show();

                } else if (order == 2 && drawPileSize <= 7 &&               //GALLERY
                        (cardOptionsData.getRequestedCardSet() == 3) &&
                        (gameMode.equals("classic")) &&
                        (galleryImages.getGalleryListDrawable().size() >= 7)) {

                    setValues();
                    cardOptionsData.setCardSet(cardOptionsData.getRequestedCardSet());
                    gameOptions.setGameMode(gameMode);
                    gameOptions.setGameDifficulty(gameDifficulty);
                    Toast.makeText(OptionsScreen.this, success, Toast.LENGTH_SHORT).show();
                }

                else if (order == 3 && drawPileSize <= 13 &&                //FLICKR
                        (cardOptionsData.getRequestedCardSet() == 2) &&
                        (gameMode.equals("classic")) &&
                        (selectedImages.getSelectedView().size() >= 13)) {

                    setValues();
                    cardOptionsData.setCardSet(cardOptionsData.getRequestedCardSet());
                    gameOptions.setGameMode(gameMode);
                    gameOptions.setGameDifficulty(gameDifficulty);
                    Toast.makeText(OptionsScreen.this, success, Toast.LENGTH_SHORT).show();

                } else if (order == 3 && drawPileSize <= 13 &&              //GALLERY
                        (cardOptionsData.getRequestedCardSet() == 3) &&
                        (gameMode.equals("classic")) &&
                        (galleryImages.getGalleryListDrawable().size() >= 13)) {

                    setValues();
                    cardOptionsData.setCardSet(cardOptionsData.getRequestedCardSet());
                    gameOptions.setGameMode(gameMode);
                    gameOptions.setGameDifficulty(gameDifficulty);
                    Toast.makeText(OptionsScreen.this, success, Toast.LENGTH_SHORT).show();

                }
                else if (order == 5 && drawPileSize <= 31 &&                //FLICKR
                        (cardOptionsData.getRequestedCardSet() == 2) &&
                        (gameMode.equals("classic")) &&
                        (selectedImages.getSelectedView().size() >= 31)) {

                    setValues();
                    cardOptionsData.setCardSet(cardOptionsData.getRequestedCardSet());
                    gameOptions.setGameMode(gameMode);
                    gameOptions.setGameDifficulty(gameDifficulty);
                    Toast.makeText(OptionsScreen.this, success, Toast.LENGTH_SHORT).show();

                }
                else if (order == 5 && drawPileSize <= 31 &&                //GALLERY
                        (cardOptionsData.getRequestedCardSet() == 3) &&
                        (gameMode.equals("classic")) &&
                        (galleryImages.getGalleryListDrawable().size() >= 31)) {

                    setValues();
                    cardOptionsData.setCardSet(cardOptionsData.getRequestedCardSet());
                    gameOptions.setGameMode(gameMode);
                    gameOptions.setGameDifficulty(gameDifficulty);
                    Toast.makeText(OptionsScreen.this, success, Toast.LENGTH_SHORT).show();

                }
                    //------------------------------------------------------------------------------
                else {
                    Toast.makeText(OptionsScreen.this, failure, Toast.LENGTH_SHORT).show();
                }

            }
        });

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
                Intent intent = MenuScreen.makeIntent(OptionsScreen.this);
                startActivity(intent);
                finish();
            }
        });

    }

    private void setupFlickrButton() {
        Button btn = findViewById(R.id.flickr_button);

        int width = btn.getLayoutParams().width;
        int height = btn.getLayoutParams().height;
        Bitmap originalBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.flickr_text);
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(originalBitmap, width, height, true);
        btn.setBackground(new BitmapDrawable(getResources(), scaledBitmap));

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = FlickrOptionsScreen.makeIntent(OptionsScreen.this);
                startActivity(intent);
            }
        });
    }

    private void setupUploadImgButton() {
        Button btn = findViewById(R.id.upload_img_button);

        int width = btn.getLayoutParams().width;
        int height = btn.getLayoutParams().height;
        Bitmap originalBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.internal_storage_text);
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(originalBitmap, width, height, true);
        btn.setBackground(new BitmapDrawable(getResources(), scaledBitmap));

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = UploadImgOptionsScreen.makeIntent(OptionsScreen.this);
                startActivity(intent);
            }
        });
    }

    private void setValues() {
        SharedPreferences prefs = getSharedPreferences("Options", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        gameOptions.setOrder(order);
        gameOptions.setDrawPileSize(drawPileSize);
        editor.putInt("Order", order);
        editor.putInt("drawPileSize", drawPileSize);


        editor.apply();
    }


    public static Intent makeIntent(Context context) {
        return new Intent(context, OptionsScreen.class);
    }

}