package game.gui;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class ProphecySelector extends HBox {
    private int selectedValue = 0;
    private int maxValue = 0;
    private Label valueLabel;
    private static final String STEPPER_NORMAL = "-fx-background-color: linear-gradient(to bottom, #5d4037, #3e2723);" + "-fx-min-width: 38; -fx-min-height: 38;" + "-fx-max-width: 38; -fx-max-height: 38;" + "-fx-background-radius: 6;" + "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.5), 4, 0, 0, 2);" + "-fx-cursor: hand;" + "-fx-text-fill: gold";
    private static final String STEPPER_HOVER = "-fx-background-color: linear-gradient(to bottom, #7d5547, #5e3723);" + "-fx-min-width: 38; -fx-min-height: 38;" + "-fx-max-width: 38; -fx-max-height: 38;" + "-fx-background-radius: 6;" + "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.5), 4, 0, 0, 2);" + "-fx-cursor: hand;" + "-fx-text-fill: gold";

    public ProphecySelector() {
        super(10);
        this.setAlignment(Pos.CENTER);
        this.setStyle("-fx-background-color: linear-gradient(to bottom, rgba(30,15,10,0.85), rgba(15,8,5,0.95));" + "-fx-padding: 6 12 6 12;" + "-fx-background-radius: 8;" + "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.6), 8, 0, 0, 2);");

        Button minusBtn = createStepperButton("-");
        minusBtn.setOnAction(e -> decrement());

        valueLabel = new Label("0");
        valueLabel.setTextFill(Color.web("#f1c40f"));
        valueLabel.setFont(Font.font("System", FontWeight.BOLD, 22));
        valueLabel.setPrefWidth(56);
        valueLabel.setMinHeight(38);
        valueLabel.setAlignment(Pos.CENTER);
        valueLabel.setStyle("-fx-background-color: #1a0e08;" + "-fx-background-radius: 6;");

        Button plusBtn = createStepperButton("+");
        plusBtn.setOnAction(e -> increment());
        this.getChildren().addAll(minusBtn, valueLabel, plusBtn);
    }

    private Button createStepperButton(String text) {
        Button btn = new Button(text);
        btn.setStyle(STEPPER_NORMAL);
        btn.setOnMouseEntered(e -> btn.setStyle(STEPPER_HOVER));
        btn.setOnMouseExited(e -> btn.setStyle(STEPPER_NORMAL));
        return btn;
    }

    private void decrement() {
        if (selectedValue > 0) {
            selectedValue--;
            valueLabel.setText(String.valueOf(selectedValue));
        }
    }

    private void increment() {
        if (selectedValue < maxValue) {
            selectedValue++;
            valueLabel.setText(String.valueOf(selectedValue));
        }
    }

    public void configureForRound(int maxAllowed) {
        this.maxValue = maxAllowed;
        this.selectedValue = 0;
        this.valueLabel.setText("0");
    }

    public int getValue() {
        return selectedValue;
    }
}