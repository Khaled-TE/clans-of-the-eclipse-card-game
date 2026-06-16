package game.model;
import java.io.Serializable;
import java.util.*;

import game.enums.BonusCase;
public class RoundRecord implements Serializable {
    private static final long serialVersionUID = 1L;
    private int roundNumber;
    //private RoundPhase roundPhase;
    private Map<Player,Integer> prophecy;
    private Map<Player,Integer> wonBattles;
    private Map<Player,ArrayList<BonusCase>> bonusMap;
    private Map<Player,Integer> baseRecord;
    private Map<Player,Integer> bonusRecord;
    private ArrayList<Battle> battleRecord;

    public RoundRecord(int roundNumber) {
        this.roundNumber = roundNumber;
        //this.roundPhase = RoundPhase.DRAW;
        this.battleRecord= new ArrayList<>();
        this.prophecy = new HashMap<>();
        this.wonBattles = new HashMap<>();
        this.baseRecord = new HashMap<>();
        this.bonusRecord = new HashMap<>();
        this.bonusMap = new HashMap<>();
    }

    public boolean hasProphecy(Player player) {
        return prophecy.containsKey(player);
    }
    
    public int getProphecy(Player player) {
        if(prophecy.containsKey(player))
            return prophecy.get(player);
        else 
            return 0;
    }

    public void setProphecy(Player player, int prop) {
        prophecy.put(player, prop);
    }

    public int getRoundNumber() {
        return roundNumber;
    }

    public ArrayList<Battle> getBattleRecord() {
        return battleRecord;
    }

    public void setBattleRecord(ArrayList<Battle> battleRecord) {
        this.battleRecord = battleRecord;
    }

    public int getWonBattles(Player p){
        if(wonBattles.containsKey(p))
            return wonBattles.get(p);
        return 0;
    }

    public void setWonBattles(Player p , int n){
        wonBattles.put(p,n);
    }

    public Map<Player , Integer> getProphecyMap(){
        return prophecy;
    }

    public ArrayList<BonusCase> getBonusCases(Player p){
        if(bonusMap.containsKey(p))
            return bonusMap.get(p);
        return new ArrayList<BonusCase>();
    }

    public void addBonusCase(Player p,BonusCase bc){

        if(bonusMap.containsKey(p)){
            bonusMap.get(p).add(bc);
            return ;
        }

        ArrayList<BonusCase> ar = new ArrayList<>();
        ar.add(bc);
        bonusMap.put(p,ar);
    }

    public int getPlayerBaseScore(Player p){
        if(baseRecord.containsKey(p))
            return baseRecord.get(p);
        return -1;
    }

    public void setPlayerBaseScore(Player p , int s){
        baseRecord.put(p,s);
    }

    public int getPlayerBonusScore(Player p){
        if(bonusRecord.containsKey(p))
            return bonusRecord.get(p);
        return 0;
    }

    public void setPlayerBonusScore(Player p , int s){
        bonusRecord.put(p,s);
    }

    public void addBattle(Battle battle){
        battleRecord.add(battle);
    }



    /*public RoundPhase getRoundPhase() {
        return roundPhase;
    }

    public void setRoundPhase(RoundPhase roundPhase) {
        this.roundPhase = roundPhase;
    } 
    public Player getWinner() {
        return winner;
    }

    public void setWinner(Player winner) {
        this.winner = winner;
    }*/

}