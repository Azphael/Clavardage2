package Clavardage.VIEW;

import Clavardage.MODEL.Configuration;
import Clavardage.MODEL.UserDataHandler;
import Clavardage.NETWORK.Multicast;
import Clavardage.NETWORK.TCPClient;
import Clavardage.NETWORK.TCPServer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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
import javafx.util.Callback;

import java.io.IOException;
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
     */
    @FXML
    protected ImageView userIDAvatarImage;

    @FXML
    protected Label userIDPseudoLabel;

    @FXML
    protected Label userIDNameLabel;

    @FXML
    public static ListView<UserDataHandler> connectedUserList = new ListView<>();

    @FXML
    protected static ObservableList<UserDataHandler> listViewDatas;

    /**
     * Méthode permettant de préparer la liste des personnes connectées à partir de l'élément UserList
     *
     * @param userList
     *
     */
    @FXML
    public static ListView<UserDataHandler> PrepareOnlineUserList(ArrayList<UserDataHandler> userList) {
        listViewDatas = FXCollections.observableArrayList();
        for (int i = 1; i < userList.size(); i++) {
            listViewDatas.add(userList.get(i));
        }
        connectedUserList.setItems(listViewDatas);
        return connectedUserList;
    }

    /**
     * Méthode permettant de mettre à jour le listView avec les dernièrs datas données en paramètre
     *
     * @param userList
     *
     */
    @FXML
    public static void UpdateOnlineUserListView(ArrayList<UserDataHandler> userList){
        PrepareOnlineUserList(userList).setCellFactory(param -> new ListCell<UserDataHandler>() {
            @Override
            protected void updateItem(UserDataHandler user, boolean empty) {
                super.updateItem(user, empty);
                if (empty || user == null || user.getUserPseudo().isEmpty() || user.getUserOnlineStatus().isEmpty()) {
                    setText(null);
                } else {
                    setText(user.getUserPseudo() + " - " + user.getUserOnlineStatus());
                }
            }
        });
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

    @FXML
    protected void SetPseudoOnAction(){
        pseudoSet = false;
        SetPseudo();
    }

    /**
     * Méthode de changement de Pseudo
     *
     */
    @FXML
    protected void SetPseudo(){
        while (pseudoSet == false) {
            TextInputDialog dialog = new TextInputDialog("");
            dialog.setTitle(Configuration.APPLICATION_NAME + " - Set Pseudo");
            dialog.setContentText("Enter your Pseudo: ");

            Optional<String> result = dialog.showAndWait();
            if (result.isPresent() && (!(result.equals("????")))) {
                Configuration.USER_PSEUDO = result.get();
                if (!Configuration.ONLINE_USER_LIST.contains(Configuration.USER_PSEUDO)){
                    ArrayList<UserDataHandler> updatePseudoPacket = Multicast.HelloMulticastPacket();
                    Multicast.SendUDPPacket(updatePseudoPacket);
                    userIDPseudoLabel.setText(Configuration.USER_PSEUDO);
                    pseudoSet = true;
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
                erreurPseudoVide.setContentText("Veuillez renseigner un Pseudo valide!");
                erreurPseudoVide.setHeaderText(null);
                erreurPseudoVide.showAndWait();
            }
        }
    }

    @FXML
    protected Button refreshConnectedUserList;

    @FXML
    protected void OnButtonActionUpdateListView(ActionEvent event){
        UpdateOnlineUserListView(Configuration.ONLINE_USER_LIST);
    }


    @FXML
    protected Button sendChatRequest;

    /**
     * Controller des TabPanes de la partie Droite du MainFrame
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
    protected void ExitChatRoom(){

    }

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

    public void PopulateOnlineUserListView(){

    }

    /**
     * Méthode de logout de la fenêtre de clavardage principale avec retour à la fenêtre de login
     *
     * @param event
     * @throws IOException
     *
     */
    @FXML
    public void LogOut(ActionEvent event) throws IOException {
        Alert confirmationCloseApp = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationCloseApp.setTitle(Configuration.APPLICATION_NAME + " - Confirmation de Fermeture");
        confirmationCloseApp.setHeaderText("Etes-vous sur(e) de vouloir retourner à l'écran de login ?");
        confirmationCloseApp.setContentText("");
        Optional<ButtonType> result = confirmationCloseApp.showAndWait();
        if (result.get() == ButtonType.OK){
            System.out.println("Fermeture de la session de Clavardage.\nFermeture des serveurs Multicast et TCP.\nRetour au Login.");
            // Fermeture de la session de Clavardage
            TCPServer.ShutdownTCPServer();
            Multicast.ShutdownMulticast();

            // Retour à la fenêtre de login
            Parent loginFrame_parent = FXMLLoader.load(MainFrameController.class.getResource("/LoginFrame.fxml"));
            Scene loginFrame_scene = new Scene(loginFrame_parent);
            Stage loginWindow = (Stage) ((Node) event.getSource()).getScene().getWindow();
            loginWindow.setTitle(Configuration.APPLICATION_NAME);
            loginWindow.setScene(loginFrame_scene);
            loginWindow.show();
        } else {
            System.out.println("Annulation de la demande de logout.");
        }
    }

    /**
     * Initialisation des paramètres de la fenêtre principale de clavardage
     *
     * @param location
     * @param resources
     *
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Multicast.getInstance().start();
        try {
            TCPServer.getInstance().start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        SetPseudo();
        userIDPseudoLabel.setText(Configuration.USER_PSEUDO);
        userIDNameLabel.setText(Configuration.USER_LASTNAME + " " + Configuration.USER_NAME);

        comboBoxData.add("ONLINE");
        comboBoxData.add("BUSY");
        comboBoxData.add("AFK");
        userIDStatusBox.setItems(comboBoxData);
        userIDStatusBox.setValue("ONLINE");

        UpdateOnlineUserListView(Configuration.ONLINE_USER_LIST);
    }
}
