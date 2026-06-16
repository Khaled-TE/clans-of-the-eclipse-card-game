package game.gui;

import game.engine.GameController;
import game.enums.DeclaredIdentity;
import game.enums.RoundPhase;
import game.model.Battle;
import game.model.GameStatus;
import game.model.PlayedUnit;
import game.model.Player;
import game.model.RoundRecord;
import game.units.Unit;
import game.utils.ImageCache;
import game.utils.SoundManager;
import game.utils.UIFactory;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.CacheHint;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.ParallelTransition;
import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

public class GameArenaScene {
    private SceneManager sceneManager;
    private GameController controller;
    private HBox handContainer;
    private VBox opponentsContainer;
    private StackPane arenaTable;
    private Label roundLabel;
    private Label phaseLabel;
    private Label playerNameLabel;
    private Label winsLabel;
    private Label prophecyLabel;
    private ProphecySelector prophecySelector;
    private Button submitProphecyButton;
    private Button eyeButton;
    private boolean cardsRevealed = false;
    private boolean isTransitioning = false;
    private StackPane rootLayers;
    private VBox leftWrapper;

    public GameArenaScene(SceneManager sceneManager, GameController controller) {
        this.sceneManager = sceneManager;
        this.controller = controller;
    }

    public Scene buildScene() {
        rootLayers = new StackPane();

        ImageView stoneBackground = new ImageView(ImageCache.getFullResImage("/background/main_bg"));
        stoneBackground.setPreserveRatio(false);
        stoneBackground.setSmooth(true);
        stoneBackground.setCache(true);
        stoneBackground.setCacheHint(CacheHint.QUALITY);
        stoneBackground.fitWidthProperty().bind(rootLayers.widthProperty());
        stoneBackground.fitHeightProperty().bind(rootLayers.heightProperty());

        NumberBinding tableSize = Bindings.min(rootLayers.widthProperty().multiply(0.85), rootLayers.heightProperty().multiply(0.85));

        ImageView tableImgView = new ImageView(ImageCache.getFullResImage("/background/wood_table"));
        tableImgView.fitWidthProperty().bind(tableSize);
        tableImgView.fitHeightProperty().bind(tableSize);
        tableImgView.setPreserveRatio(true);
        tableImgView.setSmooth(true);
        tableImgView.setCache(true);
        tableImgView.setCacheHint(CacheHint.QUALITY);
        
        Circle clip = new Circle();
        clip.radiusProperty().bind(tableSize.divide(2));
        clip.centerXProperty().bind(tableSize.divide(2));
        clip.centerYProperty().bind(tableSize.divide(2));
        tableImgView.setClip(clip);
        
        StackPane tableSurface = new StackPane(tableImgView);
        tableSurface.setEffect(new DropShadow(BlurType.THREE_PASS_BOX, Color.rgb(0, 0, 0, 0.9), 60, 0, 0, 30));
        
        arenaTable = new StackPane();
        arenaTable.setAlignment(Pos.CENTER);
        arenaTable.prefWidthProperty().bind(tableSize);
        arenaTable.prefHeightProperty().bind(tableSize);
        arenaTable.maxWidthProperty().bind(tableSize);
        arenaTable.maxHeightProperty().bind(tableSize);
        arenaTable.setStyle("-fx-background-color: transparent;");
        
        StackPane tableContainer = new StackPane(tableSurface, arenaTable);
        tableContainer.setAlignment(Pos.CENTER);
        tableContainer.setTranslateY(-50);
        tableContainer.setRotationAxis(javafx.scene.transform.Rotate.X_AXIS);
        tableContainer.setRotate(-45);

        BorderPane rootLayout = new BorderPane();
        rootLayout.setMinSize(0, 0);
        rootLayout.setPadding(new Insets(20, 20, 20, 20));
        rootLayout.setPickOnBounds(false);

        BorderPane topBar = new BorderPane();
        topBar.setPadding(new Insets(10, 20, 10, 20));
        topBar.setLeft(createIconButton("/background/Papyrus", 80, sceneManager::showScoreboardScene));
        topBar.setRight(createIconButton("/background/gear", 80, sceneManager::showSettings));
        
        HBox centerLabels = new HBox(30);
        centerLabels.setAlignment(Pos.CENTER);
        centerLabels.setMaxWidth(350);
        centerLabels.setMaxHeight(45);
        UIFactory.applyPanelStyle(centerLabels);
        centerLabels.setPadding(new Insets(10, 20, 10, 20));
        
        roundLabel = createStyledLabel("#eaddcf", 18);
        phaseLabel = createStyledLabel("#C9A84C", 18);
        centerLabels.getChildren().addAll(roundLabel, phaseLabel);
        topBar.setCenter(centerLabels);
        rootLayout.setTop(topBar);

        opponentsContainer = new VBox(10);
        opponentsContainer.setAlignment(Pos.TOP_LEFT);

        javafx.scene.control.ScrollPane opponentsScroller = new javafx.scene.control.ScrollPane(opponentsContainer);
        opponentsScroller.setFitToWidth(true);
        opponentsScroller.setHbarPolicy(javafx.scene.control.ScrollPane.ScrollBarPolicy.NEVER);
        opponentsScroller.setVbarPolicy(javafx.scene.control.ScrollPane.ScrollBarPolicy.NEVER);
        opponentsScroller.setStyle("-fx-background-color: transparent; -fx-background: transparent; -fx-control-inner-background: transparent; -fx-padding: 0; -fx-border-color: transparent;");
        opponentsScroller.maxHeightProperty().bind(rootLayers.heightProperty().multiply(0.6));

        Label leftLabel = createStyledLabel("#C9A84C", 19);
        leftLabel.setText("OPPOSING PLAYERS");

        leftWrapper = new VBox(5);
        leftWrapper.setAlignment(Pos.CENTER_LEFT);
        leftWrapper.getChildren().addAll(leftLabel, opponentsScroller);
        rootLayout.setLeft(leftWrapper);

        VBox turnContainer = new VBox(10);
        turnContainer.setAlignment(Pos.TOP_LEFT);
        turnContainer.setMaxWidth(260);
        turnContainer.setMaxHeight(95);
        UIFactory.applyPanelStyle(turnContainer);
        turnContainer.setPadding(new Insets(10, 20, 10, 20));

        Label rightLabel = createStyledLabel("#C9A84C", 19);
        rightLabel.setText("TURN PLAYER");
        playerNameLabel = createStyledLabel("#8b9bb4", 17);
        winsLabel = createStyledLabel("#eaddcf", 12);
        prophecyLabel = createStyledLabel("#eaddcf", 12);
        turnContainer.getChildren().addAll(playerNameLabel, winsLabel, prophecyLabel);

        VBox rightWrapper = new VBox(5);
        rightWrapper.setAlignment(Pos.CENTER_LEFT);
        rightWrapper.getChildren().addAll(rightLabel, turnContainer);
        rootLayout.setRight(rightWrapper);

        VBox bottomControls = new VBox(15);
        bottomControls.setAlignment(Pos.CENTER);
        bottomControls.setPickOnBounds(false);

        handContainer = new HBox(-30);
        handContainer.setAlignment(Pos.CENTER);
        handContainer.setMaxWidth(800);

        HBox actionActions = new HBox(20);
        actionActions.setAlignment(Pos.CENTER);

        eyeButton = UIFactory.create3DButton("View Cards", 13, 140, "8 18 8 18", "#5d4037", "#3e2723", "#FFD700");
        eyeButton.setOnAction(e -> {cardsRevealed = !cardsRevealed; refreshUI();});

        prophecySelector = new ProphecySelector();
        
        submitProphecyButton = UIFactory.create3DButton("Confirm Prophecy", 13, 140, "8 18 8 18", "#b8860b", "#8b6914", "#FFFFFF");
        submitProphecyButton.setOnAction(e -> handleProphecySubmission());

        actionActions.getChildren().addAll(eyeButton, prophecySelector, submitProphecyButton);
        bottomControls.getChildren().addAll(handContainer, actionActions);
        rootLayout.setBottom(bottomControls);
        rootLayers.getChildren().addAll(stoneBackground, tableContainer, rootLayout);

        refreshUI();
        Scene scene = new Scene(rootLayers, -1, -1, false, SceneAntialiasing.BALANCED);

        PerspectiveCamera camera = new PerspectiveCamera(false);
        camera.setFieldOfView(55);
        scene.setCamera(camera);
        return scene;
    }

