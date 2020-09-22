package ca.cmpt276.project.Model;

import android.annotation.SuppressLint;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

//This class is responsible for viewing the user's high scores
//and having default values for the user to try and beat

public class HighScores {

    private static HighScores highScoresInstOrderTwoDrawZero;
    private static HighScores highScoresInstOrderTwoDrawFive;

    private static HighScores highScoresInstOrderThreeDrawZero;
    private static HighScores highScoresInstOrderThreeDrawFive;
    private static HighScores highScoresInstOrderThreeDrawTen;

    private static HighScores highScoresInstOrderFiveDrawZero;
    private static HighScores highScoresInstOrderFiveDrawFive;
    private static HighScores highScoresInstOrderFiveDrawTen;
    private static HighScores highScoresInstOrderFiveDrawFifteen;
    private static HighScores highScoresInstOrderFiveDrawTwenty;

    private ArrayList<Score> highScores = new ArrayList<>();

    private HighScores(){}

    public static HighScores getInstanceOrderTwoDrawZero(){
        if(highScoresInstOrderTwoDrawZero == null){
            highScoresInstOrderTwoDrawZero = new HighScores();
        }
        return highScoresInstOrderTwoDrawZero;
    }
    public static HighScores getInstanceOrderTwoDrawFive(){
        if(highScoresInstOrderTwoDrawFive == null){
            highScoresInstOrderTwoDrawFive = new HighScores();
        }
        return highScoresInstOrderTwoDrawFive;
    }

    public static HighScores getInstanceOrderThreeDrawZero(){
        if(highScoresInstOrderThreeDrawZero == null){
            highScoresInstOrderThreeDrawZero = new HighScores();
        }
        return highScoresInstOrderThreeDrawZero;
    }
    public static HighScores getInstanceOrderThreeDrawFive(){
        if(highScoresInstOrderThreeDrawFive == null){
            highScoresInstOrderThreeDrawFive = new HighScores();
        }
        return highScoresInstOrderThreeDrawFive;
    }
    public static HighScores getInstanceOrderThreeDrawTen(){
        if(highScoresInstOrderThreeDrawTen == null){
            highScoresInstOrderThreeDrawTen = new HighScores();
        }
        return highScoresInstOrderThreeDrawTen;
    }

    public static HighScores getInstanceOrderFiveDrawZero(){
        if(highScoresInstOrderFiveDrawZero == null){
            highScoresInstOrderFiveDrawZero = new HighScores();
        }
        return highScoresInstOrderFiveDrawZero;
    }
    public static HighScores getInstanceOrderFiveDrawFive(){
        if(highScoresInstOrderFiveDrawFive == null){
            highScoresInstOrderFiveDrawFive = new HighScores();
        }
        return highScoresInstOrderFiveDrawFive;
    }
    public static HighScores getInstanceOrderFiveDrawTen(){
        if(highScoresInstOrderFiveDrawTen == null){
            highScoresInstOrderFiveDrawTen = new HighScores();
        }
        return highScoresInstOrderFiveDrawTen;
    }
    public static HighScores getInstanceOrderFiveDrawFifteen(){
        if(highScoresInstOrderFiveDrawFifteen == null){
            highScoresInstOrderFiveDrawFifteen = new HighScores();
        }
        return highScoresInstOrderFiveDrawFifteen;
    }
    public static HighScores getInstanceOrderFiveDrawTwenty(){
        if(highScoresInstOrderFiveDrawTwenty == null){
            highScoresInstOrderFiveDrawTwenty = new HighScores();
        }
        return highScoresInstOrderFiveDrawTwenty;
    }

    public void addScore(Score newScore){

        boolean scoreInserted = false;

        for(int i = 0; i < highScores.size(); i++){
            if(newScore.getTimeInSec() < highScores.get(i).getTimeInSec()){
                highScores.add(i, newScore);
                scoreInserted = true;
                break;
            }
        }

        if(!scoreInserted){
            highScores.add(highScores.size(), newScore);
        }

        while(highScores.size() > 5){
            highScores.remove(5);
        }
    }

    public ArrayList<Score> getHighScores(){
        return highScores;
    }

    public void clearHighScores(){
        highScores = new ArrayList<>();
    }

    public void resetHighScores(){
        @SuppressLint("SimpleDateFormat")
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Calendar cal = Calendar.getInstance();

        highScores.add(0, new Score("Pre-populated Score", 10,
                dateFormat.format(cal.getTime())));
        highScores.add(1, new Score("Pre-populated Score", 13,
                dateFormat.format(cal.getTime())));
        highScores.add(2, new Score("Pre-populated Score", 17,
                dateFormat.format(cal.getTime())));
        highScores.add(3, new Score("Pre-populated Score", 20,
                dateFormat.format(cal.getTime())));
        highScores.add(4, new Score("Pre-populated Score", 25,
                dateFormat.format(cal.getTime())));

        while(highScores.size() > 5){
            highScores.remove(5);
        }
    }

}
