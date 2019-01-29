package Clavardage;

import Clavardage.MODEL.DatabaseHandler;
import Clavardage.MODEL.UserDataHandler;
import Clavardage.NETWORK.Multicast;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.stage.Stage;
import sun.plugin2.message.Message;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class ClavAppController {

    protected UserDataHandler self;
    protected Multicast clavAppMulticast;
    protected Stage currentStage;
    protected DatabaseHandler authorizedLoginDB;
    protected DatabaseHandler connectedUserList;
    protected DatabaseHandler chatArchiveDB;
    protected ObservableList<UserDataHandler> userList = FXCollections.observableArrayList();
    protected ObservableMap<InetAddress, ArrayList<Message>> messageLog = FXCollections.observableHashMap();

    private ClavAppController(){

    }

    private static ClavAppController INSTANCE = null;

    public static ClavAppController GetInstance(){
        if (INSTANCE == null){
            INSTANCE = new ClavAppController();
        }
        return INSTANCE;
    }

    public void LogOut(){
        Multicast.ShutdownMulticast();
    }

    protected void SetStage(Stage s){
        this.currentStage = s;
    }

}
