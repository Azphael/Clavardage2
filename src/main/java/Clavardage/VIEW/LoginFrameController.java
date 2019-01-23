package Clavardage.VIEW;

import Clavardage.CONTROL.Configuration;
import Clavardage.ClavardageApp;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

import java.awt.event.ActionEvent;
import java.io.IOException;

public class LoginFrameController {
    ClavardageApp ClavApp = ClavardageApp.GetInstance();

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
     *
     * @param event
     *
     */
    public void Login(ActionEvent event){
        Configuration.USER_ID = loginUser.getText();
        Configuration.USER_PWD = passwordUser.getText();
        if (Configuration.USER_ID.equals("orocher") && Configuration.USER_PWD.equals("mdp")){
            System.out.println("Bienvenue " + Configuration.USER_ID);
        } else {
            Alert erreurLogin = new Alert(Alert.AlertType.ERROR);
            erreurLogin.setTitle("ClavApp - Erreur");
            erreurLogin.setContentText("Login ou Mot de Passe incorrects !");
            erreurLogin.setHeaderText(null);
            erreurLogin.showAndWait();
        }

    }




}
