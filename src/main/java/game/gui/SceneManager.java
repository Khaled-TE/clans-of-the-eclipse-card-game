package game.gui;

import game.engine.GameController;
import game.utils.SoundManager;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.stage.Stage;

public class SceneManager {

    private Stage stage;
    private GameController controller;
    private String currentTrack = "";
    private Scene baseScene;
    private Scene previousScene;
    private Scene currentVirtualScene;
    private Scene startMenuScene;
    private Scene settingsScene;
    private Scene infoScene;
    private Scene namingScene;
    private Scene gameArenaScene;
    private Scene scoreboardScene;
    private Scene winnerScene;

    public SceneManager(Stage stage, GameController controller) {
        this.stage = stage;
        this.controller = controller;
        this.stage.setTitle("Clans of the Eclipse");
        this.stage.setFullScreenExitHint("");

        baseScene = new Scene(new javafx.scene.layout.Pane(), 1280, 720, false, SceneAntialiasing.BALANCED);
        this.stage.setScene(baseScene);

        startMenuScene = new StartMenuScene(this, controller).buildScene();
        settingsScene = new SettingsScene(this, controller).buildScene();
        infoScene = new InfoScene(this, controller).buildScene();
    }

    private void switchScene(Scene incomingScene) {
        if (incomingScene == null || incomingScene == currentVirtualScene) return;

        if (incomingScene == winnerScene) {
            if (!currentTrack.equals("victory_theme.mp3")) {
                SoundManager.stopMusic();
                SoundManager.playMusic("victory_theme.mp3");
                currentTrack = "victory_theme.mp3";
            }
        } 
        else if (incomingScene == startMenuScene) {
            if (!currentTrack.equals("got_music.mp3")) {
                SoundManager.stopMusic();
                SoundManager.playMusic("got_music.mp3");
                currentTrack = "got_music.mp3";
            }
        }
    
        javafx.scene.Parent activeRoot = baseScene.getRoot();
        javafx.scene.Camera activeCamera = baseScene.getCamera();
        
        baseScene.setRoot(new javafx.scene.layout.Pane()); 
        baseScene.setCamera(null);
    
        if (currentVirtualScene != null && activeRoot != null) {
            currentVirtualScene.setRoot(activeRoot);
            currentVirtualScene.setCamera(activeCamera);
        }
    
        javafx.scene.Parent newRoot = incomingScene.getRoot();
        javafx.scene.Camera newCamera = incomingScene.getCamera();
    
        incomingScene.setRoot(new javafx.scene.layout.Pane()); 
        incomingScene.setCamera(null);
    
        baseScene.setRoot(newRoot);
        baseScene.setFill(incomingScene.getFill());
        baseScene.setCamera(newCamera);
    
        previousScene = currentVirtualScene;
        currentVirtualScene = incomingScene;
    }

    public void showStartMenu() {
        startMenuScene = new StartMenuScene(this, controller).buildScene();
        switchScene(startMenuScene);
    }

    public void showSettings() {
        settingsScene = new SettingsScene(this, controller).buildScene();
        switchScene(settingsScene);
    }

    public void showInfoScene() {
        switchScene(infoScene);
    }

    public void showNamingScene() {
        namingScene = new NamingScene(this, controller).buildScene();
        switchScene(namingScene);
    }

    public void showGameArena() {
        gameArenaScene = new GameArenaScene(this, controller).buildScene();
        switchScene(gameArenaScene);
    }

    public void showScoreboardScene() {
        scoreboardScene = new ScoreboardScene(this, controller).buildScene(); 
        switchScene(scoreboardScene); 
    }

    public void showWinnerScene() {
        winnerScene = new WinnerScene(this, controller).buildScene();
        switchScene(winnerScene);
    }

    public void goBack() {
        if (previousScene != null) {
            switchScene(previousScene);
            stage.show();
        } else {
            showStartMenu();
        }
    }
        
    public Scene getstartMenuScene(){
        return startMenuScene;
    }

    public Scene getCurrentVirtualScene() {
        return currentVirtualScene;
    }
}