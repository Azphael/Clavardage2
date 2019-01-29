package Clavardage.NETWORK;

import Clavardage.MODEL.Configuration;
import Clavardage.MODEL.PayloadHandler;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class TCPServer extends Thread {

    public static TCPServer uniqueTCPServerInstance;
    private static ServerSocket serverSocket;
    private static Socket connection;
    private static String chatRoomID;
    private static PayloadHandler messageHandler;
    private static String inputMsgType;
    private static ObjectInputStream inputStream;
    private static ObjectOutputStream outputStream;
    private static File toFile;
    private static File fromFile;
    private static FileOutputStream fileOutputStream;
    private static FileInputStream fileInputStream;

    private static HashMap<String, TCPClient> activeChatRooms;

    /**
     * Ouverture du port Serveur
     * --> Mise en place du suivi des ChatRooms
     *
     * @param serverPort
     * @param serverBacklog
     *
     */
    public TCPServer(int serverPort, int serverBacklog) throws IOException {
        serverSocket = new ServerSocket(serverPort, serverBacklog);
        activeChatRooms = new HashMap<>();
    }

    /**
     * Attente et Acceptation des connections extérieures
     * --> Création d'une ChatRoom à identifiant unique pour la discussion
     *
     */
    @Override
    public void run() {
        try {
            while(!serverSocket.isClosed()) {
                System.out.println("TCP SERVER===S Serveur en attente de connection ...");
                connection = serverSocket.accept();
                System.out.println("TCP SERVER===S Connection établie !");
                chatRoomID = FirstContact(connection);
                TCPClient chatRoom = new TCPClient(connection, chatRoomID);
                activeChatRooms.put(chatRoomID, chatRoom);
                new Thread(chatRoom).start();
            }
        } catch (IOException ioException) {
            System.out.println("TCP SERVER===S Erreur de connection au socket TCP");
        }catch (ClassNotFoundException classNotFoundException){
            classNotFoundException.printStackTrace();
        }
    }

    /**
     * Singleton Instance
     *
     * @return
     *
     */
    public static TCPServer getInstance() throws IOException {
        if (uniqueTCPServerInstance == null){
            uniqueTCPServerInstance = new TCPServer(Configuration.TCP_SERVER_PORT, Configuration.TCP_SERVER_BACKLOG);
        }
        return uniqueTCPServerInstance;
    }
    /**
     * Arrêt de toutes les ChatRooms et du Serveur
     *
     */
    public static void ShutdownTCPServer(){
        try{
            for (TCPClient chats : activeChatRooms.values()){
                chats.ShutdownChatRoom();
            }
            if (!(inputStream == null)) {
                inputStream.close();
            }
            if (!(outputStream == null)) {
                outputStream.close();
            }
            serverSocket.close();
            System.out.println("TCP SERVER===S Fermeture du serveur TCP.");
        }catch(IOException ioException){
            System.out.println("TCP SERVER===S Le serveur TCP " + serverSocket + " est déjà déconnecté !");
        }
    }

    /**
     * Lors du premier contact d'une nouvelle connection
     * --> Récupération de l'ID unique de la ChatRoom
     *
     * @param connectionExterieure
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     *
     */
    public static String FirstContact(Socket connectionExterieure) throws IOException, ClassNotFoundException {
        // Réception du message de premier contact de l'hôte distant
        ObjectInputStream firstInputStream = new ObjectInputStream(new BufferedInputStream(connectionExterieure.getInputStream()));
        PayloadHandler firstPayload = (PayloadHandler) firstInputStream.readObject();

        // Extraction du numéro unique de ChatRoom
        String newChatRoomID = firstPayload.getChatRoomID();

        return newChatRoomID;
    }
}