    private Label createStyledLabel(String hexColor, int size) {
        Label label = UIFactory.createCustomLabel("", size, hexColor, false);
        label.setEffect(new DropShadow(2, Color.BLACK));
        return label;
    }

    public void refreshUI() {
        GameStatus status = controller.getGameStatus();
        if (status == null) return;

        renderArenaTable();

        int currentRound = status.getRoundNumber();
        int maxRounds = controller.getTotalRounds();
        int completedRounds = controller.getScoreSheet() != null ? controller.getScoreSheet().getRoundRecords().size() : 0;
        boolean isGameOver = (status.getPhase() == RoundPhase.SCORING) && (currentRound > maxRounds || currentRound == 999);

        if (isGameOver) {
            roundLabel.setText("Round: " + completedRounds);
            phaseLabel.setText("Phase: FINISHED");
            javafx.application.Platform.runLater(() -> sceneManager.showWinnerScene());
            return;
        }

        int displayRound = currentRound > maxRounds ? maxRounds : currentRound;
        roundLabel.setText("Round: " + displayRound);
        phaseLabel.setText("Phase: " + status.getPhase());

        List<Player> players = status.getPlayers();
        Player activePlayer = players.get(status.getTurn());
        playerNameLabel.setText("PLAYER: " + activePlayer.getName());
        winsLabel.setText("Wins: " + activePlayer.getNumberOfWins());

        RoundRecord currentRecord = status.getRoundRecords().isEmpty() ? null : status.getCurrentRound();
        if (currentRecord != null && currentRecord.hasProphecy(activePlayer)) {
            prophecyLabel.setText("Prophecy: " + currentRecord.getProphecy(activePlayer));
        } else {
            prophecyLabel.setText("Prophecy: -");
        }

        List<Player> currentOpponents = new ArrayList<>();
        for (Player p : players) {
            if (!p.equals(activePlayer)) {
                currentOpponents.add(p);
            }
        }

        if (opponentsContainer.getChildren().isEmpty()) {
            for (int i = 0; i < currentOpponents.size(); i++) {
                VBox oppBox = new VBox(4);
                UIFactory.applyPanelStyle(oppBox);
                oppBox.setPadding(new Insets(10));
                oppBox.setMaxWidth(220);
                oppBox.setMinHeight(javafx.scene.layout.Region.USE_PREF_SIZE);

                Label nameL = createStyledLabel("#8b9bb4", 17);
                Label WL = createStyledLabel("#eaddcf", 12);
                Label PL = createStyledLabel("#eaddcf", 12);

                oppBox.getChildren().addAll(nameL, WL, PL);
                opponentsContainer.getChildren().add(oppBox);
            }
        }

        for (int i = 0; i < currentOpponents.size(); i++) {
            Player p = currentOpponents.get(i);
            VBox oppBox = (VBox) opponentsContainer.getChildren().get(i);
            
            oppBox.setUserData(p);

            Label nameL = (Label) oppBox.getChildren().get(0);
            Label WL = (Label) oppBox.getChildren().get(1);
            Label PL = (Label) oppBox.getChildren().get(2);

            nameL.setText("PLAYER: " + p.getName());
            WL.setText("Wins: " + p.getNumberOfWins());

            String propStr = "-";
            if (currentRecord != null && currentRecord.hasProphecy(p)) {
                propStr = String.valueOf(currentRecord.getProphecy(p));
            }
            PL.setText("Prophecy: " + propStr);
        }

        eyeButton.setDisable(false);

        if (status.getPhase() == RoundPhase.DRAW) {
            if (isTransitioning) return;
            isTransitioning = true;

            arenaTable.getChildren().clear();
            handContainer.getChildren().clear();
            prophecySelector.setVisible(false);
            submitProphecyButton.setVisible(false);
            eyeButton.setVisible(false);
            leftWrapper.setVisible(false);

            showTransitionBanner("Round " + currentRound, this::onDrawBannerComplete);
        } 
        else if (status.getPhase() == RoundPhase.PROPHECY) {
            eyeButton.setVisible(true);
            prophecySelector.setVisible(true);
            submitProphecyButton.setVisible(true);
            leftWrapper.setVisible(false);

            prophecySelector.configureForRound(activePlayer.getHand().size());
            
            renderHand(activePlayer, true);
        }
        else if (status.getPhase() == RoundPhase.BATTLE) {
            prophecySelector.setVisible(false);
            submitProphecyButton.setVisible(false);
            eyeButton.setVisible(true);
            leftWrapper.setVisible(true);
            renderHand(activePlayer, false);
        }
        else if (status.getPhase() == RoundPhase.SCORING) {
            controller.saveGame();
            arenaTable.getChildren().clear();
            controller.startNextRound();
            cardsRevealed = false;
            handContainer.getChildren().clear();
            isTransitioning = false;
            refreshUI();
        }
    }

