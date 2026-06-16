package game.units;

import game.enums.Clan;
import game.enums.UnitType;
import game.model.Battle;
import game.model.PlayedUnit;



public class ClanUnit extends Unit {
    private static final long serialVersionUID = 1L;
    private Clan clan;
    private int power;

    public ClanUnit( UnitType unitType, Clan clan ,int power){
        super(unitType);
        this.clan=clan;
        this.power = power;
    }

    public int getPower(){return power;}
    public Clan getClan(){return clan;}
    public String toString(){return clan + " unit with power: "+power;}

    @Override
    public int getBattlePriority(Battle context, PlayedUnit self) {
        game.enums.Clan battleClan = game.engine.BattleResolver.determineBattleClan(context);
        
        if (this.clan == game.enums.Clan.SHADOW) {
            return 700 + this.power;
        } else if (this.clan == battleClan) {
            return 600 + this.power;
        } else {
            return this.power;
        }
    }
}
