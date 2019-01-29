package Clavardage.VIEW;

import Clavardage.MODEL.Configuration;
import Clavardage.NETWORK.Multicast;
import Clavardage.NETWORK.TCPServer;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class LoginFrameController implements Initializable {

    protected Multicast serviceMulticast;
    protected TCPServer serviceTCPServer;


    public LoginFrameController(){
    }

    @FXML
    protected ImageView companyLogo;

    @FXML
    protected Label companyName;

    @FXML
    protected TextField loginUser;

    @FXML
    protected TextField passwordUser;

    /**
     * En validant le mot de passe par la touche "ENTER", lance la procédure de login
     *
     * @param event
     *
     */
    public void LoginAction(KeyEvent event) throws IOException {
        if (event.getCode().equals(KeyCode.ENTER)) {
            Configuration.USER_ID = loginUser.getText();
            Configuration.USER_PWD = passwordUser.getText();
            if (Configuration.USER_ID.equals("toto") && Configuration.USER_PWD.equals("mdp")) { //paramètre d'un premier utilisateur provisoire en attendant la mise en place de la BDD d'authentification
                System.out.println("Bienvenue " + Configuration.USER_ID);
                // Mise à jour des paramètres identifiant l'utilisateur connecté : en dur en attendant la mise en place de la BDD d'authentification
                Configuration.USER_UNIQUE_ID = "MAT20170101anc20180101";
                Configuration.USER_LASTNAME = "EL MAGNIFICO";
                Configuration.USER_NAME = "Toto";
                MainClavAppLaunch(event);

            }
            else if (Configuration.USER_ID.equals("tata") && Configuration.USER_PWD.equals("mdp")) { //paramètre d'un deuxième utilisateur provisoire en attendant la mise en place de la BDD d'authentification
                System.out.println("Bienvenue " + Configuration.USER_ID);
                // Mise à jour des paramètres identifiant l'utilisateur connecté : en dur en attendant la mise en place de la BDD d'authentification
                Configuration.USER_UNIQUE_ID = "MAT20170701anc20180701";
                Configuration.USER_LASTNAME = "LA ISLA";
                Configuration.USER_NAME = "Bonita";
                MainClavAppLaunch(event);
            }
            else {
                Alert erreurLogin = new Alert(Alert.AlertType.ERROR);
                erreurLogin.setTitle(Configuration.APPLICATION_NAME + " - Message d'Erreur");
                erreurLogin.setContentText("Login ou Mot de Passe incorrects !");
                erreurLogin.setHeaderText(null);
                erreurLogin.showAndWait();
            }
        }
    }

    /**
     * Méthode de lancement des instances uniques de Multicast, de TCPServer, et ouverture de la fenêtre principale de la ClavApp
     *
     * @param ke
     * @throws IOException
     *
     */
    public void MainClavAppLaunch(KeyEvent ke) throws IOException {
        // Lancement du thread unique Multicast
        serviceMulticast = Multicast.getInstance();

        // Lancement du thread unique TCPServer
        serviceTCPServer = TCPServer.getInstance();

        // Lancement de la fenêtre principale de chat
        Parent mainFrame_parent = FXMLLoader.load(getClass().getResource("/MainFrame.fxml"));
        Scene mainFrame_scene = new Scene(mainFrame_parent);
        Stage clavApp_stage = (Stage) ((Node) ke.getSource()).getScene().getWindow();
        clavApp_stage.setTitle(Configuration.APPLICATION_NAME);
        clavApp_stage.setScene(mainFrame_scene);
        clavApp_stage.show();
    }


    /**
     * Initialisation des paramètres de la fenêtre de login
     *
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        companyName.setText(Configuration.COMPANY_NAME);
        // Image companyLogoImage = new Image(Configuration.COMPANY_LOGO.toURI().toString());
        // companyLogo.setImage(companyLogoImage);
    }
}
