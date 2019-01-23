package Clavardage;

import Clavardage.CONTROL.Configuration;
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
        Parent root  = FXMLLoader.load(getClass().getResource("/LoginFrame.xml"));
        primaryStage.setTitle("Clavarnage 0.1");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
        primaryStage.setOnCloseRequest(event -> {
            event.consume();
            closeProgram();
        });
    }

    @Override
    public void stop() throws Exception {
        ClavardageApp.GetInstance().LogOut();
    }

    private void closeProgram(){

    }

    /**
     * Creation du Process ID
     */


    /**
     * Ouverture de la session
     */
    public void LogOn(){

    }
    /**
     * Fermeture de la session
     */
    public void LogOff(){
    }

    /**
     * Réception d'un message
     */
    public void ReceptionMessage() {

    }

    /**
     * Envoi d'un message
     */
    public void EnvoiMessage(){

    }

}
