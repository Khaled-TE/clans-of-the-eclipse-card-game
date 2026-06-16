package game.gui;

import game.engine.GameController;
import game.enums.Clan;
import game.enums.UnitType;
import game.units.*;
import game.utils.ImageCache;
import game.utils.UIFactory;
import javafx.animation.FadeTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import java.util.Arrays;
import java.util.List;

public class InfoScene {

    private SceneManager sceneManager;
    private ScrollPane currentActiveContent;

    public InfoScene(SceneManager sceneManager, GameController controller) {
        this.sceneManager = sceneManager;
    }

    public Scene buildScene() {
        StackPane root = new StackPane();
        root.setPrefSize(1280, 720);

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

        HBox mainLayout = new HBox(25);
        mainLayout.setPadding(new Insets(40));
        mainLayout.setAlignment(Pos.CENTER);
        mainLayout.setPrefSize(Double.MAX_VALUE, Double.MAX_VALUE);

        VBox sidebar = new VBox(25);
        sidebar.setAlignment(Pos.TOP_CENTER);
        sidebar.setPadding(new Insets(30, 20, 30, 20));
        UIFactory.applyPanelStyle(sidebar);
        sidebar.setMinWidth(300);
        sidebar.setMaxWidth(350);
        sidebar.prefWidthProperty().bind(root.widthProperty().multiply(0.25));
        sidebar.setMaxHeight(Double.MAX_VALUE);

        StackPane contentArea = new StackPane();
        contentArea.setPadding(new Insets(30));
        UIFactory.applyPanelStyle(contentArea);
        HBox.setHgrow(contentArea, Priority.ALWAYS);
        contentArea.setMaxHeight(Double.MAX_VALUE);

        Label sidebarTitle = UIFactory.createCustomLabel("LIBRARY", 28, "#C9A84C", true);
        
        Button playBtn = UIFactory.create3DButton("📜 HOW TO PLAY", 15, 230, "12", "#C9A84C", "#6B4F10", "#0A0A0F");
        Button scoreBtn = UIFactory.create3DButton("💯 SCORING SYSTEM", 15, 230, "12", "#C9A84C", "#6B4F10", "#0A0A0F");
        Button cardsBtn = UIFactory.create3DButton("🃏 CARDS & HIERARCHY", 15, 230, "12", "#C9A84C", "#6B4F10", "#0A0A0F");
        
        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);
        
        Button backBtn = UIFactory.createFlatButton("⬅ BACK TO MENU", 16, "#FF3333", true);

        sidebar.getChildren().addAll(sidebarTitle, playBtn, scoreBtn, cardsBtn, spacer, backBtn);

        this.currentActiveContent = buildHowToPlayContent();
        contentArea.getChildren().add(this.currentActiveContent);

        playBtn.setOnAction(e -> switchContent(contentArea, buildHowToPlayContent()));
        scoreBtn.setOnAction(e -> switchContent(contentArea, buildScoringContent()));
        cardsBtn.setOnAction(e -> switchContent(contentArea, buildCardsContent()));
        backBtn.setOnAction(e -> sceneManager.showStartMenu());

        mainLayout.getChildren().addAll(sidebar, contentArea);
        root.getChildren().addAll(vignettePane, mainLayout);

