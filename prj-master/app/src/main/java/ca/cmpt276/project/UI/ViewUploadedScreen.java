package ca.cmpt276.project.UI;

//This class is used to view the uploaded images from the photo gallery ui

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import java.util.List;

import ca.cmpt276.project.Model.GalleryImages;
import ca.cmpt276.project.Model.ViewAdapter;
import ca.cmpt276.project.R;

public class ViewUploadedScreen extends AppCompatActivity {

    RecyclerView recyclerView;
    GalleryImages galleryImages = GalleryImages.getInstance();
    List<Drawable> items = galleryImages.getGalleryListDrawable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_uploaded_screen);

        recyclerView = (RecyclerView)findViewById(R.id.my_recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(ViewUploadedScreen.this, 3));
        recyclerView.setAdapter(new ViewAdapter(this, items));
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, ViewUploadedScreen.class);
    }
}