package Clavardage.VIEW;

import Clavardage.MODEL.Configuration;
import Clavardage.MODEL.UserDataHandler;
import Clavardage.NETWORK.Multicast;
import Clavardage.NETWORK.TCPClient;
import Clavardage.NETWORK.TCPServer;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.*;

public class MainFrameController implements Initializable {

    protected Boolean pseudoSet = false;
    protected Multicast serviceMulticast;
    protected TCPServer serviceTCPServer;


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
    protected ListView<UserDataHandler> connectedUserList = new ListView<>();

    @FXML
    protected ObservableList<UserDataHandler> observableOnlineUserList;

    /**
     * Methode de mise à jour de la liste des personnes connectées
     *
     * @return
     *
     */
    @FXML
    public ListView UpdateOnlineUserListView(){
        connectedUserList.getItems().clear();
        observableOnlineUserList = FXCollections.observableArrayList(Configuration.ONLINE_USER_LIST);
        connectedUserList.setItems(observableOnlineUserList);
        connectedUserList.setCellFactory(new Callback<ListView<UserDataHandler>, ListCell<UserDataHandler>>() {
            @Override
            public ListCell<UserDataHandler> call(ListView<UserDataHandler> param) {
                ListCell<UserDataHandler> cell = new ListCell<UserDataHandler>(){
                    @Override
                    protected void updateItem(UserDataHandler user, boolean empty){
                        super.updateItem(user, empty);
                        if (user != null){
                            setText(user.toString());
                        }
                    }
                };
                return cell;
            }
        });
        // Ajout d'un listener pour récupérer les informations du user sélectionné
        connectedUserList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<UserDataHandler>() {
            @Override
            public void changed(ObservableValue<? extends UserDataHandler> observable, UserDataHandler oldValue, UserDataHandler newValue) {
                targetForChat = newValue;
            }
        });
        return connectedUserList;
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
        List<UserDataHandler> updateStatusPacket = Multicast.HelloMulticastPacket();
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
                    UpdateOnlineUserListView();
                    Configuration.USER_ONLINESTATUS = "ONLINE";
                    List<UserDataHandler> updatePseudoPacket = Multicast.HelloMulticastPacket();
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
        UpdateOnlineUserListView();
    }

    @FXML
    protected Button sendChatRequest;

    protected UserDataHandler targetForChat = new UserDataHandler();

    @FXML
    protected void OnButtonActionSendChatRequest(){
        String targetUserID = targetForChat.getUserUniqueID();
        String newChatRoomID = Configuration.USER_UNIQUE_ID + "-" + targetUserID;
        try {
            Socket connection = new Socket(targetForChat.getUserTCPServerIP(), targetForChat.getUserTCPServerPort());
            TCPClient chatRoom = new TCPClient(connection, newChatRoomID);
            TCPServer.activeChatRooms.put(newChatRoomID, chatRoom);
            addTab(newChatRoomID);
            new Thread(chatRoom).start();
            System.out.println("TCP CLIENT===C Création de la ChatRoom " + chatRoom);
        } catch (IOException ioExcexption){
            System.out.println("TCP CLIENT===C Erreur de connexion au TCPServer ciblé.");
        }
    }

    /**
     * Controller des TabPanes de la partie Droite du MainFrame
     */
    @FXML
    protected TabPane chatRoomTabs;

    @FXML
    protected FXMLLoader tabFXMLLoader = new FXMLLoader();

    @FXML
    protected Map<String, Object> chatRoomControllerMap = new HashMap<>();


    @FXML
    protected void addTab(String chatRoomID) {
        int numTabs = chatRoomTabs.getTabs().size();
        Tab tab = new Tab("ChatRoom "+(numTabs+1));
        chatRoomTabs.getTabs().add(tab);
        tab.setId(chatRoomID);
        chatRoomTabs.getSelectionModel().clearSelection();
        tabFXMLLoader.setController(new ChatRoomTabFrameController());

        //Rajout d'une écoute des modification dans la chatRoom
        chatRoomTabs.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tab>() {
            @Override
            public void changed(ObservableValue<? extends Tab> observable, Tab oldValue, Tab newValue) {
                System.out.println("Tab sélectionnée: " + newValue.getText());
                if (newValue.getContent() == null){
                    try{
                        //Chargement de la forme de la tab
                        Parent chatRoomFrame = (Parent) tabFXMLLoader.load(this.getClass().getResource("/ChatRoomTabFrame.fxml").openStream());
                        newValue.setContent(chatRoomFrame);
                        chatRoomControllerMap.put(newValue.getText(), tabFXMLLoader.getController());
                    } catch (IOException ioexc){
                        ioexc.printStackTrace();
                        System.out.println("Erreur de chargement de la forme de la chatRoom.");
                    }
                } else {
                    Parent chatRoomFrame = (Parent) newValue.getContent();
                    chatRoomControllerMap.get(newValue.getText());
                }
            }
        });
    }

    @FXML
    private void listTabs() {
        chatRoomTabs.getTabs().forEach(tab -> System.out.println(tab.getText()));
        System.out.println();
    }

    @FXML
    protected Tab emptyChatRoomTab;



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
        // Lancement du thread unique Multicast
        serviceMulticast = Multicast.getInstance();
        Multicast.getInstance().start();

        // Lancement du thread unique TCPServer
        try {
            serviceTCPServer = TCPServer.getInstance();
            TCPServer.getInstance().start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        SetPseudo();
        userIDPseudoLabel.setText(Configuration.USER_PSEUDO);
        userIDNameLabel.setText(Configuration.USER_LASTNAME + " " + Configuration.USER_NAME);

        // Mise en place du comboBox de status
        comboBoxData.add("ONLINE");
        comboBoxData.add("BUSY");
        comboBoxData.add("AFK");
        userIDStatusBox.setItems(comboBoxData);
        userIDStatusBox.setValue("ONLINE");

        // Mise en place de la liste des utilisaterus connectés
        UpdateOnlineUserListView();

    }
}
