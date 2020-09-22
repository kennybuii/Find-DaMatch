package ca.cmpt276.project.UI;

//This class is used to upload images from the photo gallery to the UI.
//It also is used to save the images so that the user does not have to
//reselect their images between app executions

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import ca.cmpt276.project.Model.GalleryImages;
import ca.cmpt276.project.R;

public class UploadImgOptionsScreen extends AppCompatActivity {

    int PICK_IMAGE_MULTIPLE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_img_options_screen);

        setupSaveButton();
        setupBackButton();
        setupUploadImgButton();
        setupViewImgButton();
        setupRemoveImgButton();

    }

    //Taken from https://stackoverflow.com/questions/18072448/how-to-save-image-in-shared-preference-in-android-shared-preference-issue-in-a
    // method for bitmap to base64
    public static String encodeTobase64(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);

        Log.d("Image Log:", imageEncoded);
        return imageEncoded;
    }

    private void setupSaveButton() {
        final GalleryImages galleryImages = GalleryImages.getInstance();
        Button btn = findViewById(R.id.save_button);

        int width = btn.getLayoutParams().width;
        int height = btn.getLayoutParams().height;
        Bitmap originalBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.save_text);
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(originalBitmap, width, height, true);
        btn.setBackground(new BitmapDrawable(getResources(), scaledBitmap));

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                List<String> myObject = galleryImages.getGalleryListString();
                SharedPreferences mPrefs = getSharedPreferences("GALLERY", MODE_PRIVATE);
                SharedPreferences.Editor prefsEditor = mPrefs.edit();

                prefsEditor.putInt("Size", myObject.size());

                for (int i = 0; i< myObject.size(); i ++ ){
                    prefsEditor.remove("" + i);
                    prefsEditor.putString("" + i, myObject.get(i));
                }
                prefsEditor.apply();

                Toast.makeText(UploadImgOptionsScreen.this, "Images from Photos App saved to card set", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void setupBackButton() {
        Button btn = (Button)findViewById(R.id.back_button);

        int width = btn.getLayoutParams().width;
        int height = btn.getLayoutParams().height;
        Bitmap originalBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.back_text);
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(originalBitmap, width, height, true);
        btn.setBackground(new BitmapDrawable(getResources(), scaledBitmap));

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void setupUploadImgButton() {
        Button btn = (Button)findViewById(R.id.upload_image_button);

        int width = btn.getLayoutParams().width;
        int height = btn.getLayoutParams().height;
        Bitmap originalBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.upload_images_text);
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(originalBitmap, width, height, true);
        btn.setBackground(new BitmapDrawable(getResources(), scaledBitmap));

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"),
                        PICK_IMAGE_MULTIPLE);

            }
        });
    }

    //Taken from https://stackoverflow.com/questions/27372106/pick-multiple-images-from-gallery to pick multiple images from Photos Gallery
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub

        if(requestCode==PICK_IMAGE_MULTIPLE){
            if(resultCode==RESULT_OK){
                GalleryImages galleryImages = GalleryImages.getInstance();
                //If Single image selected then it will fetch from Gallery
                if(data.getData()!=null){
                    Uri uri=data.getData();
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                        Drawable drawable = new BitmapDrawable(getResources(), bitmap);
                        String string = encodeTobase64(bitmap);
                        galleryImages.getGalleryListString().add(string);
                        galleryImages.getGalleryListDrawable().add(drawable);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    if(data.getClipData()!=null){
                        ClipData mClipData=data.getClipData();

                        for(int i=0; i<mClipData.getItemCount(); i++){

                            ClipData.Item item = mClipData.getItemAt(i);
                            Uri uri = item.getUri();
                            try {
                                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                                Drawable drawable = new BitmapDrawable(getResources(), bitmap);
                                String string = encodeTobase64(bitmap);

                                galleryImages.getGalleryListDrawable().add(drawable);
                                galleryImages.getGalleryListString().add(string);

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        Log.v("LOG_TAG", "Selected Images: "+ galleryImages.getGalleryListDrawable().size());
                    }
                }
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void setupViewImgButton() {
        Button btn = (Button)findViewById(R.id.view_images_button);

        int width = btn.getLayoutParams().width;
        int height = btn.getLayoutParams().height;
        Bitmap originalBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.view_images_text);
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(originalBitmap, width, height, true);
        btn.setBackground(new BitmapDrawable(getResources(), scaledBitmap));

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = ViewUploadedScreen.makeIntent(UploadImgOptionsScreen.this);
                startActivity(intent);
            }
        });
    }

    private void setupRemoveImgButton() {
        Button btn = (Button)findViewById(R.id.remove_image_button);

        int width = btn.getLayoutParams().width;
        int height = btn.getLayoutParams().height;
        Bitmap originalBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.remove_images_text);
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(originalBitmap, width, height, true);
        btn.setBackground(new BitmapDrawable(getResources(), scaledBitmap));

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = RemoveUploadedScreen.makeIntent(UploadImgOptionsScreen.this);
                startActivity(intent);
            }
        });
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, UploadImgOptionsScreen.class);
    }
}