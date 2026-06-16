package game.engine;

import game.enums.Clan;
import game.enums.UnitType;
import game.units.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Properties;

public class BarracksManager implements Serializable {
    private static final long serialVersionUID = 1L;
    private ArrayList<Unit> unitsCollection;
    private final ArrayList<Unit> completeUnitsCollection;
    private static Properties unitProps = new Properties();

    public BarracksManager() throws IOException {
        unitsCollection = new ArrayList<>();
        try (InputStream is = BarracksManager.class.getClassLoader()
                .getResourceAsStream("config/ClansOfTheEclipseUnitsDistribution.properties")) {
            if (is != null) {
                unitProps.load(is);
            } else {
                throw new IOException("ClansOfTheEclipseUnitsDistribution.properties not found!");
            }
        }
        String[] clans = {"fire", "earth", "water", "shadow"};
        Clan[] clanEnums = {Clan.FIRE, Clan.EARTH, Clan.WATER, Clan.SHADOW};

        for (int c = 0; c < clans.length; c++) {
            // clan units
            for (int power = 1; power <= 13; power++) {
                int count = Integer.parseInt(unitProps.getProperty("unit." + clans[c] + "." + power + ".count", "1"));
                for (int i = 0; i < count; i++) {
                    unitsCollection.add(new ClanUnit(UnitType.CLAN, clanEnums[c], power));
                }
            }
            // king
            int kingCount = Integer.parseInt(unitProps.getProperty("unit." + clans[c] + ".king.count", "1"));
            for (int i = 0; i < kingCount; i++) {
                unitsCollection.add(new KingUnit(clanEnums[c]));
            }
        }

        // Overlord
        int overlordCount = Integer.parseInt(unitProps.getProperty("unit.overlord.count", "0"));
        for (int i = 0; i < overlordCount; i++)
            unitsCollection.add(new Overlord(UnitType.OVERLORD, "Ruler of the eclipse"));

        // Oracle
        int oracleCount = Integer.parseInt(unitProps.getProperty("unit.oracle.count", "0"));
        for (int i = 0; i < oracleCount; i++)
            unitsCollection.add(new Oracle(UnitType.ORACLE, "Master of fate manipulation"));

        // Raider
        int raiderCount = Integer.parseInt(unitProps.getProperty("unit.raider.count", "0"));
        for (int i = 0; i < raiderCount; i++)
            unitsCollection.add(new Raider(UnitType.RAIDER, "Elite units overpowering all clans"));

        // Scout
        int scoutCount = Integer.parseInt(unitProps.getProperty("unit.scout.count", "0"));
        for (int i = 0; i < scoutCount; i++)
            unitsCollection.add(new Scout(UnitType.SCOUT, "Tactical disruptors of flow"));

        // Shapeshifter
        int shapeshifterCount = Integer.parseInt(unitProps.getProperty("unit.shapeshifter.count", "0"));
        for (int i = 0; i < shapeshifterCount; i++)
            unitsCollection.add(new Shapeshifter(UnitType.SHAPESHIFTER, "Adapts role dynamically"));

        // Leviathan
        int leviathanCount = Integer.parseInt(unitProps.getProperty("unit.leviathan.count", "0"));
        for (int i = 0; i < leviathanCount; i++)
            unitsCollection.add(new Leviathan(UnitType.LEVIATHAN, "Destroyer of battles"));

        completeUnitsCollection = new ArrayList<>(unitsCollection);
        Collections.shuffle(unitsCollection);
    }

    public ArrayList<Unit> drawUnits(int count) {
        ArrayList<Unit> units = new ArrayList<>(unitsCollection.subList(0, count));
        unitsCollection.subList(0, count).clear();
        return units;
    }

    public void refill() {
        unitsCollection = new ArrayList<>(completeUnitsCollection);
        Collections.shuffle(unitsCollection);
    }
}