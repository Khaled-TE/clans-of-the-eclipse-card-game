package game.gui;

import java.util.List;
import java.util.Map;
import game.engine.GameController;
import game.model.Player;
import game.model.RoundRecord;
import game.model.ScoreSheet;
import game.utils.ImageCache;
import game.utils.UIFactory;
import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

public class ScoreboardScene {

    private SceneManager sceneManager;
    private GameController controller;

    public ScoreboardScene(SceneManager sceneManager, GameController controller) {
        this.sceneManager = sceneManager;
        this.controller = controller;
    }

    public Scene buildScene() {
        StackPane root = new StackPane();
        ImageView bg = new ImageView(ImageCache.getFullResImage("/background/main_bg"));
        bg.fitWidthProperty().bind(root.widthProperty()); 
        bg.fitHeightProperty().bind(root.heightProperty());

        Label title = UIFactory.createCustomLabel("CHRONICLES OF THE ECLIPSE", 36, "#EAEAEA", true);
        StackPane.setAlignment(title, Pos.TOP_CENTER); 
        StackPane.setMargin(title, new Insets(30, 0, 0, 0));

        ScoreSheet sheet = controller.getScoreSheet();
        List<Player> players = controller.getGameStatus() != null ? controller.getGameStatus().getPlayers() : null;

        StackPane papyrusLayer = new StackPane();
        papyrusLayer.setPrefSize(1100, 650); 
        papyrusLayer.setMaxSize(1100, 650);
        papyrusLayer.setStyle("-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.8), 30, 0, 0, 0);");
        StackPane.setMargin(papyrusLayer, new Insets(60, 0, 0, 0));

        ImageView papyrusImage = new ImageView(ImageCache.getFullResImage("/background/papyrus_bg"));
        papyrusImage.fitWidthProperty().bind(papyrusLayer.widthProperty()); 
        papyrusImage.fitHeightProperty().bind(papyrusLayer.heightProperty());

        VBox box = new VBox(15);
        box.setAlignment(Pos.CENTER); 
        box.setMaxWidth(1100);
        box.setPadding(new Insets(100, 120, 80, 120)); 

        if (sheet == null || players == null || sheet.getRoundRecords().isEmpty()) {
            Label noData = UIFactory.createCustomLabel("The scrolls remain empty. No battles fought yet.", 20, "#3e2723", false);
            
            HBox btnBox = new HBox(); 
            btnBox.setAlignment(Pos.CENTER); 
            btnBox.setPadding(new Insets(10, 0, 0, 0));
            Button btn = UIFactory.createFlatButton("⬅ Go Back", 16, "#3e2723", true);
            btn.setOnAction(e -> sceneManager.goBack()); 
            btnBox.getChildren().add(btn);
            
            box.getChildren().addAll(noData, btnBox);
            papyrusLayer.getChildren().addAll(papyrusImage, box);
            root.getChildren().addAll(bg, papyrusLayer, title);
            return new Scene(root, 1280, 720);
        }

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER); grid.setHgap(35); grid.setVgap(15);
        grid.setStyle("-fx-background-color: transparent; -fx-padding: 10;");
        
        String glow = "-fx-effect: dropshadow(gaussian, rgba(255,255,255,0.7), 4, 0.6, 0, 0); -fx-font-weight: bold; -fx-font-family: 'Georgia';";
        String hStyle = "-fx-text-fill: #2a1b10; -fx-font-size: 16px; " + glow;
        String dStyle = "-fx-text-fill: #1a1100; -fx-font-size: 15px; " + glow;

        Label rH = new Label("Round"); rH.setStyle(hStyle); rH.setMinWidth(Region.USE_PREF_SIZE); grid.add(rH, 0, 0);
        
        for (int i = 0; i < players.size(); i++) {
            VBox pBox = new VBox(5); pBox.setAlignment(Pos.CENTER);
            Label pName = new Label(players.get(i).getName()); pName.setStyle(hStyle); pName.setMinWidth(Region.USE_PREF_SIZE);
            String[] headers = {"Prop", "|", "Win", "|", "Cor", "|", "Base", "|", "Bon", "|", "Tot"};
            pBox.getChildren().addAll(pName, buildRow(headers, hStyle));
            grid.add(pBox, i + 1, 0);
        }

