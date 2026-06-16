package game;

import game.cli.GameCLI;
import game.engine.GameController;
import game.gui.GameGUI;
import javafx.application.Application;

public class Main {

    public static void main(String[] args) {

        // Create game controller
        GameController controller = new GameController();

        // Choose mode, for now default to CLI
        boolean useGUI = true;

        if (useGUI) {
            GameGUI.setController(controller); 
            Application.launch(GameGUI.class, args);
        } else {
            GameCLI cli = new GameCLI(controller);
            cli.start();
        }
    }
}