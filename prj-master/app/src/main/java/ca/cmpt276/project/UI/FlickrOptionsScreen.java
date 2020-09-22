package ca.cmpt276.project.UI;

//This class is used to setup the other Flickr related implementations regarding the actual gameplay,
//which includes being able to add to a card set, being able to remove from a card set, and being able
//to currently view what's in the card set

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import ca.cmpt276.project.Flickr.PhotoGalleryActivity;
import ca.cmpt276.project.R;

public class FlickrOptionsScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flickr);

        setupAddImagesButton();
        setupRemoveImagesButton();
        setupViewImagesButton();
        setupBackButton();
    }

    private  void setupAddImagesButton() {
        Button btn = findViewById(R.id.addBtn);

        int width = btn.getLayoutParams().width;
        int height = btn.getLayoutParams().height;
        Bitmap originalBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.add_images_text);
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(originalBitmap, width, height, true);
        btn.setBackground(new BitmapDrawable(getResources(), scaledBitmap));

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = PhotoGalleryActivity.makeIntent(FlickrOptionsScreen.this);
                startActivity(intent);
            }
        });

    }

    private  void setupViewImagesButton() {
        Button btn = findViewById(R.id.viewBtn);

        int width = btn.getLayoutParams().width;
        int height = btn.getLayoutParams().height;
        Bitmap originalBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.view_images_text);
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(originalBitmap, width, height, true);
        btn.setBackground(new BitmapDrawable(getResources(), scaledBitmap));

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = DownloadedImagesScreen.makeIntent(FlickrOptionsScreen.this);
                startActivity(intent);
            }
        });

    }
    private void setupRemoveImagesButton() {
        Button btn = findViewById(R.id.removeBtn);

        int width = btn.getLayoutParams().width;
        int height = btn.getLayoutParams().height;
        Bitmap originalBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.remove_images_text);
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(originalBitmap, width, height, true);
        btn.setBackground(new BitmapDrawable(getResources(), scaledBitmap));

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = RemoveImagesScreen.makeIntent(FlickrOptionsScreen.this);
                startActivity(intent);
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
        return new Intent(context, FlickrOptionsScreen.class);

    }
}