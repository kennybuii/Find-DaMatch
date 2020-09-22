package ca.cmpt276.project.Model;

//This class is used to input the user's high scores into the system,
//with the time and day of the score being achieved

public class Score {

    private String playerUsername = "";
    private long timeInSec = 0;
    private String dateAndTime = "";

    public Score(String playerUsername, long timeInSec, String dateAndTime){
        this.playerUsername = playerUsername;
        this.timeInSec = timeInSec;
        this.dateAndTime = dateAndTime;
    }

    public String displayScore(){
        return "Player: " + playerUsername + " -> " + timeInSec + " sec, on " + dateAndTime;
    }

    public String getPlayerUsername() { return playerUsername; }
    public void setPlayerUsername(String playerUsername) { this.playerUsername = playerUsername; }

    public double getTimeInSec() { return timeInSec; }
    public void setTimeInSec(long timeInSec) { this.timeInSec = timeInSec; }

    public String getDateAndTime() { return dateAndTime; }
    public void setDateAndTime(String dateAndTime) { this.dateAndTime = dateAndTime; }

}