    private void onDrawBannerComplete() {
        controller.advanceToProphecyPhase();
        isTransitioning = false;
        refreshUI();
    }

    private void renderHand(Player player, boolean isProphecyPhase) {
        handContainer.getChildren().clear();
        List<Unit> hand = player.getHand();
        int numCards = hand.size();
        if (numCards == 0) return;

        double dynamicSpacing = numCards > 7 ? -30 - ((numCards - 7) * 4.5) : -30;
        handContainer.setSpacing(dynamicSpacing);
        double centerIndex = (numCards - 1) / 2.0;

        double angleMultiplier = numCards > 7 ? 3.0 : 6.0;
        double yMultiplier = numCards > 7 ? 1.2 : 4.0;

        for (int i = 0; i < numCards; i++) {
            int index = i;
            Unit unit = hand.get(index);
            double distanceFromCenter = i - centerIndex;
            double angle = distanceFromCenter * angleMultiplier;
            double yTranslation = Math.pow(distanceFromCenter, 2) * yMultiplier;

            if (!cardsRevealed) {
                handContainer.getChildren().add(createHiddenCard(angle, yTranslation));
            } else {
                CardNode cardNode = new CardNode(unit);
                if (!isProphecyPhase) {
                    boolean isAllowed = controller.canPlayCard(player, unit);
                    if (!isAllowed) {
                        cardNode.setOpacity(0.3);
                        cardNode.setDisable(true);
                        cardNode.setOnMouseEntered(null);
                    } else {
                        cardNode.setStyle("-fx-background-color: transparent;");
                        cardNode.setOnMouseClicked(e -> handleCardPlayback(player, index, unit));
                    }
                } else {
                    cardNode.setOnMouseEntered(null);
                    cardNode.setCursor(javafx.scene.Cursor.DEFAULT);
                }

                StackPane arcWrapper = new StackPane(cardNode);
                arcWrapper.setRotate(angle);
                arcWrapper.setTranslateY(yTranslation);
                cardNode.hoverProperty().addListener((obs, old, isHovered) -> {
                    if (isHovered && !cardNode.isDisabled()) {
                        arcWrapper.setViewOrder(-1.0);
                    } else {
                        arcWrapper.setViewOrder(0.0);
                    }
                });
                handContainer.getChildren().add(arcWrapper);
            }
        }
    }

