package Clavardage.VIEW;

import Clavardage.CONTROL.Configuration;
import Clavardage.MODEL.TCPClientModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.awt.event.ActionEvent;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class MainFrameController implements Initializable {

    public MainFrameController() {
        super();
    }

    /**
     * Controller des éléments de la partie Gauche du MainFrame
     *
     */
    @FXML
    protected ImageView userIDAvatarImage;

    @FXML
    protected Label userIDPseudoLabel;

    @FXML
    protected Label userIDNameLabel;

    @FXML
    protected ListView connectedUserList;

    @FXML
    protected Button logOff;

    @FXML
    protected ComboBox userIDStatusBox;

    @FXML
    protected Button setUserIDPseudo;

    protected void SetPseudo(ActionEvent event){
        TextInputDialog dialog = new TextInputDialog("");
        dialog.setTitle("Clavarnage - Set Pseudo");
        dialog.setContentText("Enter your Pseudo: ");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            Configuration.USER_PSEUDO = result.get();
        }
    }

    @FXML
    protected Button refreshConnectedUserList;

    @FXML
    protected Button sendChatRequest;

    /**
     * Controller des TabPanes de la partie Droite du MainFrame
     *
     */
    @FXML
    protected Tab modeleChatRoomTab;

    @FXML
    protected Label pseudoInterlocuteur;

    @FXML
    protected TextArea chatWallofText;

    @FXML
    protected Button exitChatRoom;

    @FXML
    protected TextField userMessageBox;

    @FXML
    protected Button uploadFile;

    @FXML
    protected Button downloadFile;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public void ClickUserList(){
    }

    public void ClickRefresh(){
    }

    /**
     * Envoi le text du userMessageBox par appui sur la touche "Entrée"
     *
     * @param keyEvent
     *
     */
    public void HandleUserText(KeyEvent keyEvent){
        if ((userMessageBox.getText() != null && !userMessageBox.getText().isEmpty() && keyEvent.getCode() == KeyCode.ENTER)){
            String userMessage = userMessageBox.getText();
            TCPClientModel.SendPayload("Text", userMessage);
            userMessageBox.setText("");
        }
    }

    public void HandleUserUpload(){

    }

    public void HandleUserDownload(){

    }
}
