package Clavardage.PopUpWindows;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AlertBox {
    public static void display(String title, String message){
        Stage alertWindow = new Stage();

        alertWindow.initModality(Modality.APPLICATION_MODAL);
        alertWindow.setTitle(title);
        alertWindow.setMinWidth(250);

        Label alertLabel = new Label();
        alertLabel.setText(message);
        Button closeAlertButton = new Button("Close the Alert Window");
        closeAlertButton.setOnAction(e -> alertWindow.close());

        VBox layout = new VBox(10);
        layout.getChildren().addAll(alertLabel, closeAlertButton);
        layout.setAlignment(Pos.CENTER);

        Scene alertScene = new Scene(layout);
        alertWindow.setScene(alertScene);
        alertWindow.showAndWait();
    }

}
