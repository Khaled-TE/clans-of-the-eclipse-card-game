package game.utils;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Slider;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.layout.Region;

public class UIFactory {

    public static Button create3DButton(String text, int fontSize, double prefWidth, String padding, String topHex, String shadowHex, String textHex) {
        Button btn = new Button(text);
        if (prefWidth > 0) btn.setPrefWidth(prefWidth);
        
        btn.setStyle("-fx-font-family: 'Georgia'; -fx-font-weight: bold; -fx-font-size: " + fontSize + "px;");

        String normal = String.format(
                "-fx-background-color: linear-gradient(to bottom, %sDD, %s99); -fx-border-color: %s; -fx-border-width: 0 0 4 0; -fx-border-radius: 5; -fx-background-radius: 5; -fx-text-fill: %s; -fx-cursor: hand; -fx-padding: %s;", 
                topHex, topHex, shadowHex, textHex, padding);

        String hover = String.format(
                "-fx-background-color: linear-gradient(to bottom, %sFF, %sBB); -fx-border-color: %s; -fx-border-width: 0 0 4 0; -fx-border-radius: 5; -fx-background-radius: 5; -fx-text-fill: %s; -fx-cursor: hand; -fx-padding: %s; -fx-effect: dropshadow(gaussian, %s, 15, 0.5, 0, 0);", 
                topHex, topHex, shadowHex, textHex, padding, topHex);

        String pressed = String.format(
                "-fx-background-color: linear-gradient(to bottom, %s88, %sCC); -fx-border-color: %s; -fx-border-width: 0 0 1 0; -fx-border-radius: 5; -fx-background-radius: 5; -fx-text-fill: %s; -fx-cursor: hand; -fx-padding: %s;", 
                topHex, topHex, shadowHex, textHex, padding);

        btn.setStyle(normal);
        btn.setOnMouseEntered(e -> btn.setStyle(hover));
        btn.setOnMouseExited(e -> btn.setStyle(normal));
        btn.setOnMousePressed(e -> { btn.setStyle(pressed); btn.setTranslateY(3); });
        btn.setOnMouseReleased(e -> { btn.setStyle(hover); btn.setTranslateY(0); });
        
        return btn;
    }

    public static Button createFlatButton(String text, int fontSize, String hexColor, boolean hasBorder) {
        Button btn = new Button(text);
        String borderCss = hasBorder ? "-fx-border-color: " + hexColor + "; -fx-border-width: 2; -fx-border-radius: 5; -fx-padding: 8 20; " : "-fx-padding: 5; ";
        String baseStyle = "-fx-background-color: transparent; -fx-font-family: 'Georgia'; -fx-font-weight: bold; -fx-font-size: " + fontSize + "px; -fx-cursor: hand; " + borderCss;
        
        String normal = baseStyle + "-fx-text-fill: " + (hasBorder ? hexColor : "#A0A0B0") + ";";
        String hover = baseStyle + "-fx-text-fill: " + hexColor + "; " + (hasBorder ? "-fx-background-color: " + hexColor + "; -fx-text-fill: #0A0A0F;" : "-fx-effect: dropshadow(gaussian, " + hexColor + ", 10, 0.5, 0, 0);");

        btn.setStyle(normal);
        btn.setOnMouseEntered(e -> btn.setStyle(hover));
        btn.setOnMouseExited(e -> btn.setStyle(normal));
        return btn;
    }

    public static Label createCustomLabel(String text, int fontSize, String hexColor, boolean hasShadow) {
        Label label = new Label(text);
        label.setStyle("-fx-font-family: 'Georgia'; -fx-font-weight: bold; -fx-font-size: " + fontSize + "px; -fx-text-fill: " + hexColor + ";");
        if (hasShadow) {
            label.setEffect(new DropShadow(10, Color.BLACK));
        }
        return label;
    }

    public static TextField createInputField(String promptText) {
        TextField tf = new TextField();
        tf.setPromptText(promptText);
        String base = "-fx-background-color: rgba(0,0,0,0.6); -fx-text-fill: #eaddcf; -fx-prompt-text-fill: #888899; -fx-font-family: 'Georgia'; -fx-font-size: 16px; -fx-pref-width: 300px; -fx-padding: 10 12; -fx-border-radius: 4; -fx-background-radius: 4; ";
        tf.setStyle(base + "-fx-border-color: #444;");
        tf.focusedProperty().addListener((obs, oldVal, newVal) -> tf.setStyle(base + (newVal ? "-fx-border-color: #C9A84C;" : "-fx-border-color: #444;")));
        return tf;
    }

    public static void applyPanelStyle(Region region) {
        region.setStyle("-fx-background-color: rgba(15, 15, 20, 0.85); -fx-border-color: #6B4F10; -fx-border-width: 2; -fx-border-radius: 10; -fx-background-radius: 10; -fx-effect: dropshadow(gaussian, black, 20, 0.5, 0, 5);");
    }

    public static Slider createStyledSlider(double min, double max, double val) {
        Slider slider = new Slider(min, max, val);
        slider.setPrefWidth(300);
        slider.setStyle("-fx-control-inner-background: #1A1A2E; -fx-accent: #C9A84C;");
        return slider;
    }
}