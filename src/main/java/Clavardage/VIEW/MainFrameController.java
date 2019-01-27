package Clavardage.VIEW;

import Clavardage.MODEL.Configuration;
import Clavardage.MODEL.UserDataHandler;
import Clavardage.NETWORK.Multicast;
import Clavardage.NETWORK.TCPClient;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Callback;

import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class MainFrameController implements Initializable {

    protected Boolean pseudoSet = false;

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
    protected ListView<UserDataHandler> connectedUserList;

    @Override
    private void PopulateListViewData() {

    }

    @FXML
    protected Button logOff;

    @FXML
    protected ComboBox<String> userIDStatusBox;

    private ObservableList<String> comboBoxData = FXCollections.observableArrayList();

    /**
     * Choix du status via la combobox et mise à jour sur le multicast
     */
    @FXML
    private void HandleComboBoxAction() {
        String selectedStatus = userIDStatusBox.getSelectionModel().getSelectedItem();
        Configuration.USER_ONLINESTATUS = selectedStatus;
        ArrayList<UserDataHandler> updateStatusPacket = Multicast.HelloMulticastPacket();
        Multicast.SendUDPPacket(updateStatusPacket);
    }

    @FXML
    protected Button setUserIDPseudo;

    /**
     * Action lié au bouton de changement de Pseudo
     *
     * @param event
     *
     */
    @FXML
    protected void SetPseudo(ActionEvent event){
        while (pseudoSet = false) {
            TextInputDialog dialog = new TextInputDialog("");
            dialog.setTitle(Configuration.APPLICATION_NAME + " - Set Pseudo");
            dialog.setContentText("Enter your Pseudo: ");

            Optional<String> result = dialog.showAndWait();
            if (result.isPresent()) {
                Configuration.USER_PSEUDO = result.get();
                if (!Configuration.ONLINE_USER_LIST.contains(Configuration.USER_PSEUDO)){
                    ArrayList<UserDataHandler> updatePseudoPacket = Multicast.HelloMulticastPacket();
                    Multicast.SendUDPPacket(updatePseudoPacket);
                } else {
                    Alert erreurPseudoPris = new Alert(Alert.AlertType.ERROR);
                    erreurPseudoPris.setTitle(Configuration.APPLICATION_NAME + " - Erreur Pseudo Existant");
                    erreurPseudoPris.setContentText("Ce Pseudo est déjà pris, veuillez en choisir un autre !");
                    erreurPseudoPris.setHeaderText(null);
                    erreurPseudoPris.showAndWait();
                }
            } else {
                Alert erreurPseudoVide = new Alert(Alert.AlertType.ERROR);
                erreurPseudoVide.setTitle(Configuration.APPLICATION_NAME + " - Erreur Pseudo");
                erreurPseudoVide.setContentText("Veuillez renseigner un Pseudo !");
                erreurPseudoVide.setHeaderText(null);
                erreurPseudoVide.showAndWait();
            }
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

    public void ClickUserList(){
    }

    public void ClickRefresh(){
    }

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

    public void HandleUserUpload(){

    }

    public void HandleUserDownload(){

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        userIDPseudoLabel.setText(Configuration.USER_PSEUDO);
        userIDNameLabel.setText(Configuration.USER_FULL_NAME);

        comboBoxData.add("ONLINE");
        comboBoxData.add("BUSY");
        comboBoxData.add("AFK");
        userIDStatusBox.setItems(comboBoxData);

        ObservableList<UserDataHandler> myObservableList = FXCollections.observableList(Configuration.ONLINE_USER_LIST);
        connectedUserList.setItems(myObservableList);
        connectedUserList.setCellFactory(new Callback<ListView<UserDataHandler>, ListCell<UserDataHandler>>() {
            @Override
            public ListCell<UserDataHandler> call(ListView<UserDataHandler> param) {
                ListCell<UserDataHandler> cell = new ListCell<UserDataHandler>(){
                    @Override
                    protected void updateItem(UserDataHandler user, boolean bln){
                        super.updateItem(user, bln);
                        if (user != null){
                            setText(user.getUserPseudo() + " - " + user.getUserOnlineStatus());
                        }
                    }
                };
                return cell;
            }
        });
    }
}
