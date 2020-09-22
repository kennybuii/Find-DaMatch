package ca.cmpt276.project.Flickr;

//This class is used to save the last search the user made in FlickrFetchr
//so that it appears for the next startup as well

import android.content.Context;

import ca.cmpt276.project.Model.CardOptionsData;

public class GalleryItem {
    private static GalleryItem instance;
    private String mCaption;
    private String mId;
    private String mUrl;

    public String getmUrl() {
        return mUrl;
    }

    public void setmUrl(String mUrl) {
        this.mUrl = mUrl;
    }

    public String getmCaption() {
        return mCaption;
    }

    public void setmCaption(String mCaption) {
        this.mCaption = mCaption;
    }

    public String getmId() {
        return mId;
    }

    public void setmId(String mId) {
        this.mId = mId;
    }

    @Override
    public String toString() {
        return mCaption;
    }

    public static GalleryItem getInstance() {
        if(instance == null) {
            instance = new GalleryItem();
        }
        return instance;
    }

}
