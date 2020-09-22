package ca.cmpt276.project.UI;

//This class is used as the UI for the search engine, as in
//this class shows whats on the web on Flickr

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ca.cmpt276.project.Flickr.SelectedImages;
import ca.cmpt276.project.Model.ViewAdapter;
import ca.cmpt276.project.R;

public class DownloadedImagesScreen extends AppCompatActivity {
    RecyclerView recyclerView;
    SelectedImages s= SelectedImages.getInstance();
    List<Drawable> items = s.getSelectedView();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_downloaded_images_screen);

        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(DownloadedImagesScreen.this, 3));
        recyclerView.setAdapter(new ViewAdapter(this,items));
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, DownloadedImagesScreen.class);
    }
}

