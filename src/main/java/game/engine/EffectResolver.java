package game.engine;

import game.model.Battle;
import game.model.PlayedUnit;
import game.model.Player;
import game.model.RoundRecord;

import game.enums.UnitType;
import game.enums.Clan;
import game.enums.BonusCase;
import game.enums.DeclaredIdentity;

//import game.units.Unit;
import game.units.ClanUnit;
public class EffectResolver {
    public static void extractBonusCases(Battle battle, RoundRecord record){
        Player winner = battle.getWinner();

        if (winner == null){
            return;
        }
        
        PlayedUnit winnerCard = null; 

        for(PlayedUnit currentCard : battle.getPlayedUnits()){
            if(currentCard.getPlayer() == winner){
                winnerCard = currentCard;
                break;
            }
        }

        UnitType winnerType = winnerCard.getUnit().getType();

        for (PlayedUnit currentCard : battle.getPlayedUnits()) {
    
            if (currentCard.getPlayer() != winner) {
        
                UnitType loserType = currentCard.getUnit().getType();
        
                if (loserType == UnitType.KING) {
                    Clan clan = ((ClanUnit) currentCard.getUnit()).getClan();
                    switch(clan){
                        case EARTH:
                            record.addBonusCase(winner, BonusCase.EARTH_KING); break;
                        case WATER:
                            record.addBonusCase(winner, BonusCase.WATER_KING); break;
                        case FIRE:
                            record.addBonusCase(winner, BonusCase.FIRE_KING); break;
                        case SHADOW:
                            record.addBonusCase(winner, BonusCase.SHADOW_KING); break;
                    }
                }
        
                else if ( (loserType == UnitType.RAIDER || (loserType == UnitType.SHAPESHIFTER && currentCard.getIdentity() == DeclaredIdentity.RAIDER)) 
                    && winnerType == UnitType.OVERLORD ) {
            
                    record.addBonusCase(winner, BonusCase.OVERLORD_DOMINANCE);
                }
        
                else if (loserType == UnitType.OVERLORD && winnerType == UnitType.ORACLE) {
            
                    record.addBonusCase(winner, BonusCase.ORACLE_TRIUMPH);
                }
            }
        }
    }

}
