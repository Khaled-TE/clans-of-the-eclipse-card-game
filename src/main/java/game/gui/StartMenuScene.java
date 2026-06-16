package game.gui;

import game.engine.GameController;
import game.utils.ImageCache;
import game.utils.UIFactory;
import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
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

public class StartMenuScene {

    private SceneManager sceneManager;
    private GameController controller;

    public StartMenuScene(SceneManager sceneManager, GameController controller) {
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
        vignette.setFill(new RadialGradient(0, 0, 0.5, 0.5, 0.85, true, CycleMethod.NO_CYCLE, new Stop(0.0, Color.TRANSPARENT), new Stop(1.0, Color.web("#000000", 0.85))));
        vignettePane.getChildren().add(vignette);

        Label title = UIFactory.createCustomLabel("CLANS OF THE ECLIPSE", 40, "#C9A84C", false);
        DropShadow titleGlow = new DropShadow(30, Color.web("#C9A84C"));
        titleGlow.setSpread(0.25);
        title.setEffect(titleGlow);

        FadeTransition titlePulse = new FadeTransition(Duration.seconds(3), title);
        titlePulse.setFromValue(0.85);
        titlePulse.setToValue(1.0);
        titlePulse.setAutoReverse(true);
        titlePulse.setCycleCount(Animation.INDEFINITE);
        titlePulse.play();

        Label subtitle = UIFactory.createCustomLabel("⚔  Four Clans. One Eclipse. One Ruler.  ⚔", 14, "#888888", false);
        subtitle.setStyle("-fx-font-family: 'Georgia'; -fx-font-style: italic; -fx-font-size: 14px; -fx-text-fill: #888888;");

        Button startBtn    = UIFactory.create3DButton("⚔  START GAME", 15, 270, "10", "#C9A84C", "#6B4F10", "#0A0A0F");
        Button loadBtn     = UIFactory.create3DButton("📂  LOAD GAME", 15, 270, "10", "#4C8FC9", "#10406B", "#0A0A0F");
        Button settingsBtn = UIFactory.create3DButton("⚙  SETTINGS", 15, 270, "10", "#666688", "#333355", "#0A0A0F");
        Button infoBtn     = UIFactory.create3DButton("📖  INFO", 15, 270, "10", "#666688", "#333355", "#0A0A0F");
        Button quitBtn     = UIFactory.create3DButton("✖  QUIT GAME", 15, 270, "10", "#FF3333", "#8B0000", "#0A0A0F");

        startBtn.setOnAction(ev -> sceneManager.showNamingScene());
        loadBtn.setOnAction(ev -> {controller.loadGame(); javafx.application.Platform.runLater(() -> sceneManager.showGameArena());});
        settingsBtn.setOnAction(ev -> sceneManager.showSettings());
        infoBtn.setOnAction(ev -> sceneManager.showInfoScene());
        quitBtn.setOnAction(ev -> {javafx.application.Platform.exit(); System.exit(0);});

        VBox content = new VBox(16, title, subtitle, startBtn);
        if (controller.hasSave()) {
            content.getChildren().add(loadBtn);
        }
        content.getChildren().addAll(settingsBtn, infoBtn, quitBtn);
        content.setAlignment(Pos.CENTER);
        VBox.setMargin(subtitle, new Insets(0, 0, 24, 0));

        root.getChildren().addAll(vignettePane, content);
        return new Scene(root);
    }
}