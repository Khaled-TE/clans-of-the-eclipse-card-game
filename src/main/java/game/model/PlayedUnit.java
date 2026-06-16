package game.model;

import java.io.Serializable;
import java.util.ArrayList;

import game.enums.Clan;
import game.enums.DeclaredIdentity;
import game.exceptions.IllegalFollowRuleException;
import game.units.ClanUnit;
import game.units.SpecialUnit;
import game.units.Unit;

public class PlayedUnit implements Serializable {
    private static final long serialVersionUID = 1L;
    private Unit unit;
    private Player player;
    private DeclaredIdentity identity;

    public void validateFollowIfPossibleRule(PlayedUnit firstUnitPlayed , ArrayList<Unit> playerHand) throws IllegalFollowRuleException{
        if(!canFollowRule(firstUnitPlayed, playerHand)){
            throw new IllegalFollowRuleException("That selection doesn't follow the follow if possible rule please select another unit");
        }  
    }

    public boolean canFollowRule(PlayedUnit firstUnitPlayed , ArrayList<Unit> playerHand){
        if(this.unit instanceof SpecialUnit || firstUnitPlayed.getUnit() instanceof SpecialUnit)
            return true;

        ClanUnit currUnit = (ClanUnit) this.unit;
        ClanUnit firsUnit = (ClanUnit) firstUnitPlayed.getUnit();
        Clan battleClan = firsUnit.getClan();
        if(currUnit.getClan() == battleClan)
            return true;

        for(Unit u : playerHand){
            ClanUnit clanedU;
            if(u instanceof ClanUnit){
                clanedU = (ClanUnit) u;
                if(clanedU.getClan() == battleClan)
                    return false;
            }
        }
        return true;
    }

    public PlayedUnit(Unit unit, Player player, DeclaredIdentity identity) {
        this.unit = unit;
        this.player = player;
        this.identity = identity;
    }

    public PlayedUnit(Unit unit, Player player) {
        this.unit = unit;
        this.player = player;
        this.identity = null;
    }

    public Unit getUnit() {
        return unit;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public DeclaredIdentity getIdentity() {
        return identity;
    }

    public void setIdentity(DeclaredIdentity identity) {
        this.identity = identity;
    }
}
