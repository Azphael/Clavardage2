package MODEL;

import CONTROL.PayloadHandler;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class TCPServerModel implements Runnable {

    private ServerSocket serverSocket;
    private Socket connection;
    private String chatRoomID;
    private PayloadHandler messageHandler;
    private String inputMsgType;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
    private File toFile;
    private File fromFile;
    private FileOutputStream fileOutputStream;
    private FileInputStream fileInputStream;

    private HashMap<String, TCPClientModel> activeChatRooms;

    /**
     * Ouverture du port Serveur
     * --> Mise en place du suivi des ChatRooms
     *
     * @param serverPort
     * @param serverBacklog
     *
     */
    public TCPServerModel(int serverPort, int serverBacklog) throws IOException {
        this.serverSocket = new ServerSocket(serverPort, serverBacklog);
        this.activeChatRooms = new HashMap<>();
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
                connection = serverSocket.accept();
                System.out.println("Connection établie !");
                chatRoomID = FirstContact(connection);
                TCPClientModel chatRoom = new TCPClientModel(connection, chatRoomID);
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
    public void ShutdownTCPServer(){
        try{
            for (TCPClientModel chats : this.activeChatRooms.values()){
                chats.ShutdownChatRoom();
            }
            this.inputStream.close();
            this.outputStream.close();
            this.serverSocket.close();
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
    public String FirstContact(Socket connectionExterieure) throws IOException, ClassNotFoundException {
        // Réception du message de premier contact de l'hôte distant
        ObjectInputStream firstInputStream = new ObjectInputStream(new BufferedInputStream(connectionExterieure.getInputStream()));
        PayloadHandler firstPayload = (PayloadHandler) firstInputStream.readObject();

        // Extraction du numéro unique de ChatRoom
        String newChatRoomID = firstPayload.getChatRoomID();

        return newChatRoomID;
    }
}
