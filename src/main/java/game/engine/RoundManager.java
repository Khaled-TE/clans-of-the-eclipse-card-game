package game.engine;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import game.enums.DeclaredIdentity;
import game.enums.RoundPhase;
import game.exceptions.*;
import game.model.*;
import game.units.Unit;

public class RoundManager implements Serializable{
    private RoundRecord roundRecord;
    private int n;
    private GameStatus gameStatus;
    private ArrayList<Player> players;
    private BarracksManager barracks;
    private ScoreSheet scoreSheet;
    private int numberOfCards;

    private Set<Player> prophesiedPlayers = new HashSet<>();
    private int cardsPlayedInCurrentBattle = 0;
    private int currentBattleNumber = 1;
    private Battle currentBattle;
    private PlayedUnit leaderUnit;
    private ArrayList<PlayedUnit> currentPlayedUnits;
    private ArrayList<PlayedUnit> allCardsPlayedInRound = new ArrayList<>();
    private transient Runnable onBattleResolved;

    public void setOnBattleResolved(Runnable callback) {
        this.onBattleResolved = callback;
    }

    public RoundManager(int n, GameStatus gameStatus, BarracksManager barracks, ScoreSheet scoreSheet) {
        this.roundRecord = new RoundRecord(n);
        this.n = n;
        this.gameStatus = gameStatus;
        this.players = gameStatus.getPlayers();
        this.barracks = barracks;
        this.scoreSheet = scoreSheet;
        if (gameStatus.getPhase() != RoundPhase.BATTLE) {
            this.gameStatus.addRoundRecord(this.roundRecord);
        }
    }

    public void initializeRound(int numofcards) {
        this.numberOfCards = numofcards;

        if (gameStatus.getPhase() == RoundPhase.BATTLE) {
            this.currentBattleNumber = gameStatus.getCurrentBattleNumber();
            this.roundRecord = gameStatus.getRoundRecords().get(gameStatus.getRoundRecords().size() - 1);
            startNewBattle();
            return;
        }

        gameStatus.setPhase(RoundPhase.DRAW);
        for (Player p : players) {
            p.reset();
            ArrayList<Unit> drawn = null;
            try {
                drawn = barracks.drawUnits(numofcards);
            } catch (Exception e) {
                drawn = new ArrayList<>();
            }
            if (drawn == null || drawn.size() < numofcards) {
                gameStatus.setRoundNumber(999);
                gameStatus.setPhase(RoundPhase.SCORING);
                return;
            }
            p.setHand(drawn);
        }
        barracks.refill();
    }

    private void startNewBattle() {
        currentPlayedUnits = new ArrayList<>(Collections.nCopies(players.size(), null));
        currentBattle = new Battle(currentBattleNumber, currentPlayedUnits, gameStatus.getWinner());
        leaderUnit = null;
        cardsPlayedInCurrentBattle = 0;
        gameStatus.setTurn(gameStatus.getWinner());
    }

    public void executeCardPlay(Player player, int unitIndex, DeclaredIdentity identity)
            throws InvalidUnitSelectionException, IllegalFollowRuleException {
        if (gameStatus.getPhase() != RoundPhase.BATTLE) return;

        int playerPosition = players.indexOf(player);
        if (playerPosition == -1 || playerPosition != gameStatus.getTurn()) return;
        if (currentPlayedUnits.get(playerPosition) != null) return;

        if (unitIndex < 0 || unitIndex >= player.getHand().size()) {
            throw new InvalidUnitSelectionException();
        }

        Unit plaUnit = player.getHand().get(unitIndex);
        PlayedUnit playedUnit = new PlayedUnit(plaUnit, player, identity);

        boolean isLeader = (cardsPlayedInCurrentBattle == 0);
        if (!isLeader) {
            playedUnit.validateFollowIfPossibleRule(leaderUnit, player.getHand());
        }

        player.getHand().remove(plaUnit);
        currentPlayedUnits.set(playerPosition, playedUnit);
        allCardsPlayedInRound.add(playedUnit);

        if (isLeader) {
            leaderUnit = playedUnit;
        }

        cardsPlayedInCurrentBattle++;

        if (cardsPlayedInCurrentBattle == players.size()) {
            resolveCurrentBattle();
        } else {
            gameStatus.moveTurn();
        }
    }

    private void resolveCurrentBattle() {
        BattleResolver.resolveBattle(currentBattle, roundRecord);
        Player winner = currentBattle.getWinner();
        int winnerIndex;

        if (winner != null) {
            winner.setNumberOfWins(winner.getNumberOfWins() + 1);
            winnerIndex = players.indexOf(winner);
        }
        else{
            winnerIndex = players.indexOf(leaderUnit.getPlayer());
        }
        gameStatus.setWinner(winnerIndex);

        currentBattleNumber++;
        gameStatus.setCurrentBattleNumber(currentBattleNumber);
        if (currentBattleNumber <= numberOfCards) {
            startNewBattle();
        } else {
            gameStatus.setPhase(RoundPhase.SCORING);
            ScoreManager.calculateRoundScore(roundRecord);
            scoreSheet.addRoundRecord(roundRecord);
            gameStatus.setRoundNumber(n + 1);

        }
        if (onBattleResolved != null) {
            onBattleResolved.run();
        }
    }

    public PlayedUnit getLeaderUnit() {

        return this.leaderUnit;
    }

    public ArrayList<PlayedUnit> getCurrentPlayedUnits() {
        return new ArrayList<PlayedUnit>(this.currentPlayedUnits);
    }

    public ArrayList<PlayedUnit> getAllCardsPlayedInRound() {
        return new ArrayList<>(this.allCardsPlayedInRound);
    }

    public boolean canPlayCard(Player player, Unit unit) {
        if (gameStatus.getPhase() != RoundPhase.BATTLE) return false;
        int playerPosition = players.indexOf(player);
        if (playerPosition == -1 || playerPosition != gameStatus.getTurn()) return false;
        if (currentPlayedUnits.get(playerPosition) != null) return false;
        if (leaderUnit == null) return true;
        PlayedUnit candidate = new PlayedUnit(unit, player);
        return candidate.canFollowRule(leaderUnit, player.getHand());
    }
    public void startProphecyPhase() {
        if (gameStatus.getPhase() != RoundPhase.DRAW) return;
        gameStatus.setPhase(RoundPhase.PROPHECY);
        prophesiedPlayers.clear();
        gameStatus.setTurn((n - 1) % players.size());
    }

    public void recordProphecy(Player player, int prophecy) throws InvalidProphecyException {
        if (gameStatus.getPhase() != RoundPhase.PROPHECY) return;

        int playerPosition = players.indexOf(player);
        if (playerPosition == -1 || playerPosition != gameStatus.getTurn()) return;
        if (prophesiedPlayers.contains(player)) return;

        roundRecord.setProphecy(player, prophecy);
        prophesiedPlayers.add(player);

        if (prophesiedPlayers.size() == players.size()) {
            gameStatus.setPhase(RoundPhase.BATTLE);
            gameStatus.setWinner((n - 1) % players.size());
            startNewBattle();
        } else {
            gameStatus.moveTurn();
        }
    }
    public void restoreAllCards(ArrayList<PlayedUnit> allCards) {
        if (allCards != null) {
            this.allCardsPlayedInRound = allCards;
        }
    }

}