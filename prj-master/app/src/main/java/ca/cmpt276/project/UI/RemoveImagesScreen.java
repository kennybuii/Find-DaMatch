package ca.cmpt276.project.UI;

//This class handles the UI for when the user wants to removes images

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.Toast;

import java.util.List;

import ca.cmpt276.project.Flickr.SelectedImages;
import ca.cmpt276.project.Model.RemoveAdapter;
import ca.cmpt276.project.R;

public class RemoveImagesScreen extends AppCompatActivity implements RemoveAdapter.OnNoteListener {
    private static final String TAG = "hehe";
    RecyclerView recyclerView;
    SelectedImages s= SelectedImages.getInstance();

    List<Drawable> items = s.getSelectedView();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove_images_screen);

        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(RemoveImagesScreen.this, 3));
        recyclerView.setAdapter(new RemoveAdapter(this, items, this));

    }


    public static Intent makeIntent(Context context) {
        return new Intent(context, RemoveImagesScreen.class);
    }

    @Override
    public void onNoteClick(int position) {
        Toast.makeText(RemoveImagesScreen.this, "The image selected was deleted", Toast.LENGTH_SHORT).show();
        s.getSelectedView().remove(position);
        finish();

    }
}