package game.model;

import java.io.Serializable;
import java.util.*;

import game.units.Unit;

public class Player implements Serializable {
    private static final long serialVersionUID = 1L;
    private  String name ;
    private  ArrayList<Unit> hand ;
    private  int numberOfWins;

    public Player(String name) {
      this.name = name;
      this.hand = new ArrayList<Unit>();
      this.numberOfWins = 0;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public ArrayList<Unit> getHand() {
        return hand;
    }
    public void setHand(ArrayList<Unit> hand) {
        this.hand = hand;
    }
    public int getNumberOfWins() {
        return numberOfWins;
    }
    public void setNumberOfWins(int numberOfWins) {
        this.numberOfWins = numberOfWins;
    }
    public void addUnit(Unit unit) {
        this.hand.add(unit);
    }
    public void removeUnit(Unit unit) {
        this.hand.remove(unit);
    }
    public void reset(){
        this.numberOfWins = 0;
    }

}
