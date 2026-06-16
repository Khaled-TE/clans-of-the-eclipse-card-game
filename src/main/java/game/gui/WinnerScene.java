package game.gui;

import game.engine.GameController;
import game.model.Player;
import game.model.ScoreSheet;
import game.utils.ImageCache;
import game.utils.UIFactory;
import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class WinnerScene {
    private SceneManager sceneManager;
    private GameController controller;

    public WinnerScene(SceneManager sceneManager, GameController controller) {
        this.sceneManager = sceneManager;
        this.controller = controller;
    }

    public Scene buildScene() {
        StackPane root = new StackPane();
        root.setStyle("-fx-background-color: #050508;");

        ImageView bg = new ImageView(ImageCache.getFullResImage("/background/winner_bg"));
        bg.fitWidthProperty().bind(root.widthProperty());
        bg.fitHeightProperty().bind(root.heightProperty());

        VBox box = new VBox(35);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(50));
        box.getChildren().addAll(UIFactory.createCustomLabel("THE ECLIPSE IS COMPLETE", 28, "#A0A0B0", true), UIFactory.createCustomLabel("RULER OF THE ECLIPSE", 48, "#C9A84C", true));

        ScoreSheet sheet = controller.getScoreSheet();
        if (sheet != null) {
            for (Player w : sheet.getWinners()) {
                VBox pBox = new VBox(10);
                pBox.setAlignment(Pos.CENTER);
                pBox.getChildren().addAll(UIFactory.createCustomLabel(w.getName().toUpperCase(), 55, "#EAEAEA", true), UIFactory.createCustomLabel("Final Score: " + sheet.getTotalScoreMap().get(w), 26, "#8b0000", true));
                box.getChildren().add(pBox);
            }
        }

        HBox btnBox = new HBox(30);
        btnBox.setAlignment(Pos.CENTER);
        btnBox.setPadding(new Insets(40, 0, 0, 0));

        Button viewBtn = UIFactory.createFlatButton("View Chronicles ➡", 16, "#C9A84C", true);
        Button menuBtn = UIFactory.createFlatButton("Abandon Quest", 16, "#C9A84C", true);
        viewBtn.setOnAction(e -> sceneManager.showScoreboardScene());
        menuBtn.setOnAction(e -> sceneManager.showStartMenu());
        
        btnBox.getChildren().addAll(viewBtn, menuBtn);
        box.getChildren().add(btnBox);
        root.getChildren().addAll(bg, box);

        TranslateTransition tt = new TranslateTransition(Duration.seconds(4), box);
        tt.setFromY(-1000);
        tt.setToY(0);
        tt.setInterpolator(Interpolator.EASE_OUT);
        tt.play();

        return new Scene(root, 1280, 720);
    }
}