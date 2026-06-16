package game.cli;

import java.util.Scanner;
import game.enums.DeclaredIdentity;
import game.exceptions.InvalidProphecyException;
import game.exceptions.InvalidUnitSelectionException;
import game.model.Player;

public class Input {
    
    private static final Scanner scanner = new Scanner(System.in);

    public String getPlayerName(int playerNumber) {
        System.out.print("Enter name for Player " + playerNumber + ": ");
        String name = scanner.nextLine().trim();
        
        while (name.isEmpty() || name.length() > 25) {
            System.out.print("Name cannot be empty or too long for player " + playerNumber + ": ");
            name = scanner.nextLine().trim();
        }
        return name;
    }

    public int getProphecy(Player player, int currentRound) throws InvalidProphecyException {
        System.out.print(player.getName() + " Please enter your prophecy: ");

        while (true) {
            try {
                String inputString = scanner.nextLine().trim();
                int in = Integer.parseInt(inputString);
                if (in < 0 || in > currentRound) {
                    throw new InvalidProphecyException("Invalid prophecy, must be between 0 and " + currentRound);
                }
                Output.move();
                return in;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please type a whole number.");
            }
        }
    }

    public int getUnitChoice(Player player) throws InvalidUnitSelectionException {
        System.out.print(player.getName() + " Please enter the number of the unit you want to recruit : ");
        int handSize = player.getHand().size();

        while (true) {
            try {
                String inputString = scanner.nextLine().trim();
                int in = Integer.parseInt(inputString);
                if (in < 1 || in > handSize) {
                    throw new InvalidUnitSelectionException("Invalid UnitSelection, your selction should be between 1 and " + handSize);
                }
                Output.move();
                return in - 1;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please type a whole number.");
            }
        }
    }

    public DeclaredIdentity getShapeshifterDecision() {
        while (true) {
            System.out.print("Decide whether to use the shapeshifter as Raider(R) or scout(S): ");
            String input = scanner.nextLine().trim().toUpperCase();

            if (input.isEmpty()) {
                System.out.println(">> Error: Input cannot be empty.");
                continue;
            }

            char in = input.charAt(0);
            if (in == 'R') {
                return DeclaredIdentity.RAIDER;
            } else if (in == 'S') {
                return DeclaredIdentity.SCOUT;
            }
        }
    }

    public static void passTurn() {
        while (true) {
            String input = scanner.nextLine();
            if (input.isEmpty()) {
                return;
            }
            System.out.println(">> Invalid! Please press ONLY the ENTER key:");
        }
    }

    public int getNumberOfPlayers() {
        System.out.print("Enter the number of players : ");
        
        while (true) {
            try {
                String inputString = scanner.nextLine().trim();
                int numPlayers = Integer.parseInt(inputString);
                if (numPlayers < 2 || numPlayers > 7) {
                    System.out.println(">> Invalid number of players. Please enter a number between 2 and 7.");
                    System.out.print("Enter the number of players : ");
                    continue;
                }
                return numPlayers;
            } catch (NumberFormatException e) {
                System.out.println(">> Invalid input! Please enter a valid number.");
                System.out.print("Enter the number of players : ");
            }
        }
    }

    public static boolean askToLoadSave() {
        System.out.print("A saved game was found. Do you want to load it? (Y/N): ");
        String input = scanner.nextLine().trim().toUpperCase();
        return input.equals("Y");
    }
}