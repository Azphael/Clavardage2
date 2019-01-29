package Clavardage;

import Clavardage.MODEL.Configuration;
import Clavardage.NETWORK.Multicast;
import Clavardage.NETWORK.TCPServer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

public class Launcher extends Application {

    protected Stage loginWindow;

    public static void main(String[] args){
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        loginWindow = primaryStage;
       // Ouverture du Frame de login
        FXMLLoader loginFrame  = new FXMLLoader(getClass().getResource("/LoginFrame.fxml"));
        Parent root = loginFrame.load();
        loginWindow.setTitle(Configuration.APPLICATION_NAME);
        loginWindow.setScene(new Scene(root));
        loginWindow.show();

        // Fermeture par clic sur la croix de la fenêtre
        loginWindow.setOnCloseRequest(event -> {
            event.consume();
            closeProgram();
        });
    }

    @Override
    public void stop() {
        System.out.println("Fin de la ClavApp.\nBonne nuit les petits ...");
        if (!(TCPServer.uniqueTCPServerInstance == null)) {
            TCPServer.ShutdownTCPServer();
        }
        if (!(Multicast.uniqueMulticastInstance == null)) {
            Multicast.ShutdownMulticast();
        }
        loginWindow.close();
    }

    private void closeProgram() {
        Alert confirmationCloseApp = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationCloseApp.setTitle(Configuration.APPLICATION_NAME + " - Confirmation de Fermeture");
        confirmationCloseApp.setHeaderText("Etes-vous sur(e) de vouloir quitter l'application ?");
        confirmationCloseApp.setContentText("");
        Optional<ButtonType> result = confirmationCloseApp.showAndWait();
        if (result.get() == ButtonType.OK){
            stop();
        } else {
            System.out.println("Annulation de la demande de fin de programme.");
        }
    }
}