    private void dropCardOnTable(Unit unit, DeclaredIdentity identity) {
        CardNode playedCard = (identity != null) ? new CardNode(identity) : new CardNode(unit);
        applyPlayedCardStyle(playedCard, identity);

        double randomZAngle = (Math.random() * 100) - 50;
        StackPane centerWrapper = new StackPane(playedCard);
        centerWrapper.setRotate(randomZAngle);
        arenaTable.getChildren().add(centerWrapper);
    }

    private void renderArenaTable() {
        List<PlayedUnit> roundCards = controller.getAllRoundCards();

        if (roundCards == null || roundCards.isEmpty()) {
            if (!isTransitioning) {
                arenaTable.getChildren().clear();
            }
            return;
        }

        if (arenaTable.getChildren().size() < roundCards.size()) {
            arenaTable.getChildren().clear();

            for (int i = 0; i < roundCards.size(); i++) {
                PlayedUnit pu = roundCards.get(i);
                if (pu == null) continue;

                CardNode playedCard = new CardNode(pu);
                applyPlayedCardStyle(playedCard, pu.getIdentity());

                double deterministicAngle = Math.sin(i) * 50;
                StackPane centerWrapper = new StackPane(playedCard);
                centerWrapper.setRotate(deterministicAngle);

                arenaTable.getChildren().add(centerWrapper);
            }
        }
    }

    private void handleProphecySubmission() {
        GameStatus status = controller.getGameStatus();
        Player activePlayer = status.getPlayers().get(status.getTurn());

        try {
            controller.submitPlayerProphecy(activePlayer, prophecySelector.getValue());
            cardsRevealed = false;
            refreshUI();
        } catch (Exception e) {
            showErrorAlert(e.getMessage());
        }
    }

    private void handleCardPlayback(Player player, int index, Unit unit) {
        if (unit.getType() == game.enums.UnitType.SHAPESHIFTER) {
            handContainer.getChildren().clear();
            eyeButton.setDisable(true);
            Label instruction = createStyledLabel("#FFD700", 24);
            instruction.setText("CHOOSE IDENTITY:");

            CardNode raiderCard = new CardNode(DeclaredIdentity.RAIDER);
            raiderCard.setOnMouseClicked(e -> playSelectedCard(player, index, DeclaredIdentity.RAIDER, unit));

            CardNode scoutCard = new CardNode(DeclaredIdentity.SCOUT);
            scoutCard.setOnMouseClicked(e -> playSelectedCard(player, index, DeclaredIdentity.SCOUT, unit));

            handContainer.setSpacing(40);
            handContainer.getChildren().addAll(instruction, raiderCard, scoutCard);
        } else {
            playSelectedCard(player, index, null, unit);
        }
    }

