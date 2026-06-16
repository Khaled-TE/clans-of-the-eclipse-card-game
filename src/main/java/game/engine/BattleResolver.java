package game.engine;

import game.enums.Clan;
import game.enums.DeclaredIdentity;
import game.enums.UnitType;
import game.model.*;
import game.units.ClanUnit;
import game.units.Unit;

public class BattleResolver {

    public static Clan determineBattleClan(Battle battle) {
        PlayedUnit leaderUnit = battle.getPlayedUnits().get(battle.getLeaderUnitIndex());
        Unit unit1 = leaderUnit.getUnit();

        if (unit1.getType() == UnitType.CLAN || unit1.getType() == UnitType.KING) {
            return ((ClanUnit) unit1).getClan();
        }

        if (unit1.getType() == UnitType.SCOUT ||
                (unit1.getType() == UnitType.SHAPESHIFTER && leaderUnit.getIdentity() == DeclaredIdentity.SCOUT)) {

            for (PlayedUnit pu : battle.getPlayedUnits()) {
                if (pu == leaderUnit) continue;
                Unit u = pu.getUnit();
                if (u.getType() == UnitType.CLAN || u.getType() == UnitType.KING) {
                    return ((ClanUnit) u).getClan();
                }
            }
        }
        return null;
    }

    public static boolean isLeviathan(Battle battle) {
        for (PlayedUnit pu : battle.getPlayedUnits()) {
            if (pu.getUnit().getType() == UnitType.LEVIATHAN) {
                return true;
            }
        }
        return false;
    }

    public static void resolveBattle(Battle battle, RoundRecord roundRecord) {
        if (isLeviathan(battle)) {
            battle.setLeviathan(true);
            battle.setWinner(null);
            roundRecord.addBattle(battle);
            return;
        }

        PlayedUnit winningUnit = null;
        int maxPriority = -1;
        int leaderIndex = battle.getLeaderUnitIndex();
        int numberOfPlayers = battle.getPlayedUnits().size();

        for (int i = 0; i < numberOfPlayers; i++) {
            int currentIndex = (leaderIndex + i) % numberOfPlayers;
            PlayedUnit pu = battle.getPlayedUnits().get(currentIndex);
            
            if (pu == null || pu.getUnit() == null) continue;

            int currentPriority = pu.getUnit().getBattlePriority(battle, pu);
            
            if (currentPriority > maxPriority) {
                maxPriority = currentPriority;
                winningUnit = pu;
            }
        }

        Player winner = (winningUnit != null) ? winningUnit.getPlayer() : null;
        battle.setWinner(winner);

        EffectResolver.extractBonusCases(battle, roundRecord);

        if (winner != null) {
            roundRecord.setWonBattles(winner, roundRecord.getWonBattles(winner) + 1);
        }
        roundRecord.addBattle(battle);
    }
}