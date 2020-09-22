package ca.cmpt276.project.Flickr;

//This class uses the Singleton Model to save information about the photo
//a user selected in the search activity

import android.graphics.drawable.Drawable;

import java.util.ArrayList;

import java.util.List;

public class SelectedImages {
    private List<Drawable> SelectedView = new ArrayList<>();
    private List<String> URLS = new ArrayList<>();

    private List<Integer> itemsToDelete = new ArrayList<>();

    public List<Integer> getItemsToDelete() {
        return itemsToDelete;
    }

    public void addItem(int x) {
          itemsToDelete.add(x);
    }

    public List<String> getURLS() {
        return URLS;
    }

    public List<Drawable> getSelectedView() {
        return SelectedView;
    }

    public void addSelectedView(Drawable drawable) {
        SelectedView.add(drawable);
    }

    private static SelectedImages instance;
    private SelectedImages(){};

    public static SelectedImages getInstance() {
        if(instance == null) {
            instance = new SelectedImages();
        }
        return instance;
    }

}
