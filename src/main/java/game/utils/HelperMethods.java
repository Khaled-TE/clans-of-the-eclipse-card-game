package game.utils;

import game.units.Unit;
import game.enums.DeclaredIdentity;
import game.units.ClanUnit;

public class HelperMethods {

    public static String getTextureName(Unit unit) {

        if (unit instanceof game.units.ClanUnit) {
            ClanUnit clanCard = (ClanUnit) unit;
            switch (clanCard.getClan()) {
                case FIRE: return "/cards/FireClanCard";
                case WATER: return "/cards/WaterClanCard";
                case EARTH: return "/cards/EarthClanCard";
                case SHADOW: return "/cards/ShadowClanCard";
                default: return "/cards/EarthClanCard"; 
            }
        } 
        else {
            switch (unit.getType()) {
                case SCOUT: return "/cards/ScoutCard";
                case LEVIATHAN: return "/cards/LeviathanCard";
                case RAIDER: return "/cards/RaiderCard";
                case SHAPESHIFTER: return "/cards/ShapeshifterCard";
                case OVERLORD: return "/cards/OverLordCard"; 
                case ORACLE: return "/cards/OracleCard";
                default: return "/cards/EarthClanCard";
            }
        }
    }

    public static String getTextureName(DeclaredIdentity identity) {

        if (identity == DeclaredIdentity.RAIDER) return "/cards/RaiderCard";
        if (identity == DeclaredIdentity.SCOUT)  return "/cards/ScoutCard";
        return "/cards/EarthClanCard";   // fallback
    }

}
