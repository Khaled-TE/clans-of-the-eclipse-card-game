package game.gui;

import game.engine.GameController;
import game.utils.ImageCache;
import game.utils.SoundManager;
import game.utils.UIFactory;
import javafx.animation.PauseTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class SettingsScene {

    private SceneManager sceneManager;
    private GameController controller;

    public SettingsScene(SceneManager sceneManager, GameController controller) {
        this.sceneManager = sceneManager;
        this.controller   = controller;
    }

    public Scene buildScene() {
        StackPane root = new StackPane();
        root.setPrefSize(Double.MAX_VALUE, Double.MAX_VALUE);

        Image bgImage = ImageCache.getFullResImage("/background/bg");
        if (bgImage != null && !bgImage.isError()) {
            BackgroundImage bg = new BackgroundImage(bgImage, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, new BackgroundSize(1.0, 1.0, true, true, false, false));
            root.setBackground(new Background(bg));
        } else {
            root.setStyle("-fx-background-color: #050508;");
        }

        Pane vignettePane = new Pane();
        vignettePane.setMouseTransparent(true);
        Rectangle vignette = new Rectangle();
        vignette.widthProperty().bind(root.widthProperty());
        vignette.heightProperty().bind(root.heightProperty());
        vignette.setFill(new RadialGradient(0, 0, 0.5, 0.5, 0.85, true, CycleMethod.NO_CYCLE, new Stop(0.0, Color.TRANSPARENT), new Stop(1.0, Color.web("#000000", 0.88))));
        vignettePane.getChildren().add(vignette);

        Label title = UIFactory.createCustomLabel("⚙  SETTINGS", 32, "#C9A84C", true);
        Label soundLabel = UIFactory.createCustomLabel("🔊  AUDIO SETTINGS", 18, "#E8E8E8", false);

        Label musicLabel = UIFactory.createCustomLabel("Music Volume", 14, "#888888", false);
        Slider musicSlider = UIFactory.createStyledSlider(0, 1, SoundManager.getMusicVolume());
        
        Label sfxLabel = UIFactory.createCustomLabel("SFX Volume", 14, "#888888", false);
        Slider sfxSlider = UIFactory.createStyledSlider(0, 1, SoundManager.getSFXVolume());

        Button muteBtn = makeMuteButton();

        musicSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            SoundManager.setMusicVolume(newVal.doubleValue()); 
            if (SoundManager.isMuted()) {
                SoundManager.toggleMute();
                updateMuteBtnAppearance(muteBtn);
            }
        });

        sfxSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            SoundManager.setSFXVolume(newVal.doubleValue()); 
            if (SoundManager.isMuted()) {
                SoundManager.toggleMute();
                updateMuteBtnAppearance(muteBtn);
            }
        });

        VBox musicBox = new VBox(5, musicLabel, musicSlider);
        VBox sfxBox = new VBox(5, sfxLabel, sfxSlider);

        VBox soundControlsBox = new VBox(15, soundLabel, musicBox, sfxBox, muteBtn);
        soundControlsBox.setAlignment(Pos.CENTER_LEFT);
        soundControlsBox.setPadding(new Insets(20));
        UIFactory.applyPanelStyle(soundControlsBox);
        soundControlsBox.setMaxWidth(360);

        Button continueButton = UIFactory.createFlatButton("CONTINUE ➡", 13, "#C9A84C", true);
        continueButton.setPrefWidth(200);
        continueButton.setOnAction(e -> sceneManager.goBack());
        
        Button backButton = UIFactory.createFlatButton("⬅ BACK TO MENU", 16, "#A0A0B0", false);
        backButton.setOnAction(e -> {controller.saveGame(); sceneManager.showStartMenu();});
        StackPane.setAlignment(backButton, Pos.TOP_LEFT);
        StackPane.setMargin(backButton, new Insets(20, 0, 0, 25));

        VBox content = new VBox(30, title, soundControlsBox);
        content.setAlignment(Pos.CENTER);

        root.getChildren().addAll(vignettePane, content, backButton);

        if (sceneManager.getCurrentVirtualScene() != null && sceneManager.getCurrentVirtualScene() != sceneManager.getstartMenuScene()) {
            content.getChildren().add(continueButton);
        } else {
            content.getChildren().add(createGameRulesBox());
        }
        
        return new Scene(root);
    }

    private VBox createGameRulesBox() {
        Label gameLabel = UIFactory.createCustomLabel("⚔  GAME RULES", 18, "#E8E8E8", false);
        Label playersLabel = UIFactory.createCustomLabel("Players: " + controller.getNumberOfPlayers(), 14, "#888888", false);
        Label roundsLabel = UIFactory.createCustomLabel("Rounds: " + controller.getTotalRounds(), 14, "#888888", false);

        Slider playersSlider = UIFactory.createStyledSlider(2, 7, controller.getNumberOfPlayers());
        playersSlider.setMajorTickUnit(1);
        playersSlider.setMinorTickCount(0);
        playersSlider.setSnapToTicks(true);
        playersSlider.valueProperty().addListener((obs, oldVal, newVal) -> {playersLabel.setText("Players: " + newVal.intValue());});

        Slider roundsSlider = UIFactory.createStyledSlider(1, 20, controller.getTotalRounds());
        roundsSlider.setMajorTickUnit(1);
        roundsSlider.setMinorTickCount(0);
        roundsSlider.setSnapToTicks(true);
        roundsSlider.valueProperty().addListener((obs, oldVal, newVal) -> {roundsLabel.setText("Rounds: " + newVal.intValue());});

        Button applyBtn = UIFactory.create3DButton("APPLY SETTINGS", 13, 160, "10", "#C9A84C", "#6B4F10", "#0A0A0F");
        applyBtn.setOnAction(e -> {
            controller.setNumberOfPlayers((int) playersSlider.getValue());
            controller.setTotalRounds((int) roundsSlider.getValue());
            
            applyBtn.setText("APPLIED ✔");
            applyBtn.setStyle(applyBtn.getStyle().replace("#C9A84C", "#2ecc71").replace("#6B4F10", "#27ae60"));
            
            PauseTransition pause = new PauseTransition(Duration.seconds(1.5));
            pause.setOnFinished(ev -> {
                applyBtn.setText("APPLY SETTINGS");
                applyBtn.setStyle(applyBtn.getStyle().replace("#2ecc71", "#C9A84C").replace("#27ae60", "#6B4F10"));
            });
            pause.play();
        });

        VBox gameBox = new VBox(10, gameLabel, playersLabel, playersSlider, roundsLabel, roundsSlider, applyBtn);
        gameBox.setAlignment(Pos.CENTER_LEFT);
        gameBox.setPadding(new Insets(20));
        UIFactory.applyPanelStyle(gameBox);
        gameBox.setMaxWidth(360);

        return gameBox;
    }

    private Button makeMuteButton() {
        boolean muted = SoundManager.isMuted();
        Button btn = UIFactory.create3DButton(muted ? "🔇  UNMUTE ALL" : "🔊  MUTE ALL", 13, 160, "10", muted ? "#FF3333" : "#C9A84C", muted ? "#8B0000" : "#6B4F10", "#0A0A0F");
        btn.setOnAction(e -> {
            SoundManager.toggleMute();
            updateMuteBtnAppearance(btn);
        });
        return btn;
    }

    private void updateMuteBtnAppearance(Button btn) {
        boolean isMuted = SoundManager.isMuted();
        btn.setText(isMuted ? "🔇  UNMUTE ALL" : "🔊  MUTE ALL");
        String topHex = isMuted ? "#FF3333" : "#C9A84C";
        String shadowHex = isMuted ? "#8B0000" : "#6B4F10";
        btn.setStyle(String.format("-fx-background-color: linear-gradient(to bottom, %sDD, %s99); -fx-border-color: %s; -fx-border-width: 0 0 4 0; -fx-border-radius: 5; -fx-background-radius: 5; -fx-text-fill: #0A0A0F; -fx-cursor: hand; -fx-padding: 10; -fx-font-family: 'Georgia'; -fx-font-weight: bold; -fx-font-size: 13px;", topHex, topHex, shadowHex));
    }
}