        int row = 1;
        for (RoundRecord rec : sheet.getRoundRecords()) {
            Label rL = new Label(" " + rec.getRoundNumber() + " ");
            rL.setStyle(dStyle + "-fx-border-color: #5d4037; -fx-border-width: 0 1 0 0; -fx-padding: 0 15 0 0;");
            rL.setMinWidth(Region.USE_PREF_SIZE);
            grid.add(rL, 0, row);

            for (int i = 0; i < players.size(); i++) {
                Player p = players.get(i);
                int pr = rec.getProphecy(p), w = rec.getWonBattles(p), bs = rec.getPlayerBaseScore(p), bn = rec.getPlayerBonusScore(p);
                String[] stats = {pr+"", "|", w+"", "|", (pr==w?"Y":"N"), "|", bs+"", "|", bn+"", "|", (bs+bn)+""};
                grid.add(buildRow(stats, dStyle), i + 1, row);
            }
            row++;
        }

        ScrollPane scroll = new ScrollPane(new VBox(grid));
        scroll.setMaxWidth(820);
        scroll.setStyle("-fx-background: transparent; -fx-background-color: transparent; -fx-padding: 0;");
        scroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        
        Platform.runLater(() -> {
            String[] clearEls = {".scroll-bar", ".track", ".track-background", ".increment-button", ".decrement-button", ".increment-arrow", ".decrement-arrow"};
            for (String el : clearEls) scroll.lookupAll(el).forEach(n -> n.setStyle("-fx-background-color: transparent; -fx-padding: 0;"));
            scroll.lookupAll(".thumb").forEach(n -> n.setStyle("-fx-background-color: #5d4037; -fx-background-radius: 5;"));
        });

        Line sep = new Line(0, 0, 750, 0); 
        sep.setStroke(Color.web("#5d4037")); sep.setStrokeWidth(2);
        
        FlowPane tPane = new FlowPane(Orientation.HORIZONTAL, 25, 10); 
        tPane.setAlignment(Pos.CENTER); tPane.setMaxWidth(820);
        Label fL = new Label("FINAL SCORE:"); fL.setStyle("-fx-text-fill: #8b0000; -fx-font-size: 19px; " + glow);
        tPane.getChildren().add(fL);

        Map<Player, Integer> totals = sheet.getTotalScoreMap();
        for (int i = 0; i < players.size(); i++) {
            Label tL = new Label(players.get(i).getName() + ": " + totals.get(players.get(i)));
            tL.setStyle("-fx-text-fill: #8b0000; -fx-font-size: 19px; " + glow);
            tPane.getChildren().add(tL);
        }

        HBox btnBox = new HBox(); btnBox.setAlignment(Pos.CENTER); btnBox.setPadding(new Insets(10, 0, 0, 0));
        Button backBtn = UIFactory.createFlatButton("⬅ Go Back", 16, "#3e2723", true);
        backBtn.setOnAction(e -> sceneManager.goBack()); 
        btnBox.getChildren().add(backBtn);

        box.getChildren().addAll(scroll, sep, tPane, btnBox);
        papyrusLayer.getChildren().addAll(papyrusImage, box);
        root.getChildren().addAll(bg, papyrusLayer, title);
        
        return new Scene(root);
    }

    private GridPane buildRow(String[] vals, String style) {
        GridPane sg = new GridPane(); sg.setAlignment(Pos.CENTER); sg.setHgap(2);
        double[] w = {45, 15, 45, 15, 35, 15, 45, 15, 45, 15, 45};
        for (int i = 0; i < w.length; i++) {
            ColumnConstraints cc = new ColumnConstraints(); 
            cc.setPrefWidth(w[i]); cc.setMinWidth(w[i]); cc.setHalignment(HPos.CENTER);
            sg.getColumnConstraints().add(cc);
            Label l = new Label(vals[i]); l.setStyle(style); l.setMinWidth(Region.USE_PREF_SIZE);
            sg.add(l, i, 0);
        }
        return sg;
    }
}