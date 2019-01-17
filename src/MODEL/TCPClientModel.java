package MODEL;

import CONTROL.Configuration;
import CONTROL.PayloadDataHandler;
import CONTROL.PayloadHandler;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.time.Instant;

public class TCPClientModel implements Runnable {

    private Socket connection;
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
    public TCPClientModel(Socket connection, String chatRoomNbr) throws IOException {
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
        if (messageType == "FIRST"){
            outputPayload.setMessageType("FIRST");
            System.out.println("Message de premier contact pour la ChatRoom Nbr " + chatRoomID);
            PayloadDataHandler.StringToPayload(content,outputPayload);  // Content --> Simple message de type "Premier Contact"
        }
        else if (messageType == "TEXT") {
            outputPayload.setMessageType("TEXT");
            System.out.println("Envoi d'un texte");
            PayloadDataHandler.StringToPayload(content,outputPayload);  // Content --> contenu du message tapé
        }
        else if (messageType == "FILE") {
            outputPayload.setMessageType("FILE");
            System.out.println("Envoi d'un fichier");
            PayloadDataHandler.FileToPayload(content, outputPayload);   // Content --> nom du fichier et chemin d'accès source complet, soit Configuration.UPLOAD
        }
        else {
            System.out.println("Le type du message est mal précisé.");
        }
        try{
            if (!this.connection.isClosed()){
                outputPayload.setTimeStamp(Instant.now());
                outputStream.writeObject(outputPayload);
                System.out.println("Message envoyé.");
            }
            else{
                System.out.println("La connection est fermée !");
            }
        }catch(IOException ioException){
            System.out.println("Erreur lors de l'envoi du message !");
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
            System.out.print("Réception du message de premier contact ...");
        } else if (inputPayload.getMessageType() == "TEXT") {
            inputMessage = PayloadDataHandler.PayloadToString(inputPayload);
        } else if (inputPayload.getMessageType() == "FILE") {
            PayloadDataHandler.PayloadToFile(Configuration.DOWNLOAD, inputPayload);
            inputMessage = "Le fichier " + Configuration.FILE_TO_DOWNLOAD + " a été récupéré!";
        } else {
            System.out.println("Le type du message est mal indiqué.");
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
            System.out.print("Connection avec le serveur perdue !");
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
            this.outputStream.close();
            this.inputStream.close();
            this.connection.close();
        }catch(IOException ioException){
            System.out.println("Cette ChatRoom est déjà déconnectée !");
        }
    }
}
