package VIEW;

import MODEL.TCPClientModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Tab;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import javax.swing.text.html.ImageView;
import javax.swing.text.html.ListView;
import java.awt.*;
import java.net.URL;
import java.util.ResourceBundle;

public class MainFrameController implements Initializable {

    /**
     * Controller des éléments de la partie Gauche du MainFrame
     *
     */
    @FXML
    public ImageView avatarImage;

    @FXML
    public Label pseudoLabel;

    @FXML
    public Label nameLabel;

    @FXML
    public ListView connectedUserList;
    public ObservableList<String > connectedUserListView = FXCollections.observableArrayList();

    @FXML
    public Button logOff;

    @FXML
    public ComboBox statusBox;

    @FXML
    public Button setPseudo;

    @FXML
    public Button refreshConnectedUserList;

    @FXML
    public Button sendChatRequest;

    @FXML
    public void ClickUserList(){
    }

    @FXML
    public void ClickRefresh(){
    }

    @FXML


    /**
     * Controller des TabPanes de la partie Droite du MainFrame
     *
     */
    @FXML
    private Tab modeleChatRoomTab;

    @FXML
    private Label pseudoInterlocauteur;

    @FXML
    private TextArea chatWallofText;

    @FXML
    private Button exitChatRoom;

    @FXML
    private TextField userMessageBox;

    @FXML
    private Button uploadFile;

    @FXML
    private Button downloadFile;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    /**
     * Set Event Handlers


    // Envoi du message texte sur l'appui de la touche entrée
    userMessageBox.setOnKeyPressed(new EventHandler<KeyEvent>() {
        @Override
        public void handle(KeyEvent keyEvent) {
            if (keyEvent.getCode() == KeyCode.ENTER)  {
                TCPClientModel.SendPayload("TEXT",);
                sendFunction();
            }
        }
     */
}