    private void playSelectedCard(Player player, int index, DeclaredIdentity identity, Unit unit) {
        try {
            int cardsBefore = controller.getAllRoundCards() != null ? controller.getAllRoundCards().size() : 0;
            int playersCount = controller.getNumberOfPlayers();

            controller.playCard(player, index, identity);
            
            cardsRevealed = false;
            handContainer.setVisible(false);
            javafx.application.Platform.runLater(() -> {
                handContainer.getChildren().clear();
                handContainer.setVisible(true);
            });
            eyeButton.setDisable(true);

            ArrayList<Battle> battles = controller.getGameStatus().getCurrentRound().getBattleRecord();
            Battle currentBattle = battles.isEmpty() ? null : battles.get(battles.size() - 1);

            boolean battleEnded = ((cardsBefore + 1) % playersCount == 0);
            
            forceStatsSync();

            animateCardThrow(unit, identity, () -> {
                if (battleEnded) {
                    forceStatsSync();
                    String transitionMessage;
                    
                    if (currentBattle != null && currentBattle.isLeviathan()) {
                        transitionMessage = "BATTLE DESTROYED!";
                    } else {
                        Player winner = controller.getGameStatus().getPlayers().get(controller.getGameStatus().getWinner());
                        transitionMessage = winner.getName() + " WINS!";
                    }
                    showTransitionBanner(transitionMessage, () -> refreshUI());
                } else {
                    refreshUI();
                }
            });
        } catch (Exception e) {
            showErrorAlert(e.getMessage());
            refreshUI();
        }
    }

    private void animateCardThrow(Unit unit, DeclaredIdentity identity, Runnable onFinished) {
        CardNode flyingCard = (identity != null) ? new CardNode(identity) : new CardNode(unit);
        flyingCard.setOnMouseEntered(null);
        flyingCard.setOnMouseExited(null);
        flyingCard.setCursor(javafx.scene.Cursor.DEFAULT);
        flyingCard.setTranslateY(350);
        flyingCard.setScaleX(1.2);
        flyingCard.setScaleY(1.2);
        flyingCard.setRotationAxis(javafx.scene.transform.Rotate.X_AXIS);
        flyingCard.setMouseTransparent(true);

        rootLayers.getChildren().add(flyingCard);
        Duration duration = Duration.millis(500);

        TranslateTransition tt = new TranslateTransition(duration, flyingCard);
        tt.setToY(30);
        tt.setInterpolator(Interpolator.EASE_OUT);

        RotateTransition rt = new RotateTransition(duration, flyingCard);
        rt.setToAngle(-40);

        ScaleTransition st = new ScaleTransition(duration, flyingCard);
        st.setToX(1.0);
        st.setToY(1.0);

        ParallelTransition pt = new ParallelTransition(tt, rt, st);
        pt.setOnFinished(e -> {
            rootLayers.getChildren().remove(flyingCard);
            dropCardOnTable(unit, identity);
            onFinished.run();
        });
        SoundManager.playSFX("card_play.mp3");
        pt.play();
    }

