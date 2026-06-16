package game.cli;

import java.util.ArrayList;
import java.util.List;
import game.engine.GameController;
import game.enums.DeclaredIdentity;
import game.enums.UnitType;
import game.exceptions.IllegalFollowRuleException;
import game.exceptions.InvalidProphecyException;
import game.exceptions.InvalidUnitSelectionException;
import game.model.GameStatus;
import game.model.Player;
import game.model.RoundRecord;
import game.model.ScoreSheet;

public class GameCLI {

    private final GameController controller;
    private final Input input = new Input();

    public GameCLI(GameController controller) {
        this.controller = controller;
    }

    public void start() {
        System.out.println("=== CLANS OF THE ECLIPSE ===");

        int startingRound = initializeGame();
        int totalRounds = controller.getTotalRounds();
        GameStatus status = controller.getGameStatus();

        for (int round = startingRound; round <= totalRounds; round++) {
            controller.startNextRound();
            ArrayList<Player> players = status.getPlayers();

            Output.displayCurrentRound(round);
            Output.showDrawal(players, 0);

            controller.advanceToProphecyPhase();
            executeProphecyPhase(players, status);

            RoundRecord currentRecord = status.getRoundRecords().get(status.getRoundRecords().size() - 1);
            Output.displayAllProphecies(currentRecord, players);
            
            waitForEnter();

            executeBattlePhase(players, status);

            if (!status.getRoundRecords().isEmpty()) {
                Output.showRoundRecord(status.getRoundRecords().get(status.getRoundRecords().size() - 1), players);
            }
        }

        ScoreSheet sheet = controller.getScoreSheet();
        Output.displayFinalScoreSheet(sheet, status.getPlayers());
        Output.showWinner(sheet);
    }

    private int initializeGame() {
        if (controller.hasSave() && Input.askToLoadSave()) {
            controller.loadGame();
            GameStatus status = controller.getGameStatus();
            int startingRound = status.getRoundNumber();
            System.out.println("Starting from round: " + startingRound + " Total rounds: " + controller.getTotalRounds());
            System.out.println("Phase: " + status.getPhase());
            return startingRound;
        } 
        
        int numPlayers = input.getNumberOfPlayers();
        controller.setNumberOfPlayers(numPlayers);
        List<String> names = new ArrayList<>();

        for (int i = 1; i <= numPlayers; i++) {
            names.add(input.getPlayerName(i));
        }

        controller.startNewGame(names);
        return 1;
    }

    private void executeProphecyPhase(ArrayList<Player> players, GameStatus status) {
        for (int i = 0; i < players.size(); i++) {
            Player p = players.get(status.getTurn());
            Output.displayHand(p);
            Output.displayProphecy(p);

            boolean done = false;
            while (!done) {
                try {
                    int prophecy = input.getProphecy(p, p.getHand().size());
                    controller.submitPlayerProphecy(p, prophecy);
                    done = true;
                } catch (InvalidProphecyException e) {
                    Output.printException(e.getMessage());
                }
            }
        }
    }

    private void executeBattlePhase(ArrayList<Player> players, GameStatus status) {
        int numberOfBattles = players.get(0).getHand().size();

        for (int battle = 1; battle <= numberOfBattles; battle++) {
            Output.battleStart(battle);

            for (int turn = 0; turn < players.size(); turn++) {
                Player p = players.get(status.getTurn());
                Output.displayHand(p);
                boolean done = false;

                while (!done) {
                    try {
                        int idx = input.getUnitChoice(p);
                        DeclaredIdentity id = null;
                        if (p.getHand().get(idx).getType() == UnitType.SHAPESHIFTER) {
                            id = input.getShapeshifterDecision();
                        }
                        controller.playCard(p, idx, id);
                        done = true;
                    } catch (InvalidUnitSelectionException | IllegalFollowRuleException e) {
                        Output.printException(e.getMessage());
                    }
                }
            }
        }
    }

    private void waitForEnter() {
        System.out.println("Press ENTER to clear the screen and begin the battles...");
        try {
            System.in.read();
            while (System.in.available() > 0) {
                System.in.read();
            }
        } catch (Exception ignored) {
        }
    }
}