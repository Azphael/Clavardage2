package Clavardage.VIEW;

import Clavardage.NETWORK.TCPClient;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class ChatRoomTabFrameController {
    @FXML
    protected Label pseudoInterlocuteur;

    @FXML
    protected TextArea chatWallofText;

    @FXML
    protected Button exitChatRoom;

    @FXML
    protected void ExitChatRoom(){

    }

    @FXML
    protected TextField userMessageBox;

    /**
     * Envoi le texte du userMessageBox par appui sur la touche "Entrée"
     *
     * @param keyEvent
     *
     */
    @FXML
    public void HandleUserText(KeyEvent keyEvent){
        if ((userMessageBox.getText() != null && !userMessageBox.getText().isEmpty() && keyEvent.getCode() == KeyCode.ENTER)){
            String userMessage = userMessageBox.getText();
            TCPClient.SendPayload("Text", userMessage);
            userMessageBox.setText("");
        }
    }

    @FXML
    protected Button uploadFile;

    @FXML
    protected Button downloadFile;

    public void HandleUserUpload(){

    }

    public void HandleUserDownload(){

    }

}
