package ca.cmpt276.project.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import ca.cmpt276.project.Model.GameOptions;
import ca.cmpt276.project.Model.HighScores;
import ca.cmpt276.project.Model.Score;
import ca.cmpt276.project.R;

//This class is the UI interface to the highscores and score classes listed
//in the Model package. It is responsible for connecting all the scores
//and keeping the list updated for the user to see or reset the list
//if wanted

public class HighScoreScreen extends AppCompatActivity {

    private ArrayList<TextView> textViewList = new ArrayList<>();
    private HighScores highScoresInst;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_score_screen);

        setHighScoreInstance();
        setupTextViewList();
        updateHighScoreDisplay();
        setupClearButton();
        setupResetButton();
        setupBackButton();
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

    private void setupTextViewList() {

        textViewList.add((TextView) findViewById(R.id.high_score_1));
        textViewList.add((TextView) findViewById(R.id.high_score_2));
        textViewList.add((TextView) findViewById(R.id.high_score_3));
        textViewList.add((TextView) findViewById(R.id.high_score_4));
        textViewList.add((TextView) findViewById(R.id.high_score_5));
    }

    @SuppressLint("SetTextI18n")
    private void updateHighScoreDisplay() {
        ArrayList<Score> highScores = highScoresInst.getHighScores();

        for(int i = 0; i < highScores.size(); i++){
            textViewList.get(i).setText((i+1)+". "+highScores.get(i).displayScore());
        }
    }

    private void setupClearButton() {
        Button btn = findViewById(R.id.clear_button);

        int width = btn.getLayoutParams().width;
        int height = btn.getLayoutParams().height;
        Bitmap originalBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.clear_text);
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(originalBitmap, width, height, true);
        btn.setBackground(new BitmapDrawable(getResources(), scaledBitmap));

        btn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                highScoresInst.clearHighScores();

                for(int i = 0; i < 5; i++){
                    textViewList.get(i).setText("");
                }
            }
        });

    }

    private void setupResetButton() {
        Button btn = findViewById(R.id.reset_button);

        int width = btn.getLayoutParams().width;
        int height = btn.getLayoutParams().height;
        Bitmap originalBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.reset_text);
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(originalBitmap, width, height, true);
        btn.setBackground(new BitmapDrawable(getResources(), scaledBitmap));

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                highScoresInst.resetHighScores();
                updateHighScoreDisplay();
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
                finish();
            }
        });
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, HighScoreScreen.class);
    }

}