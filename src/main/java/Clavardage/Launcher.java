package Clavardage;

import Clavardage.MODEL.Configuration;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Launcher extends Application {

    public static void main(String[] args){
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = null;

        // Ouverture du Frame de login
        FXMLLoader upFrontGUI  = new FXMLLoader(getClass().getResource("/LoginFrame.fxml"));
        root = upFrontGUI.load();
        primaryStage.setTitle(Configuration.APPLICATION_NAME);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
        primaryStage.setOnCloseRequest(event -> {
            event.consume();
            closeProgram();
        });
        ClavAppController.GetInstance().SetStage(primaryStage);
    }

    @Override
    public void stop() throws Exception {
        ClavAppController.GetInstance().LogOut();
    }

    private void closeProgram(){

    }
}
