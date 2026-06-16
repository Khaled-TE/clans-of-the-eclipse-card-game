package game.cli;

import java.util.ArrayList;
import game.model.Battle;
import game.model.GameStatus;
import game.model.Player;
import game.model.RoundRecord;
import game.model.ScoreSheet;

public class Output {

    private static void pause(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public static void showStatus(GameStatus status) {
        System.out.println(status.getPhase() + " Round " + status.getRoundNumber());
        pause(1000);
    }

    public static void showDrawal(ArrayList<Player> players, int winner) {
        System.out.println("The Units have been distributed");
        pause(2000);
        move();

        for (int i = winner; i < players.size(); i++) {
            displayHand(players.get(i));
            System.out.print("\nMEMORIZE YOUR UNITS AND PRESS ENTER key when you are ready to move...");
            Input.passTurn();
            move();
        }

        for (int i = 0; i < winner; i++) {
            Player p = players.get(i);
            displayHand(p);
            System.out.print("\nMEMORIZE YOUR UNITS AND PRESS ENTER key when you are ready to move...");
            Input.passTurn();
            move();
        }
    }

    public static void displayCurrentRound(int round) {
        System.out.println("\n=== ROUND " + round + " ===");
        pause(1000);
    }

    public static void displayProphecy(Player p) {
        System.out.println("\n" + p.getName() + ", it's time to make your prophecy!");
        pause(1000);
    }

    public static void displayProphecyResult(Player winner, int prophecy) {
        System.out.println(winner.getName() + " has prophesied " + prophecy + " wins for this round.");
        pause(1000);
    }

    public static void displayAllProphecies(RoundRecord record, ArrayList<Player> players) {
        System.out.println("\n=========================================");
        System.out.println("      THE PROPHECIES ARE REVEALED!       ");
        System.out.println("=========================================");
        
        for (Player p : players) {
            int prophecy = record.getProphecy(p);
            System.out.println("  " + p.getName().toUpperCase() + " predicts they will win: " + prophecy + " battles.");
        }
        
        System.out.println("=========================================\n");
        pause(3000);
    }

    public static void battleStart(int n) {
        System.out.println("\n=== BATTLE " + n + " ===");
        pause(1000);
    }

    public static void displayBattleResults(Battle battle) {
        System.out.println("Battle concluded!");
        try {
            System.out.println("Winner: " + battle.getWinner().getName());
        } catch (NullPointerException e) {
            System.out.println("Leviathan played so battle destroyed ");
        }
        pause(1000);
    }

    public static void displayHand(Player player) {
        System.out.println("\n*** PASS THE LAPTOP TO " + player.getName().toUpperCase() + " ***");
        System.out.println("Press ONLY the ENTER key when you are alone and ready...");
        Input.passTurn();

        System.out.println("\n" + player.getName() + "'s hand:\n");
        for (int i = 0; i < player.getHand().size(); i++) {
            System.out.println((i + 1) + ". " + player.getHand().get(i));
        }
    }
    
    public static void showRoundRecord(RoundRecord record, ArrayList<Player> players) {
        System.out.println("\n---------------------------------------------------------------------------------------");
        System.out.println("                                 ROUND " + record.getRoundNumber() + " SUMMARY");
        System.out.println("---------------------------------------------------------------------------------------");
        System.out.printf("| %-15s | %-10s | %-7s | %-10s | %-7s | %-8s | %-8s |\n", "PLAYER", "Prophecy", "Wins", "Correct", "Base", "Bonus", "Total");
        System.out.println("---------------------------------------------------------------------------------------");

        for (Player p : players) {
            int prophecy = record.getProphecy(p);
            int wins = record.getWonBattles(p);
            int base = record.getPlayerBaseScore(p);
            int bonus = record.getPlayerBonusScore(p);
            System.out.printf("| %-15s | %-10d | %-7d | %-10s | %-7d | %-8d | %-8d |\n", p.getName(), prophecy, wins, (prophecy == wins ? "Yes" : "No"), base, bonus, (base + bonus));
        }
        System.out.println("---------------------------------------------------------------------------------------\n");
        pause(3000);
        move();
    }

    public static void showErrorMessage(String messageString) {
        System.out.println("Error: " + messageString);
    }

    public static void showWinner(ScoreSheet scoreSheet) {
        ArrayList<Player> winners = scoreSheet.getWinners();
        String stars = "★".repeat(40);
        
        System.out.println("\n" + stars);
        for (Player p : winners) {
            System.out.println("  CONGRATULATIONS " + p.getName().toUpperCase());
        }
        
        if (winners.size() == 1) {
            System.out.println("  YOU ARE THE RULER OF THE ECLIPSE!");
        } else {
            System.out.println("  YOU ARE THE RULERS OF THE ECLIPSE!");
        }
        System.out.println(stars);
    }

    public static void displayFinalScoreSheet(ScoreSheet scoreSheet, ArrayList<Player> players) {
        System.out.println("\n==============================================================");
        System.out.println("                     CLANS OF THE ECLIPSE");
        System.out.println("                         SCORE SHEET");
        System.out.println("==============================================================\n");
        
        for (int i = 0; i < players.size(); i++) {
            System.out.println("Team / Player " + (i + 1) + ": " + players.get(i).getName());
        }
        
        System.out.println("\n---------------------------------------------------------------------------------------------------------------------------------------------------");
        System.out.print("| Round |");
        for (int i = 0; i < players.size(); i++) {
            System.out.printf(" P%d Prophecy | P%d Wins | P%d Correct | P%d Base | P%d Bonus | P%d Total |", i + 1, i + 1, i + 1, i + 1, i + 1, i + 1);
        }
        System.out.println("\n---------------------------------------------------------------------------------------------------------------------------------------------------");

        for (RoundRecord r : scoreSheet.getRoundRecords()) {
            System.out.printf("| %-5d |", r.getRoundNumber());
            
            for (Player p : players) {
                int prophecy = r.getProphecy(p);
                int wins = r.getWonBattles(p);
                int base = r.getPlayerBaseScore(p);
                int bonus = r.getPlayerBonusScore(p);
                System.out.printf(" %-11d | %-7d | %-10s | %-7d | %-8d | %-8d |", prophecy, wins, (prophecy == wins ? "Yes" : "No"), base, bonus, (base + bonus));
            }
            System.out.println(); 
        }
        
        System.out.println("---------------------------------------------------------------------------------------------------------------------------------------------------\n");

        for (int i = 0; i < players.size(); i++) {
            System.out.println(players.get(i).getName() + " Final Score: " + scoreSheet.getTotalScoreMap().get(players.get(i)));
        }
    }

    public static void printException(String s) {
        System.out.println(s);
    }

    public static void move() {
        for (int i = 0; i < 100; i++) {
            System.out.println();
        }
    }
}