package ca.cmpt276.project.UI;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Base64;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import ca.cmpt276.project.Model.GalleryImages;
import ca.cmpt276.project.R;

//This class is the main screen that appears after the welcome screen has
//time out, which is used to navigate between the other activities,
//which include playing the actual game, the help menu, or to view
//the highscores menu.

public class MenuScreen extends AppCompatActivity {

    private boolean actionMade = false;

    private SoundPool soundPool;
    private int start;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_screen);

        setSound();
        setupPlayButton();
        setupHighScoresButton();
        setupHelpButton();
        setupOptionsButton();
        getSavedGalleryImages();
    }

    private void setSound() {
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();

        soundPool = new SoundPool.Builder()
                .setMaxStreams(6)
                .setAudioAttributes(audioAttributes)
                .build();

        start = soundPool.load(this, R.raw.start, 1);
    }

    // method for base64 to bitmap
    public static Bitmap decodeBase64(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }

    private void getSavedGalleryImages() {
        GalleryImages galleryImages = GalleryImages.getInstance();
        SharedPreferences mPrefs = getSharedPreferences("GALLERY", MODE_PRIVATE);

        galleryImages.getGalleryListString().clear();
        galleryImages.getGalleryListDrawable().clear();

        int size = mPrefs.getInt("Size", 0);

        for (int i = 0; i < size; i++) {
            galleryImages.getGalleryListString().add(mPrefs.getString("" + i, null));
            Bitmap bitmap = decodeBase64(galleryImages.getGalleryListString().get(i));
            Drawable drawable = new BitmapDrawable(getResources(), bitmap);
            galleryImages.getGalleryListDrawable().add(drawable);
        }
    }

    private void setupPlayButton() {
        Button btn = findViewById(R.id.play_button);

        int width = btn.getLayoutParams().width;
        int height = btn.getLayoutParams().height;
        Bitmap originalBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.play_text);
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(originalBitmap, width, height, true);
        btn.setBackground(new BitmapDrawable(getResources(), scaledBitmap));

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!actionMade){
                    actionMade = true;
                    YoYo.with(Techniques.FadeOut).duration(1000).playOn(findViewById(R.id.help_button));
                    YoYo.with(Techniques.FadeOut).duration(1000).playOn(findViewById(R.id.high_scores_button));
                    YoYo.with(Techniques.FadeOut).duration(1000).playOn(findViewById(R.id.options_button));
                    new CountDownTimer(1000, 1000) {
                        public void onFinish() {
                            soundPool.play(start, 1, 1, 0, 0, 1);
                            Intent intent = GameScreen.makeIntent(MenuScreen.this);
                            startActivity(intent);
                            YoYo.with(Techniques.FadeIn).duration(2000).playOn(findViewById(R.id.help_button));
                            YoYo.with(Techniques.FadeIn).duration(2000).playOn(findViewById(R.id.high_scores_button));
                            YoYo.with(Techniques.FadeIn).duration(2000).playOn(findViewById(R.id.options_button));
                            actionMade = false;
                        }
                        public void onTick(long millisUntilFinished) {}
                    }.start();
                }
            }
        });
    }

    private void setupHelpButton() {
        Button btn = findViewById(R.id.help_button);

        int width = btn.getLayoutParams().width;
        int height = btn.getLayoutParams().height;
        Bitmap originalBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.help_text);
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(originalBitmap, width, height, true);
        btn.setBackground(new BitmapDrawable(getResources(), scaledBitmap));

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!actionMade){
                    actionMade = true;
                    YoYo.with(Techniques.FadeOut).duration(1000).playOn(findViewById(R.id.play_button));
                    YoYo.with(Techniques.FadeOut).duration(1000).playOn(findViewById(R.id.high_scores_button));
                    YoYo.with(Techniques.FadeOut).duration(1000).playOn(findViewById(R.id.options_button));
                    new CountDownTimer(1000, 1000) {
                        public void onFinish() {
                            Intent intent = HelpScreen.makeIntent(MenuScreen.this);
                            startActivity(intent);
                            YoYo.with(Techniques.FadeIn).duration(2000).playOn(findViewById(R.id.play_button));
                            YoYo.with(Techniques.FadeIn).duration(2000).playOn(findViewById(R.id.high_scores_button));
                            YoYo.with(Techniques.FadeIn).duration(2000).playOn(findViewById(R.id.options_button));
                            actionMade = false;
                        }
                        public void onTick(long millisUntilFinished) {}
                    }.start();
                }
            }
        });
    }

    private void setupHighScoresButton() {
        Button btn = findViewById(R.id.high_scores_button);

        int width = btn.getLayoutParams().width;
        int height = btn.getLayoutParams().height;
        Bitmap originalBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.high_scores_text);
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(originalBitmap, width, height, true);
        btn.setBackground(new BitmapDrawable(getResources(), scaledBitmap));

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!actionMade){
                    actionMade = true;
                    YoYo.with(Techniques.FadeOut).duration(1000).playOn(findViewById(R.id.play_button));
                    YoYo.with(Techniques.FadeOut).duration(1000).playOn(findViewById(R.id.help_button));
                    YoYo.with(Techniques.FadeOut).duration(1000).playOn(findViewById(R.id.options_button));
                    new CountDownTimer(1000, 1000) {
                        public void onFinish() {
                            Intent intent = HighScoreScreen.makeIntent(MenuScreen.this);
                            startActivity(intent);
                            YoYo.with(Techniques.FadeIn).duration(2000).playOn(findViewById(R.id.play_button));
                            YoYo.with(Techniques.FadeIn).duration(2000).playOn(findViewById(R.id.help_button));
                            YoYo.with(Techniques.FadeIn).duration(2000).playOn(findViewById(R.id.options_button));
                            actionMade = false;
                        }
                        public void onTick(long millisUntilFinished) {}
                    }.start();
                }
            }
        });
    }

    private void setupOptionsButton() {
        Button btn = findViewById(R.id.options_button);

        int width = btn.getLayoutParams().width;
        int height = btn.getLayoutParams().height;
        Bitmap originalBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.options_text);
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(originalBitmap, width, height, true);
        btn.setBackground(new BitmapDrawable(getResources(), scaledBitmap));

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!actionMade){
                    actionMade = true;
                    YoYo.with(Techniques.FadeOut).duration(1000).playOn(findViewById(R.id.play_button));
                    YoYo.with(Techniques.FadeOut).duration(1000).playOn(findViewById(R.id.help_button));
                    YoYo.with(Techniques.FadeOut).duration(1000).playOn(findViewById(R.id.high_scores_button));
                    new CountDownTimer(1000, 1000) {
                        public void onFinish() {
                            Intent intent = OptionsScreen.makeIntent(MenuScreen.this);
                            startActivity(intent);
                            YoYo.with(Techniques.FadeIn).duration(2000).playOn(findViewById(R.id.play_button));
                            YoYo.with(Techniques.FadeIn).duration(2000).playOn(findViewById(R.id.help_button));
                            YoYo.with(Techniques.FadeIn).duration(2000).playOn(findViewById(R.id.high_scores_button));
                            actionMade = false;
                        }
                        public void onTick(long millisUntilFinished) {}
                    }.start();
                }
            }
        });
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, MenuScreen.class);
    }

}