package Clavardage.NETWORK;

import Clavardage.MODEL.Configuration;
import Clavardage.MODEL.PayloadDataHandler;
import Clavardage.MODEL.PayloadHandler;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.time.Instant;

public class TCPClient implements Runnable {

    private static Socket connection;
    private static String chatRoomID;
    private ObjectInputStream inputStream;
    private static ObjectOutputStream outputStream;
    private String sourceFile = Configuration.FILE_TO_UPLOAD;
    private String sourceFilePath = Configuration.PATH_TO_UPLOAD;
    private String textToPrint = null;

    /**
     * Discussion dans une ChatRoom donnée
     *
     * @param connection
     * @param chatRoomNbr
     * @throws IOException
     *
     */
    public TCPClient(Socket connection, String chatRoomNbr) throws IOException {
        this.connection = connection;
        this.chatRoomID = chatRoomNbr;
        this.outputStream = new ObjectOutputStream(connection.getOutputStream());
        this.inputStream = new ObjectInputStream(new BufferedInputStream(connection.getInputStream()));
        SendPayload("FIRST","Premier Contact");
    }

    /**
     * Préparation et envoi des données
     *
     * @param messageType
     * @param content
     *
     */
    public static void SendPayload(String messageType, String content) {
        PayloadHandler outputPayload = new PayloadHandler();
        outputPayload.setChatRoomID(chatRoomID);
        outputPayload.setUserSourceID(Configuration.USER_UNIQUE_ID);
        if (messageType == "FIRST"){
            outputPayload.setMessageType("FIRST");
            System.out.println("TCP CLIENT===C Message de premier contact pour la ChatRoom Nbr " + chatRoomID);
            PayloadDataHandler.StringToPayload(content,outputPayload);  // Content --> Simple message de type "Premier Contact"
        }
        else if (messageType == "TEXT") {
            outputPayload.setMessageType("TEXT");
            System.out.println("Envoi d'un texte");
            PayloadDataHandler.StringToPayload(content,outputPayload);  // Content --> contenu du message tapé
        }
        else if (messageType == "FILE") {
            outputPayload.setMessageType("FILE");
            System.out.println("TCP CLIENT===C Envoi d'un fichier");
            PayloadDataHandler.FileToPayload(content, outputPayload);   // Content --> nom du fichier et chemin d'accès source complet, soit Configuration.UPLOAD
        }
        else {
            System.out.println("TCP CLIENT===C Le type du message est mal précisé.");
        }
        try{
            if (!connection.isClosed()){
                outputPayload.setTimeStamp(Instant.now());
                outputStream.writeObject(outputPayload);
                System.out.println("TCP CLIENT===C Message envoyé.");
            }
            else{
                System.out.println("TCP CLIENT===C La connection est fermée !");
            }
        }catch(IOException ioException){
            System.out.println("TCP CLIENT===C Erreur lors de l'envoi du message !");
        }
    }


    /**
     * Traitement des données réceptionnées
     *
     * @param payload
     * @return
     *
     */
    private String GetInputPayload(PayloadHandler payload){
        String inputMessage = null;
        PayloadHandler inputPayload = payload;
        if (inputPayload.getMessageType() == "FIRST") {
            System.out.print("TCP CLIENT===C Réception du message de premier contact ...");
        } else if (inputPayload.getMessageType() == "TEXT") {
            inputMessage = PayloadDataHandler.PayloadToString(inputPayload);
        } else if (inputPayload.getMessageType() == "FILE") {
            PayloadDataHandler.PayloadToFile(Configuration.DOWNLOAD, inputPayload);
            inputMessage = "TCP CLIENT===C Le fichier " + Configuration.FILE_TO_DOWNLOAD + " a été récupéré!";
        } else {
            System.out.println("TCP CLIENT===C Le type du message est mal indiqué.");
        }
        return inputMessage;
    }

    /**
     * La ChatRoom est en attente de l'arrivée de nouveaux messages entrants
     *
     */
    public void run(){
        try{
            while(!this.connection.isClosed()){
                PayloadHandler inputPayload = (PayloadHandler) inputStream.readObject();
                textToPrint = GetInputPayload(inputPayload);
            }
        }catch(IOException ioException){
            System.out.print("TCP CLIENT===C Connection avec le serveur perdue !");
        }catch(ClassNotFoundException classNotFoundException){
            classNotFoundException.printStackTrace();
        }
    }

    /**
     * Fermeture d'une ChatRoom donnée
     *
     */
    public void ShutdownChatRoom(){
        try{
            if (!(this.outputStream == null)) {
                this.outputStream.close();
            }
            if (!(this.inputStream == null)) {
                this.inputStream.close();
            }
            this.connection.close();
        }catch(IOException ioException){
            System.out.println("TCP CLIENT===C Cette ChatRoom est déjà déconnectée !");
        }
    }
}