        return new Scene(root);
    }

    private void switchContent(StackPane container, ScrollPane newContent) {
        if (this.currentActiveContent == newContent) return;

        FadeTransition fadeOut = new FadeTransition(Duration.millis(150), this.currentActiveContent);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);
        fadeOut.setOnFinished(e -> {
            container.getChildren().clear();
            container.getChildren().add(newContent);
            this.currentActiveContent = newContent;

            FadeTransition fadeIn = new FadeTransition(Duration.millis(200), newContent);
            fadeIn.setFromValue(0.0);
            fadeIn.setToValue(1.0);
            fadeIn.play();
        });
        fadeOut.play();
    }

    private ScrollPane buildHowToPlayContent() {
        VBox box = new VBox(20);
        
        Label title = UIFactory.createCustomLabel("HOW TO PLAY", 36, "#C9A84C", true);
        
        String rules = "Clans of the Eclipse is a trick-taking game of strategy and prediction.\n\n" +
                       "1. PROPHECY PHASE:\n" +
                       "Analyze your hand and predict exactly how many battles you will win this round.\n\n" +
                       "2. BATTLE PHASE:\n" +
                       "Players take turns playing one card. The first card played (by the Leader) determines the 'Battle Clan'. Others must try to win using hierarchy.\n\n" +
                       "3. WINNING A BATTLE:\n" +
                       "- The highest power card of the Battle Clan wins, UNLESS special units or Shadow Clan are present.\n" +
                       "- Shadow Clan units always defeat regular Clan units.\n" +
                       "- Special units (Overlord, Oracle, Raider, Scout) have rules that override basic power levels.\n\n" +
                       "4. END OF ROUND:\n" +
                       "Points are calculated based on prophecy accuracy and bonus achievements.";

        Label body = new Label(rules);
        body.setStyle("-fx-font-family: 'Georgia'; -fx-font-size: 18px; -fx-text-fill: #EAEAEA; -fx-line-spacing: 6px;");
        body.setWrapText(true);

        box.getChildren().addAll(title, body);
        return createScrollPane(box);
    }

    private ScrollPane buildScoringContent() {
        VBox box = new VBox(20);

        Label title = UIFactory.createCustomLabel("SCORING SYSTEM", 36, "#C9A84C", true);
        
        String scoringRules = "A. BASE SCORE (PROPHECY > 0):\n" +
                              "✔ If Correct: Score = 20 × Prophecy\n" +
                              "✖ If Wrong: Score = -10 × Difference\n\n" +
                              "B. BASE SCORE (ZERO PROPHECY):\n" +
                              "Predicting 0 is high risk/reward.\n" +
                              "✔ If Correct: Score = 10 × Round_Number\n" +
                              "✖ If Wrong: Score = -10 × Round_Number\n\n" +
                              "C. BONUS SCORING:\n" +
                              "Only awarded if your initial Prophecy was correct.\n" +
                              "- Capture a Clan King: +10 Points (Shadow King +20)\n" +
                              "- Overlord defeats Raider: +30 Points\n" +
                              "- Oracle defeats Overlord: +50 Points";

        Label body = new Label(scoringRules);
        body.setStyle("-fx-font-family: 'Georgia'; -fx-font-size: 18px; -fx-text-fill: #EAEAEA; -fx-line-spacing: 6px;");
        body.setWrapText(true);

        box.getChildren().addAll(title, body);
        return createScrollPane(box);
    }

    private ScrollPane buildCardsContent() {
        VBox box = new VBox(40);
        
        Label title = UIFactory.createCustomLabel("CARDS & HIERARCHY", 36, "#C9A84C", true);
        box.getChildren().add(title);

        box.getChildren().add(buildCategorySection(
            "SPECIAL UNITS", "#C9A84C", 
            "Special units dictate the flow of the game, overriding basic power rules.\n\n" +
            "• OVERLORD: Supreme ruler. Beats all clan units, Raiders, and Scouts. Loses ONLY to Oracle.\n" +
            "• ORACLE: Master of fate. Beats the Overlord. Loses to Raiders.\n" +
            "• RAIDER: Elite warrior. Beats Oracles and all clan units. Loses to Overlord.\n" +
            "• SCOUT: Tactical disruptor. Low power but can be played anytime.\n" +
            "• SHAPESHIFTER: Adapts dynamically. Can be declared as RAIDER or SCOUT.\n" +
            "• LEVIATHAN: Destroyer of battles. Annihilates the current battle.",
            Arrays.asList(
                new Overlord(UnitType.OVERLORD, ""), new Oracle(UnitType.ORACLE, ""),
                new Raider(UnitType.RAIDER, ""), new Scout(UnitType.SCOUT, ""),
                new Shapeshifter(UnitType.SHAPESHIFTER, ""), new Leviathan(UnitType.LEVIATHAN, "")
            )
        ));

        box.getChildren().add(buildCategorySection(
            "FIRE CLAN", "#FF4500", 
            "The fierce Fire Clan.\n\nBasic units are numbered 1-13.\nThe Fire King (Power 14) is the strongest Fire unit.\n\nBeats other regular clan units ONLY when Fire is the declared battle clan.",
            Arrays.asList(new ClanUnit(UnitType.CLAN, Clan.FIRE, 10), new KingUnit(Clan.FIRE))
        ));

        box.getChildren().add(buildCategorySection(
            "EARTH CLAN", "#4CAF50", 
            "The resilient Earth Clan.\n\nBasic units are numbered 1-13.\nThe Earth King (Power 14) is the strongest Earth unit.\n\nBeats other regular clan units ONLY when Earth is the declared battle clan.",
            Arrays.asList(new ClanUnit(UnitType.CLAN, Clan.EARTH, 10), new KingUnit(Clan.EARTH))
        ));

        box.getChildren().add(buildCategorySection(
            "WATER CLAN", "#1E90FF", 
            "The fluid Water Clan.\n\nBasic units are numbered 1-13.\nThe Water King (Power 14) is the strongest Water unit.\n\nBeats other regular clan units ONLY when Water is the declared battle clan.",
            Arrays.asList(new ClanUnit(UnitType.CLAN, Clan.WATER, 10), new KingUnit(Clan.WATER))
        ));

        box.getChildren().add(buildCategorySection(
            "SHADOW CLAN", "#9B59B6", 
            "The elusive Shadow Clan.\n\nBasic units are numbered 1-13.\nThe Shadow King (Power 14) is the strongest Shadow unit.\n\nShadow units beat ALL other regular clan units regardless of the battle clan. Use them wisely.",
            Arrays.asList(new ClanUnit(UnitType.CLAN, Clan.SHADOW, 10), new KingUnit(Clan.SHADOW))
        ));

        VBox hierarchyBox = new VBox(15);
        hierarchyBox.setPadding(new Insets(20, 0, 0, 0));
        Label hTitle = UIFactory.createCustomLabel("POWER HIERARCHY SUMMARY", 28, "#FFD700", true);
        
        String hierarchyText = "1. Overlord\n" +
                               "2. Raiders (ties: first Raider played wins)\n" +
                               "3. Oracle (ties: first Oracle played wins)\n" +
                               "4. Shadow Clan units (highest wins)\n" +
                               "5. Battle Clan units (highest wins)\n" +
                               "6. Scouts\n\n" +
                               "Absolute Exception: Oracle always beats Overlord.\n" +
                               "Destruction: Leviathan destroys the battle (no winner).";
                               
        Label hDesc = new Label(hierarchyText);
        hDesc.setStyle("-fx-font-family: 'Georgia'; -fx-font-size: 18px; -fx-text-fill: #EAEAEA; -fx-line-spacing: 8px; -fx-font-weight: bold;");
        hierarchyBox.getChildren().addAll(hTitle, hDesc);
        
        box.getChildren().add(hierarchyBox);

        return createScrollPane(box);
    }

    private VBox buildCategorySection(String titleStr, String colorHex, String descStr, List<Unit> units) {
        VBox section = new VBox(15);
        Label title = UIFactory.createCustomLabel(titleStr, 24, colorHex, true);
        
        HBox contentBox = new HBox(30);
        contentBox.setAlignment(Pos.CENTER_LEFT);
        
        FlowPane cardsPane = new FlowPane(15, 15);
        cardsPane.setAlignment(Pos.CENTER_LEFT);
        
        cardsPane.setMinWidth(310);
        cardsPane.setMaxWidth(310);
        cardsPane.setPrefWrapLength(310);
        
        for (Unit u : units) {
            CardNode c = new CardNode(u);
            c.setPrefSize(140, 200);
            DropShadow ds = new DropShadow(15, Color.BLACK);
            ds.setOffsetY(5);
            c.setEffect(ds);
            cardsPane.getChildren().add(c);
        }
        
        Label desc = new Label(descStr);
        desc.setStyle("-fx-font-family: 'Georgia'; -fx-font-size: 16px; -fx-text-fill: #CCCCCC; -fx-line-spacing: 6px;");
        desc.setWrapText(true);
        HBox.setHgrow(desc, Priority.ALWAYS);
        
        contentBox.getChildren().addAll(cardsPane, desc);
        section.getChildren().addAll(title, contentBox);
        
        return section;
    }

    private ScrollPane createScrollPane(VBox content) {
        content.setStyle("-fx-background-color: transparent;");
        content.setPadding(new Insets(10, 20, 30, 10));
        
        ScrollPane scroll = new ScrollPane(content);
        scroll.setStyle("-fx-background: transparent; -fx-background-color: transparent; -fx-padding: 0;");
        scroll.setFitToWidth(true);
        
        scroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        return scroll;
    }
}