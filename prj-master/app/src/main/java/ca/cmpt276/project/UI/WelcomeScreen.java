package ca.cmpt276.project.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import ca.cmpt276.project.Model.GameOptions;
import ca.cmpt276.project.Model.HighScores;
import ca.cmpt276.project.R;

//This class is the welcome screen, which displays as soon as the app is
//launched, and times out after several seconds or after the user
//hits the skip button

public class WelcomeScreen extends AppCompatActivity {

    private boolean skipped = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen);

        populateHighScores();
        setupSkipButton();
        autoAdvance();

    }

    private void populateHighScores() {
        HighScores highScoresInst = HighScores.getInstanceOrderTwoDrawZero();
        highScoresInst.resetHighScores();
        highScoresInst = HighScores.getInstanceOrderTwoDrawFive();
        highScoresInst.resetHighScores();

        highScoresInst = HighScores.getInstanceOrderThreeDrawZero();
        highScoresInst.resetHighScores();
        highScoresInst = HighScores.getInstanceOrderThreeDrawFive();
        highScoresInst.resetHighScores();
        highScoresInst = HighScores.getInstanceOrderThreeDrawTen();
        highScoresInst.resetHighScores();

        highScoresInst = HighScores.getInstanceOrderFiveDrawZero();
        highScoresInst.resetHighScores();
        highScoresInst = HighScores.getInstanceOrderFiveDrawFive();
        highScoresInst.resetHighScores();
        highScoresInst = HighScores.getInstanceOrderFiveDrawTen();
        highScoresInst.resetHighScores();
        highScoresInst = HighScores.getInstanceOrderFiveDrawFifteen();
        highScoresInst.resetHighScores();
        highScoresInst = HighScores.getInstanceOrderFiveDrawTwenty();
        highScoresInst.resetHighScores();
    }

    private void setupSkipButton() {
        Button btn = findViewById(R.id.skip_button);

        int width = btn.getLayoutParams().width;
        int height = btn.getLayoutParams().height;
        Bitmap originalBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.skip_text);
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(originalBitmap, width, height, true);
        btn.setBackground(new BitmapDrawable(getResources(), scaledBitmap));

        btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                skipped = true;
                Intent intent = MenuScreen.makeIntent(WelcomeScreen.this);
                startActivity(intent);
                finish();
            }
        });

    }

    private void autoAdvance(){
        animate();
        new CountDownTimer(10000, 1000) {
            public void onFinish() {
                if(!skipped){
                    Intent intent = MenuScreen.makeIntent(WelcomeScreen.this);
                    startActivity(intent);
                    finish();
                }
            }
            public void onTick(long millisUntilFinished) {}
        }.start();
    }

    private void animate(){
        YoYo.with(Techniques.BounceInLeft).duration(3000).playOn(findViewById(R.id.authors));
        YoYo.with(Techniques.BounceInLeft).duration(3000).playOn(findViewById(R.id.welcome_to));
        YoYo.with(Techniques.BounceInRight).duration(3000).playOn(findViewById(R.id.game_title));
        YoYo.with(Techniques.BounceInRight).duration(3000).playOn(findViewById(R.id.skip_button));
        YoYo.with(Techniques.FadeIn).duration(3000).playOn(findViewById(R.id.planet_1));
        YoYo.with(Techniques.FadeIn).duration(3000).playOn(findViewById(R.id.planet_2));
        YoYo.with(Techniques.FadeIn).duration(3000).playOn(findViewById(R.id.planet_3));

        new CountDownTimer(5000, 1000) {
            public void onFinish() {
                YoYo.with(Techniques.FadeOutLeft).duration(5000).playOn(findViewById(R.id.authors));
                YoYo.with(Techniques.FadeOutLeft).duration(5000).playOn(findViewById(R.id.welcome_to));
                YoYo.with(Techniques.FadeOutRight).duration(5000).playOn(findViewById(R.id.game_title));
                YoYo.with(Techniques.FadeOutRight).duration(5000).playOn(findViewById(R.id.skip_button));
                YoYo.with(Techniques.FadeOutRight).duration(5000).playOn(findViewById(R.id.planet_1));
                YoYo.with(Techniques.FadeOutLeft).duration(5000).playOn(findViewById(R.id.planet_2));
                YoYo.with(Techniques.FadeOutUp).duration(5000).playOn(findViewById(R.id.planet_3));
            }
            public void onTick(long millisUntilFinished) {}
        }.start();

    }



}