package game.engine;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import game.enums.DeclaredIdentity;
import game.enums.RoundPhase;
import game.exceptions.IllegalFollowRuleException;
import game.exceptions.InvalidProphecyException;
import game.exceptions.InvalidUnitSelectionException;
import game.model.*;
import game.utils.SaveManager;

public class GameController {

    private BarracksManager barracks;
    private GameStatus gameStatus;
    private ScoreSheet scoreSheet;
    private RoundManager currentRoundManager;
    private int totRoundnum, totPlayernum;
    private Properties gameConfig = new Properties();
    private ArrayList<PlayedUnit> savedAllCards = new ArrayList<>();
    public GameController() {
        currentRoundManager = null;

        try (InputStream input = getClass().getClassLoader().getResourceAsStream("config/ClansOfTheEclipseGame.properties")) {
            if (input != null) {
                gameConfig.load(input);
            } else {
                System.err.println("Error: config/ClansOfTheEclipseGame.properties not found!");
            }
        } catch (IOException e) {
            System.err.println("Error loading config file: " + e.getMessage());
        }

        try {
            barracks = new BarracksManager();
        } catch (IOException e) {
            System.err.println("Error initializing Barracks: " + e.getMessage());
        }

        this.totRoundnum = getTotalConfigRounds();
        this.totPlayernum = getConfigNumberOfPlayers();
    }

    private int getTotalConfigRounds(){
        String roundsValue = gameConfig.getProperty("game.rounds", "10");
        try {
            return Integer.parseInt(roundsValue.trim());
        } catch (NumberFormatException e) {
            return 10;
        }
    }
    public int getTotalRounds() {
        return totRoundnum;
    }

    public void setTotalRounds(int totRoundnum){
        if (totRoundnum > 0)
            this.totRoundnum = totRoundnum;
    }

    private int getConfigNumberOfPlayers(){
        String playersValue = gameConfig.getProperty("game.players", "2");
        try {
            return Integer.parseInt(playersValue.trim());
        } catch (NumberFormatException e) {
            return 2; 
        }
    }

    public int getNumberOfPlayers() {
        return totPlayernum;
    }

    public void setNumberOfPlayers(int totPlayernum){
        if (totPlayernum >= 2 && totPlayernum <= 7)
            this.totPlayernum = totPlayernum;
    }

    public int getStartingDrawCount() {
        String val = gameConfig.getProperty("round.starting.draw.count", "1");
        try { return Integer.parseInt(val.trim()); } catch (Exception e) { return 1; }
    }

    public int getDrawIncrement() {
        String val = gameConfig.getProperty("round.draw.increment.per.round", "1");
        try { return Integer.parseInt(val.trim()); } catch (Exception e) { return 1; }
    }

    public void startNewGame(List<String> playerNames) {
        if (playerNames.size() != this.totPlayernum) {
            throw new IllegalArgumentException("Mismatch: Expected " + totPlayernum + " players, got " + playerNames.size());
        }
        
        currentRoundManager = null;
        ArrayList<Player> players = new ArrayList<>();
        for (String name : playerNames) {
            players.add(new Player(name));
        }
        gameStatus = new GameStatus(players);
        scoreSheet = new ScoreSheet(players);
    }

    public void startNextRound() {
        if (currentRoundManager != null && gameStatus.getPhase() != RoundPhase.SCORING) {
            return;
        }
        int nextRoundNum = gameStatus.getRoundNumber();
        if (nextRoundNum > getTotalRounds()) {
            currentRoundManager = null;
            SaveManager.deleteSave();
            return;
        }
        int numofcards = getStartingDrawCount() + (nextRoundNum - 1) * getDrawIncrement();
        currentRoundManager = new RoundManager(nextRoundNum, gameStatus, barracks, scoreSheet);
        currentRoundManager.setOnBattleResolved(() -> saveGame());
        currentRoundManager.initializeRound(numofcards);
        if (savedAllCards != null && !savedAllCards.isEmpty()) {
            currentRoundManager.restoreAllCards(savedAllCards);
            savedAllCards = null;
        }
    }


    public void advanceToProphecyPhase() {
        if (currentRoundManager != null) {
            currentRoundManager.startProphecyPhase();
        }
    }

    public void submitPlayerProphecy(Player player, int prophecy) throws InvalidProphecyException {
        if (currentRoundManager != null) {
            currentRoundManager.recordProphecy(player, prophecy);
        }
    }

    public void playCard(Player player, int unitIndex, DeclaredIdentity identity)
            throws InvalidUnitSelectionException, IllegalFollowRuleException {
        if (currentRoundManager != null) {
            currentRoundManager.executeCardPlay(player, unitIndex, identity);
        }
    }

    public GameStatus getGameStatus() {
        return this.gameStatus;
    }

    public PlayedUnit getLeaderUnit() {
        if (currentRoundManager != null) {
            return currentRoundManager.getLeaderUnit();
        }
        return null;
    }

    public ScoreSheet getScoreSheet() {
        return this.scoreSheet;
    }

    public List<PlayedUnit> getTableCards() {
        if (currentRoundManager != null) {
            return currentRoundManager.getCurrentPlayedUnits();
        }
        return null;
    }

    public List<PlayedUnit> getAllRoundCards() {
        if (currentRoundManager != null) {
            return currentRoundManager.getAllCardsPlayedInRound();
        }
        return null;
    }

    public boolean canPlayCard(Player player, game.units.Unit unit) {
        if (currentRoundManager == null) return false;
        return currentRoundManager.canPlayCard(player, unit);
    }
    public void saveGame() {
        SaveData data = new SaveData(gameStatus, scoreSheet, barracks, currentRoundManager);
        SaveManager.saveGame(data);
    }
    public void loadGame() {
        SaveData data = SaveManager.loadGame();
        if (data != null) {
            this.gameStatus = data.getGameStatus();
            this.scoreSheet = data.getScoreSheet();
            this.barracks = data.getBarracks();
            this.currentRoundManager = data.getCurrentRoundManager(); 
            
            if (this.currentRoundManager != null) {
                this.currentRoundManager.setOnBattleResolved(() -> saveGame());
            }
        }
    }

    public boolean hasSave() {
        return SaveManager.saveExists();
    }
    public ArrayList<PlayedUnit> getSavedAllCards() {
        return savedAllCards;
    }

}