package ca.cmpt276.project.Model;

//This class is used to decide which card set the user wanted to select
//so it can be accessed by any activity through singleton use
//it is also used to save bitmaps from external storage

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import java.util.ArrayList;
import java.util.List;

public class CardOptionsData {

    private int requestedCardSet;
    private int cardSet;
    private List<Drawable> imagesList = new ArrayList<>();
    private List<Bitmap> bitmapList = new ArrayList<>();

    public List<Bitmap> getCardList() {
        return cardList;
    }

    private List<Bitmap> cardList = new ArrayList<>();

    public void addCardlist(Bitmap bitmap) {
        cardList.add(bitmap);
    }

    public List<Bitmap> getBitmapList() {
        return bitmapList;
    }
    public List<Drawable> getImagesList() {
        return imagesList;
    }

    private static CardOptionsData instance;
    private CardOptionsData() {};

    public int getRequestedCardSet() {
        return requestedCardSet;
    }
    public int getCardSet() {
        return cardSet;
    }

    public void requestCardSet(int cardSet) {
        this.requestedCardSet = cardSet;
    }

    public void setCardSet(int cardSet) {
        this.cardSet = cardSet;
    }

    public static CardOptionsData getInstance() {
        if(instance == null) {
            instance = new CardOptionsData();
        }
        return instance;
    }
}
