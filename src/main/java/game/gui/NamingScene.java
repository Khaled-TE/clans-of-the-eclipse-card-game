package game.gui;

import java.util.ArrayList;
import java.util.List;
import game.engine.GameController;
import game.utils.ImageCache;
import game.utils.UIFactory;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class NamingScene {

    private SceneManager sceneManager;
    private GameController controller;

    public NamingScene(SceneManager sceneManager, GameController controller) {
        this.sceneManager = sceneManager;
        this.controller = controller;
    }

    public Scene buildScene() {
        StackPane root = new StackPane(); 
        
        Image bgImage = ImageCache.getFullResImage("/background/bg");
        if (bgImage != null && !bgImage.isError()) {
            BackgroundImage bg = new BackgroundImage(bgImage, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, new BackgroundSize(1.0, 1.0, true, true, false, false));
            root.setBackground(new Background(bg));
        } else {
            root.setStyle("-fx-background-color: #050508;");
        }

        VBox formContainer = new VBox(20);
        formContainer.setAlignment(Pos.CENTER);
        formContainer.setMaxSize(500, 400); 
        formContainer.setPadding(new Insets(30));
        UIFactory.applyPanelStyle(formContainer);

        Label title = UIFactory.createCustomLabel("ENTER CLAN LEADERS", 28, "#C9A84C", true);
        VBox.setMargin(title, new Insets(0, 0, 10, 0));
        formContainer.getChildren().add(title);

        int numPlayers = controller.getNumberOfPlayers();
        List<TextField> nameFields = new ArrayList<>();

        for (int i = 1; i <= numPlayers; i++) {
            TextField pField = UIFactory.createInputField("Player " + i);
            nameFields.add(pField);
            formContainer.getChildren().add(pField);
        }

        Button startButton = UIFactory.create3DButton("⚔ COMMENCE BATTLE ⚔", 18, -1, "10 30 10 30", "#8B0000", "#330000", "#eaddcf");
        
        startButton.setOnAction(e -> {
            List<String> names = new ArrayList<>();
            boolean allNamesValid = true;

            for (int i = 0; i < nameFields.size(); i++) {
                String inputName = nameFields.get(i).getText().trim();

                if (inputName.isEmpty()) {
                    inputName = nameFields.get(i).getPromptText();
                }

                if (inputName.length() > 14) {
                    allNamesValid = false;

                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Invalid Name");
                    alert.setHeaderText("Name Too Long");
                    alert.setContentText("Names must be 14 characters or less. (Player " + (i + 1) + ")");
                    alert.showAndWait();
                    break; 
                }

                names.add(inputName);
            }

            if (allNamesValid) {
                controller.startNewGame(names);
                controller.startNextRound(); 
                sceneManager.showGameArena();
            }
        });

        VBox.setMargin(startButton, new Insets(15, 0, 0, 0));
        formContainer.getChildren().add(startButton);
        
        Button backButton = UIFactory.createFlatButton("⬅ BACK TO MENU", 16, "#C9A84C", false);
        backButton.setOnAction(e -> sceneManager.showStartMenu()); 
        
        StackPane.setAlignment(backButton, Pos.TOP_LEFT);
        StackPane.setMargin(backButton, new Insets(20, 0, 0, 25));
        
        root.getChildren().addAll(formContainer, backButton);
        return new Scene(root);
    }
}