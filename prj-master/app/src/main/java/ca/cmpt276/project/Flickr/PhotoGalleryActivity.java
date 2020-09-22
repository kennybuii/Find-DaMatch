package ca.cmpt276.project.Flickr;

//This class is supposed to handle setting up the UI interface, but is instead extended
//to SingleFragmentActivity as a subclass to handle

import android.content.Context;
import android.content.Intent;


import androidx.fragment.app.Fragment;

public class PhotoGalleryActivity extends SingleFragmentActivity {

    @Override

    protected Fragment createFragment() {
        return PhotoGalleryFragment.newInstance();
    }
    public static Intent makeIntent(Context context) {
        return new Intent(context, PhotoGalleryActivity.class);
    }
}