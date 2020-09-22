package ca.cmpt276.project.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.UUID;

import ca.cmpt276.project.Flickr.SelectedImages;
import ca.cmpt276.project.Model.CardOptionsData;
import ca.cmpt276.project.Model.GameOptions;
import ca.cmpt276.project.Model.HighScores;
import ca.cmpt276.project.Model.Score;
import ca.cmpt276.project.R;

//This class is the UI interface of the highscore screen, which appears
//after the user has finished a session, and if they were to beat the
//default 5th best score, then it would be saved into the system

public class AddNewScoreScreen extends AppCompatActivity {

    private long timeInSec = 0;
    private String dateAndTime = "";
    private HighScores highScoresInst;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_score_screen);

        setHighScoreInstance();
        extractDataFromIntent();
        displayGameScore();
        setupSubmitScoreButton();
        setupDiscardScoreButton();
        setupExportButton();

    }

    private void setupExportButton() {

        Button btn = findViewById(R.id.export_button);

        int width = btn.getLayoutParams().width;
        int height = btn.getLayoutParams().height;
        Bitmap originalBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.export_images_text);
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(originalBitmap, width, height, true);
        btn.setBackground(new BitmapDrawable(getResources(), scaledBitmap));

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveToInternal();
                Toast.makeText(AddNewScoreScreen.this, "Saved to Photos App", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveToInternal() {
        CardOptionsData c = CardOptionsData.getInstance();
        for (int i = 0; i < c.getCardList().size(); i++) {
            MediaStore.Images.Media.insertImage(this.getContentResolver(),
                    c.getCardList().get(i),
                    UUID.randomUUID().toString() + ".png",
                    "Image");
        }
        c.getCardList().clear();
    }

    private void setHighScoreInstance() {
        GameOptions gameOptions = GameOptions.getInstance();

        int order = gameOptions.getOrder();
        int drawPileSize = gameOptions.getDrawPileSize();

        if(order == 2) {
            if(drawPileSize == 0) {
                highScoresInst = HighScores.getInstanceOrderTwoDrawZero();
            } else {
                highScoresInst = HighScores.getInstanceOrderTwoDrawFive();
            }
        } else if (order == 3) {
            if(drawPileSize == 0) {
                highScoresInst = HighScores.getInstanceOrderThreeDrawZero();
            } else if (drawPileSize == 5) {
                highScoresInst = HighScores.getInstanceOrderThreeDrawFive();
            } else {
                highScoresInst = HighScores.getInstanceOrderThreeDrawTen();
            }
        } else {
            if(drawPileSize == 0) {
                highScoresInst = HighScores.getInstanceOrderFiveDrawZero();
            } else if (drawPileSize == 5) {
                highScoresInst = HighScores.getInstanceOrderFiveDrawFive();
            } else if (drawPileSize == 10) {
                highScoresInst = HighScores.getInstanceOrderFiveDrawTen();
            } else if (drawPileSize == 15) {
                highScoresInst = HighScores.getInstanceOrderFiveDrawFifteen();
            } else {
                highScoresInst = HighScores.getInstanceOrderFiveDrawTwenty();
            }
        }
    }

    private void extractDataFromIntent() {

        Intent intent = getIntent();

        timeInSec = intent.getLongExtra("ca.cmpt276.project.UI.AddNewScoreScreen - timeInSec", 0);
        dateAndTime = intent.getStringExtra("ca.cmpt276.project.UI.AddNewScoreScreen - dateAndTime");

    }

    @SuppressLint("SetTextI18n")
    private void displayGameScore() {

        TextView timeInSecView = findViewById(R.id.time_in_sec);
        timeInSecView.setText("Time: "+timeInSec+" seconds");
        TextView dateAndTimeView = findViewById(R.id.date_and_time);
        dateAndTimeView.setText("Date / Time: "+dateAndTime);

    }

    private void setupSubmitScoreButton() {

        Button btn = findViewById(R.id.submit_score_button);

        int width = btn.getLayoutParams().width;
        int height = btn.getLayoutParams().height;
        Bitmap originalBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.submit_text);
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(originalBitmap, width, height, true);
        btn.setBackground(new BitmapDrawable(getResources(), scaledBitmap));

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText playerNameView = findViewById(R.id.player_name);

                highScoresInst.addScore(new Score(playerNameView.getText().toString(),
                                                                timeInSec, dateAndTime));
                Intent intent = MenuScreen.makeIntent(AddNewScoreScreen.this);
                startActivity(intent);
                finish();
            }
        });

    }

    private void setupDiscardScoreButton() {

        Button btn = findViewById(R.id.discard_button);

        int width = btn.getLayoutParams().width;
        int height = btn.getLayoutParams().height;
        Bitmap originalBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.discard_text);
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(originalBitmap, width, height, true);
        btn.setBackground(new BitmapDrawable(getResources(), scaledBitmap));

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = MenuScreen.makeIntent(AddNewScoreScreen.this);
                startActivity(intent);
                finish();
            }
        });

    }

    public static Intent makeIntent(Context context, long timeInSec, String dateAndTime){

        Intent intent = new Intent(context, AddNewScoreScreen.class);

        intent.putExtra("ca.cmpt276.project.UI.AddNewScoreScreen - timeInSec", timeInSec);
        intent.putExtra("ca.cmpt276.project.UI.AddNewScoreScreen - dateAndTime", dateAndTime);

        return intent;

    }

}