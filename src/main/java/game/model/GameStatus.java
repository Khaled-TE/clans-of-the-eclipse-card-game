package game.model;
import java.io.Serializable;
import java.util.*;

import game.enums.RoundPhase;
public class GameStatus implements Serializable {
    private static final long serialVersionUID = 1L;
    private ArrayList<Player> players;
    private int roundNumber;
    private ArrayList<RoundRecord> roundRecords;
    private int winner;
    private RoundPhase phase;
    private int turn;
    private int currentBattleNumber = 1;

    public GameStatus( List<Player> players ) {
        this.phase = RoundPhase.DRAW;
        this.roundNumber = 1;
        this.winner = (int) (Math.random()*2);
        this.players = new ArrayList<>(players);
        this.roundRecords = new ArrayList<>();
        turn = 0;
    }

    public RoundPhase getPhase() {
        return phase;
    }

    public void setPhase(RoundPhase phase) {
        this.phase = phase;
    }

    public int getWinner() {
        return winner;
    }

    public void setWinner(int winner) {
        this.winner = winner;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public void setPlayers(ArrayList<Player> players) {
        this.players = players;
    }

    public List<RoundRecord> getRoundRecords() {
        return roundRecords;
    }

    public void setRoundRecords(ArrayList<RoundRecord> roundRecords) {
        this.roundRecords = roundRecords;
    }

    public int getRoundNumber() {
        return roundNumber;
    }

    public void setRoundNumber(int roundNumber) {
        this.roundNumber = roundNumber;
    }

    public void addRoundRecord(RoundRecord round) {
        roundRecords.add(round);
    }

    public RoundRecord getCurrentRound() {
        return roundRecords.get(roundRecords.size() - 1);
    }

    public int getTurn() {
        return turn;
    }

    public void setTurn(int x){
        turn = x;
    }

    public void moveTurn(){
        turn++;
        turn %= players.size();
    }
    public int getCurrentBattleNumber() { return currentBattleNumber; }
    public void setCurrentBattleNumber(int n) { this.currentBattleNumber = n; }

}
