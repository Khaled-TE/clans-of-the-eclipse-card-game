package game.gui;                  
import game.units.Unit;            
import game.units.ClanUnit;         
import game.model.PlayedUnit;         
import game.enums.DeclaredIdentity;       
import game.utils.HelperMethods;
import game.utils.ImageCache;
import javafx.geometry.Insets;
import javafx.geometry.Pos;                        
import javafx.scene.layout.StackPane;               
import javafx.scene.image.ImageView;                   
import javafx.scene.text.Text;                           
import javafx.scene.text.Font;                 
import javafx.scene.text.FontWeight;
import javafx.scene.paint.Color;
import javafx.scene.Cursor;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.InnerShadow;

public class CardNode extends StackPane {
    
    private Unit unit; 

    public CardNode(Unit unit){
        this.unit = unit;
        buildVisuals(HelperMethods.getTextureName(unit), powerLabelFor(unit));
    }

    public CardNode(DeclaredIdentity identity){
        this.unit = null;
        buildVisuals(HelperMethods.getTextureName(identity), "★");
    }

    public CardNode(PlayedUnit pu){
        DeclaredIdentity id = pu.getIdentity();
        if (id != null) {
            this.unit = null;
            buildVisuals(HelperMethods.getTextureName(id), "★");
        } else {
            this.unit = pu.getUnit();
            buildVisuals(HelperMethods.getTextureName(this.unit), powerLabelFor(this.unit));
        }
    }

    private static String powerLabelFor(Unit unit) {
        if (unit instanceof ClanUnit) {
            int power = ((ClanUnit) unit).getPower();
            if (power == 14)
                return "♚";
            else
                return String.valueOf(power);
        }
        return String.valueOf(unit.getType());
    }

    public void buildVisuals(String textureName, String powerLabel) {

        this.setPrefSize(140, 200);
        this.setMaxSize(140, 200); 
        this.setStyle("-fx-background-radius: 15; -fx-background-color: transparent;");

        ImageView artworkView = new ImageView();
        artworkView.setImage(ImageCache.getImage(textureName));
        artworkView.setFitWidth(128);
        artworkView.setFitHeight(188);
        artworkView.setPreserveRatio(false);
        
        javafx.scene.shape.Rectangle clip = new javafx.scene.shape.Rectangle(128, 188);
        clip.setArcWidth(10);
        clip.setArcHeight(10);
        artworkView.setClip(clip);

        ImageView standardframeView = new ImageView(ImageCache.getImage("/cards/card_frame_standard"));
        standardframeView.setFitWidth(140);
        standardframeView.setFitHeight(200);
        ImageView especialframeView = new ImageView(ImageCache.getImage("/cards/card_frame_especial"));
        especialframeView.setFitWidth(140);
        especialframeView.setFitHeight(200);

        Text powerText = new Text(powerLabel);
        powerText.setFill(Color.web("#eaddcf")); 
        powerText.setEffect(new InnerShadow(3, Color.BLACK));
        
        StackPane powerTextWrapper = new StackPane(powerText);
        if (powerLabel.length() < 3) {
            powerText.setFont(Font.font("Arial", FontWeight.BOLD, 16));
            powerText.setEffect(new InnerShadow(3, Color.BLACK));

            powerTextWrapper.setPrefSize(40, 40);
            powerTextWrapper.setMaxSize(40, 40);
            powerTextWrapper.setAlignment(Pos.CENTER);

            this.getChildren().addAll(artworkView, standardframeView, powerTextWrapper);
            StackPane.setAlignment(powerTextWrapper, Pos.TOP_LEFT);
            StackPane.setMargin(powerTextWrapper, new Insets(4, 0, 0, 6));
        } else {
            powerText.setFont(Font.font("Arial", FontWeight.BOLD, 17));
            powerText.setEffect(new DropShadow(2, Color.BLACK));
            
            int safeThreshold = 7;
            if (powerLabel.length() > safeThreshold) {
                double compressionFactor = (double) safeThreshold / powerLabel.length();
                
                powerText.setScaleX(Math.max(0.65, compressionFactor));
            }
            
            powerTextWrapper.setPrefSize(110, 20);
            powerTextWrapper.setMaxSize(110, 20);
            powerTextWrapper.setAlignment(Pos.CENTER);

            this.getChildren().addAll(artworkView, especialframeView, powerTextWrapper);
            StackPane.setAlignment(powerTextWrapper, Pos.BOTTOM_CENTER);
            StackPane.setMargin(powerTextWrapper, new Insets(0, 0, 25, 0));
        }
        StackPane.setMargin(artworkView, new Insets(4, 0, 0, 0));
        setupInteractions();
    }
    
    private void setupInteractions() {
        this.setCursor(Cursor.HAND);
        
        // Hover In
        this.setOnMouseEntered(e -> {
            this.setScaleX(1.15);
            this.setScaleY(1.15);
            this.setTranslateY(-15);
            this.setViewOrder(-1.0);
            this.setEffect(new DropShadow(BlurType.THREE_PASS_BOX, Color.rgb(212, 175, 55, 0.8), 20, 0.5, 0, 0));
        });
        
        // Hover Out
        this.setOnMouseExited(e -> {
            this.setScaleX(1.0);
            this.setScaleY(1.0);
            this.setTranslateY(0);
            this.setEffect(new DropShadow(BlurType.THREE_PASS_BOX, Color.rgb(0, 0, 0, 0.5), 10, 0, 5, 5));
        });
    }
    
    public void setTableMode(boolean isTableMode) {
        if (isTableMode) {
            this.setRotationAxis(javafx.scene.transform.Rotate.X_AXIS);
            this.setRotate(50); 
            this.setScaleX(0.9);
            this.setScaleY(0.9);
            
            this.setOnMouseEntered(null);
            this.setOnMouseExited(null);
            this.setCursor(Cursor.DEFAULT);
            this.setEffect(new DropShadow(BlurType.THREE_PASS_BOX, Color.rgb(0, 0, 0, 0.8), 15, 0, 10, 10));
        } else {
            this.setRotate(0);
            this.setScaleX(1.0);
            this.setScaleY(1.0);
            setupInteractions(); 
        }
    }

    public Unit getUnit() {
        return this.unit;
    }
}