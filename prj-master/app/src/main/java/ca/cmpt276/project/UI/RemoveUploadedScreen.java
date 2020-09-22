package ca.cmpt276.project.UI;

//This class is used to remove images from the photo gallery selection screen

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.Toast;

import java.util.List;

import ca.cmpt276.project.Model.GalleryImages;
import ca.cmpt276.project.Model.RemoveAdapter;
import ca.cmpt276.project.R;

public class RemoveUploadedScreen extends AppCompatActivity implements RemoveAdapter.OnNoteListener {
    RecyclerView recyclerView;
    GalleryImages galleryImages = GalleryImages.getInstance();
    List<Drawable> items = galleryImages.getGalleryListDrawable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove_uploaded_screen);

        recyclerView = (RecyclerView)findViewById(R.id.my_recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(RemoveUploadedScreen.this, 3));
        recyclerView.setAdapter(new RemoveAdapter(this, items,this));
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, RemoveUploadedScreen.class);
    }

    @Override
    public void onNoteClick(int position) {
        Toast.makeText(RemoveUploadedScreen.this, "The image selected was deleted", Toast.LENGTH_SHORT).show();
        galleryImages.getGalleryListDrawable().remove(position);
        galleryImages.getGalleryListString().remove(position);
        finish();

    }
}