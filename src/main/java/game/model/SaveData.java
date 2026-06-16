package game.model;

import game.engine.BarracksManager;
import game.engine.RoundManager;
import java.io.Serializable;

public class SaveData implements Serializable {
    private static final long serialVersionUID = 1L;

    private GameStatus gameStatus;
    private ScoreSheet scoreSheet;
    private BarracksManager barracks;
    private RoundManager currentRoundManager;

    public SaveData(GameStatus gameStatus, ScoreSheet scoreSheet, BarracksManager barracks, RoundManager currentRoundManager) {
        this.gameStatus = gameStatus;
        this.scoreSheet = scoreSheet;
        this.barracks = barracks;
        this.currentRoundManager = currentRoundManager;
    }

    public GameStatus getGameStatus() { return gameStatus; }
    public ScoreSheet getScoreSheet() { return scoreSheet; }
    public BarracksManager getBarracks() { return barracks; }
    public RoundManager getCurrentRoundManager() { return currentRoundManager; }
}