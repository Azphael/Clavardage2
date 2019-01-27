package Clavardage.PopUpWindows;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class ConfirmBox {

    static boolean answer;

    @FXML
    protected static Label confirmationMessage;

    @FXML
    protected static Button confirmationButton;

    @FXML
    protected static Button cancelButton;


    public static boolean display(String title, String message) throws IOException {
        Stage confirmWindow = FXMLLoader.load(ConfirmBox.class.getResource("/ConfirmFrame.xml"));
        confirmWindow.initModality(Modality.APPLICATION_MODAL);
        confirmationMessage.setText(message);

        confirmationButton.setOnAction(event -> {
            answer = true;
            confirmWindow.close();
        });

        cancelButton.setOnAction(event -> {
            answer = false;
            confirmWindow.close();
        });

        // confirmWindow.setScene();
        return answer;
    }
}
