package game.engine;

import java.util.ArrayList;
import game.cli.Output;
import game.enums.BonusCase;
import game.model.Player;
import game.model.RoundRecord;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ScoreManager {

    private static final Properties props = new Properties();
    
    private static boolean BONUS_REQUIRES_CORRECT_PROPHECY;
    private static int SCORE_CORRECT_PROPHECY_MULTIPLIER;
    private static int SCORE_INCORRECT_PROPHECY_PENALTY_MULTIPLIER;
    private static int SCORE_ZERO_PROPHECY_CORRECT_MULTIPLIER;
    private static int SCORE_ZERO_PROPHECY_INCORRECT_MULTIPLIER;
    private static int BONUS_CAPTURE_KING_EARTH;
    private static int BONUS_CAPTURE_KING_FIRE;
    private static int BONUS_CAPTURE_KING_WATER;
    private static int BONUS_CAPTURE_KING_SHADOW;
    private static int BONUS_OVERLORD_DEFEATS_RAIDER;
    private static int BONUS_ORACLE_DEFEATS_OVERLORD;

    static {
        try (InputStream is = ScoreManager.class.getClassLoader().getResourceAsStream("config/ClansOfTheEclipseRewards.properties")) {
            if (is != null) {
                props.load(is);
                
                BONUS_REQUIRES_CORRECT_PROPHECY = Boolean.parseBoolean(props.getProperty("bonus.requires.correct.prophecy", "true"));
                SCORE_CORRECT_PROPHECY_MULTIPLIER = Integer.parseInt(props.getProperty("score.correct.prophecy.multiplier", "20"));
                SCORE_INCORRECT_PROPHECY_PENALTY_MULTIPLIER = Integer.parseInt(props.getProperty("score.incorrect.prophecy.penalty.multiplier", "-10"));
                SCORE_ZERO_PROPHECY_CORRECT_MULTIPLIER = Integer.parseInt(props.getProperty("score.zero.prophecy.correct.multiplier", "10"));
                SCORE_ZERO_PROPHECY_INCORRECT_MULTIPLIER = Integer.parseInt(props.getProperty("score.zero.prophecy.incorrect.multiplier", "-10"));
                BONUS_CAPTURE_KING_EARTH = Integer.parseInt(props.getProperty("bonus.capture.king.earth", "10"));
                BONUS_CAPTURE_KING_FIRE = Integer.parseInt(props.getProperty("bonus.capture.king.fire", "10"));
                BONUS_CAPTURE_KING_WATER = Integer.parseInt(props.getProperty("bonus.capture.king.water", "10"));
                BONUS_CAPTURE_KING_SHADOW = Integer.parseInt(props.getProperty("bonus.capture.king.shadow", "20"));
                BONUS_OVERLORD_DEFEATS_RAIDER = Integer.parseInt(props.getProperty("bonus.overlord.defeats.raider", "30"));
                BONUS_ORACLE_DEFEATS_OVERLORD = Integer.parseInt(props.getProperty("bonus.oracle.defeats.overlord", "50"));
            } else {
                Output.printException("Error: config/ClansOfTheEclipseRewards.properties not found!");
            }
        } catch (IOException e) {
            Output.printException("Error loading scoring config: " + e.getMessage());
        }
    }
    
    public static void calculateRoundScore(RoundRecord roundRecord){
        for(Player p : roundRecord.getProphecyMap().keySet())
            calculateEachPlayerScore(roundRecord, p);
    }

    private static void calculateEachPlayerScore(RoundRecord r, Player p){
        int prophecy = r.getProphecy(p);
        int won = r.getWonBattles(p);

        r.setPlayerBaseScore(p, baseScore(r, p));
        
        boolean correctProphecy = prophecy == won;
        
        if (!BONUS_REQUIRES_CORRECT_PROPHECY || correctProphecy)
            r.setPlayerBonusScore(p, bonusScore(r.getBonusCases(p)));
    }

    private static int baseScore(RoundRecord r, Player p){
        int prophecy = r.getProphecy(p);
        int won = r.getWonBattles(p);

        if (prophecy == 0) {
            return (won == 0) ? (r.getRoundNumber() * SCORE_ZERO_PROPHECY_CORRECT_MULTIPLIER) : (r.getRoundNumber() * SCORE_ZERO_PROPHECY_INCORRECT_MULTIPLIER);
        } else {
            return (prophecy == won) ? (won * SCORE_CORRECT_PROPHECY_MULTIPLIER) : (Math.abs(prophecy - won) * SCORE_INCORRECT_PROPHECY_PENALTY_MULTIPLIER);
        }
    }

    private static int bonusScore(ArrayList<BonusCase> bonusCasesCollected){
        int sum = 0;
        for(BonusCase b : bonusCasesCollected){
            switch (b) {
                case EARTH_KING: 
                    sum += BONUS_CAPTURE_KING_EARTH; break;
                case FIRE_KING: 
                    sum += BONUS_CAPTURE_KING_FIRE; break;
                case WATER_KING: 
                    sum += BONUS_CAPTURE_KING_WATER; break;
                case SHADOW_KING:
                    sum += BONUS_CAPTURE_KING_SHADOW; break;
                case OVERLORD_DOMINANCE:
                    sum += BONUS_OVERLORD_DEFEATS_RAIDER; break;
                case ORACLE_TRIUMPH:
                    sum += BONUS_ORACLE_DEFEATS_OVERLORD; break;
                default:
                    break;
            }
        }
        return sum;
    }
}