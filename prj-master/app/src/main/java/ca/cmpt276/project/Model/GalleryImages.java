package ca.cmpt276.project.Model;

//This class is a singleton model used to save images from the Photo
//Gallery application

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;


import java.util.ArrayList;
import java.util.List;

public class GalleryImages {

    private List<Drawable> galleryListDrawable = new ArrayList<>();
    private List<String> galleryListString = new ArrayList<>();
    public List<String> getGalleryListString() {
        return galleryListString;
    }
    public List<Drawable> getGalleryListDrawable() {
        return galleryListDrawable;
    }

    private static GalleryImages instance;
    private GalleryImages(){};
    public static GalleryImages getInstance() {
        if(instance == null) {
            instance = new GalleryImages();
        }
        return instance;
    }
}