    private void showTransitionBanner(String message, Runnable onFinished) {
        javafx.animation.PauseTransition layoutDelay = new javafx.animation.PauseTransition(Duration.millis(100));
        layoutDelay.setOnFinished(event -> {
            javafx.scene.text.Text roundText = new javafx.scene.text.Text(message);

            double dynamicFontSize = 85.0;
            if (message.length() > 8) {
                dynamicFontSize = Math.max(60.0, 85.0 - ((message.length() - 8) * 4.0));
            }
            roundText.setFont(Font.font("System", FontWeight.BOLD, dynamicFontSize));
            roundText.setStyle("-fx-fill: linear-gradient(to bottom, #ffffff, #8899a6); -fx-stroke: #1a1a24; -fx-stroke-width: 1.5; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.9), 20, 0.5, 0, 15);");
            roundText.setRotationAxis(javafx.scene.transform.Rotate.X_AXIS);
            roundText.setRotate(30);

            double screenW = rootLayers.getWidth();
            double screenH = rootLayers.getHeight();

            StackPane bannerContainer = new StackPane(roundText);
            bannerContainer.setStyle("-fx-background-color: transparent;");
            bannerContainer.setMouseTransparent(true);
            bannerContainer.setTranslateX(-screenW * 1.2);
            bannerContainer.setTranslateY(screenH * 0.15);
            bannerContainer.setRotate(-35);

            rootLayers.getChildren().add(bannerContainer);
            Duration animationSpeed = Duration.millis(900);

            TranslateTransition enterTranslate = new TranslateTransition(animationSpeed, bannerContainer);
            enterTranslate.setToX(0);
            enterTranslate.setToY(-screenH * 0.32);

            RotateTransition enterRotate = new RotateTransition(animationSpeed, bannerContainer);
            enterRotate.setToAngle(0);

            ParallelTransition clockPhase1 = new ParallelTransition(enterTranslate, enterRotate);
            clockPhase1.setInterpolator(Interpolator.EASE_OUT);

            FadeTransition stayPause = new FadeTransition(Duration.millis(900), bannerContainer);
            stayPause.setFromValue(1.0);
            stayPause.setToValue(1.0);

            TranslateTransition exitTranslate = new TranslateTransition(animationSpeed, bannerContainer);
            exitTranslate.setToX(screenW * 1.2);
            exitTranslate.setToY(-screenH * 0.05);

            RotateTransition exitRotate = new RotateTransition(animationSpeed, bannerContainer);
            exitRotate.setToAngle(35);

            FadeTransition exitFade = new FadeTransition(animationSpeed, bannerContainer);
            exitFade.setToValue(0.0);

            ParallelTransition clockPhase2 = new ParallelTransition(exitTranslate, exitRotate, exitFade);
            clockPhase2.setInterpolator(Interpolator.EASE_IN);

            SequentialTransition clockSequence = new SequentialTransition(clockPhase1, stayPause, clockPhase2);
            clockSequence.setOnFinished(e -> {
                rootLayers.getChildren().remove(bannerContainer);
                onFinished.run();
            });

            clockSequence.play();
        });
        layoutDelay.play();
    }

    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void applyPlayedCardStyle(CardNode playedCard, DeclaredIdentity identity) {
        playedCard.setOnMouseEntered(null);
        playedCard.setOnMouseExited(null);
        playedCard.setCursor(javafx.scene.Cursor.DEFAULT);
    }

    private Button createIconButton(String imagePath, double size, Runnable onClick) {
        Button btn = new Button();
        btn.setStyle("-fx-background-color: transparent; -fx-padding: 0; -fx-cursor: hand;");
        ImageView icon = new ImageView(ImageCache.getImage(imagePath));
        icon.setFitWidth(size);
        icon.setFitHeight(size);
        icon.setPreserveRatio(true);
        btn.setGraphic(icon);
        btn.setOnAction(e -> onClick.run());
        return btn;
    }

    private StackPane createHiddenCard(double rotate, double translateY) {
        StackPane card = new StackPane();
        card.setPrefSize(140, 200);
        card.setStyle("-fx-background-color: transparent;");
        
        ImageView cardBackImage = new ImageView(ImageCache.getImage("/cards/card_bg"));
        
        cardBackImage.setFitWidth(140);
        cardBackImage.setFitHeight(200);
        
        javafx.scene.shape.Rectangle clip = new javafx.scene.shape.Rectangle(140, 200);
        clip.setArcWidth(15);
        clip.setArcHeight(15);
        cardBackImage.setClip(clip);

        card.getChildren().add(cardBackImage);
        card.setRotate(rotate);
        card.setTranslateY(translateY);
        return card;
    }

    private void forceStatsSync() {
        GameStatus status = controller.getGameStatus();
        if (status == null) return;

        String currentUIName = playerNameLabel.getText().replace("PLAYER: ", "");

        for (Player p : status.getPlayers()) {
            if (p.getName().equals(currentUIName)) {
                winsLabel.setText("Wins: " + p.getNumberOfWins());
            } else {
                for (javafx.scene.Node node : opponentsContainer.getChildren()) {
                    if (node instanceof VBox) {
                        VBox oppBox = (VBox) node;
                        Player opp = (Player) oppBox.getUserData();
                        
                        if (opp != null && opp.getName().equals(p.getName())) {
                            Label oppWinsLabel = (Label) oppBox.getChildren().get(1);
                            oppWinsLabel.setText("Wins: " + p.getNumberOfWins());
                        }
                    }
                }
            }
        }
    }
}