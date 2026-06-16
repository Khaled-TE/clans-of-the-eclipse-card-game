package game.gui;

import game.engine.GameController;
import javafx.application.Application;
import javafx.stage.Stage;


public class GameGUI extends Application {

    private static GameController controller; // ← static

    public GameGUI() {} // JavaFX needs an empty constructor

    public static void setController(GameController c) {
        controller = c;
    }

    @Override
    public void start(Stage primaryStage) {
        SceneManager sceneManager = new SceneManager(primaryStage, controller);
        sceneManager.showStartMenu();
        primaryStage.show();
    }
}

