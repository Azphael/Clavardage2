package Clavardage.NETWORK;

import Clavardage.MODEL.PayloadHandler;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class TCPServer implements Runnable {

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
            while(!this.serverSocket.isClosed()) {
                System.out.println("Serveur en attente de connection ...");
                connection = this.serverSocket.accept();
                System.out.println("Connection établie !");
                chatRoomID = FirstContact(connection);
                TCPClient chatRoom = new TCPClient(connection, chatRoomID);
                activeChatRooms.put(chatRoomID, chatRoom);
                new Thread(chatRoom).start();
            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
            System.out.println("Erreur de connection au socket TCP");
        }catch (ClassNotFoundException classNotFoundException){
            classNotFoundException.printStackTrace();
        }
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
            inputStream.close();
            outputStream.close();
            serverSocket.close();
            System.out.println("Le serveur TCP vient de se fermer.");
        }catch(IOException ioException){
            System.out.println("Le serveur TCP " + serverSocket + " est déjà déconnecté !");
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
