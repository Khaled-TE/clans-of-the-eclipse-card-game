package game.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Battle implements Serializable {
    private static final long serialVersionUID = 1L;
    private int battleNumber;
    private int leaderUnitIndex;
    private ArrayList<PlayedUnit> playedUnits;
    private Player winner;
    private boolean leviathan;
    //private ArrayList<Player> players;
    
    public Battle(int battleNumber, ArrayList<PlayedUnit> playedUnits, int leaderUnitIndex) {
        this.battleNumber= battleNumber;
        this.playedUnits =playedUnits;
        this.leaderUnitIndex = leaderUnitIndex;
        this.winner = null;
        this.leviathan = false;
    }

    public boolean isLeviathan() {
        return leviathan;
    }

    public void setLeviathan(boolean leviathan) {
        this.leviathan = leviathan;
    }

    public Player getWinner() {
        return winner;
    }

    public void setWinner(Player winner) {
        this.winner = winner;
    }


     public ArrayList<PlayedUnit> getPlayedUnits() {
        return playedUnits;
    }

    public void setPlayedUnits(ArrayList<PlayedUnit> playedUnits) {
        this.playedUnits = playedUnits;
    }

    public int getBattleNumber() {
        return battleNumber;
    }

    public void setBattleNumber(int battleNumber) {
        this.battleNumber = battleNumber;
    }

    public int getLeaderUnitIndex() {
        return leaderUnitIndex;
    }

    public void setLeaderUnitIndex(int leaderUnitIndex) {
        this.leaderUnitIndex = leaderUnitIndex;
    }

    


}
