package game.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ScoreSheet implements Serializable {
    private static final long serialVersionUID = 1L;
        private ArrayList<RoundRecord> roundRecords;
        private Map<Player , Integer> totalScoreMap;

    public ScoreSheet(ArrayList<Player> players) {
        roundRecords = new ArrayList<>();
        totalScoreMap = new HashMap<>();
        for(Player p : players)
            totalScoreMap.put(p , 0);
    }

    public void addRoundRecord(RoundRecord r){
        roundRecords.add(r);
        Map<Player , Integer> playersMap = r.getProphecyMap();
        for(Player p : playersMap.keySet()){
            addScore(p , r.getPlayerBaseScore(p) + r.getPlayerBonusScore(p));
        }
    }

    public ArrayList<RoundRecord> getRoundRecords(){
        return roundRecords;
    }

    public void addScore(Player p , int score){
        totalScoreMap.put(p , totalScoreMap.get(p)+score );
    }

    public Map<Player,Integer> getTotalScoreMap(){
        return totalScoreMap;
    }

    

    public ArrayList<Player> getWinners(){
        ArrayList<Player> winners = new ArrayList<>();
        int biggestScore = -1000000000;
        for(Player p :totalScoreMap.keySet()){
            if(totalScoreMap.get(p) > biggestScore){
                biggestScore = totalScoreMap.get(p);
            }
        }
        for(Player p :totalScoreMap.keySet()){
            if(totalScoreMap.get(p) == biggestScore)
                winners.add(p);
        }
        return winners;
    }

}
