package MODEL;

import CONTROL.Configuration;
import CONTROL.UserHandler;
import CONTROL.UserListHandler;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MulticastModel implements Runnable {

    private MulticastSocket multicastSocket;
    private DatagramSocket senderSocket;
    private InetAddress multicastGroupName;
    private byte[] receivedData = new byte[1024];
    private byte[] dataToSend = new byte[1024];
    private Timer autoSendTimer;
    private ArrayList<UserHandler>   inFromMulticast;

    /**
     * Lancement serveur Multicast
     * --> mise en attente de messages entrants
     * --> lancement du timer d'envoi automatique de message de présence
     *
     */
    public void run(){
        try{
            // Purge de la liste des connectés
            Configuration.ONLINE_USER_LIST.clear();

            // Récupéraition de l'IP routable
            Configuration.TCP_SERVER_IP = Configuration.MyRoutableIP();

            // Ouverture du port d'écoute du serveur multicast
            multicastSocket = new MulticastSocket(Configuration.MULTICAST_PORT);
            System.out.println("Serveur Multicast créé -> port " + multicastSocket.getPort());

            // Accès au groupe multicast donné
            multicastGroupName = InetAddress.getByName(Configuration.MULTICAST_IP);
            multicastSocket.joinGroup(multicastGroupName);
            System.out.println("Serveur Multicast : group \"" + multicastGroupName + "\" rejoint.");

            // Envoi du premier message de présence à fin de récupération de la liste des connectés
            ArrayList<UserHandler> fPacket = FirstPacket();
            SendUDPPacket(fPacket);

            // Lancement du timer d'envoi automatique d'un message de présence
            AutomaticHelloMultiCast();

            // Serveur Multicast en attente de messages entrants
            while(!this.multicastSocket.isClosed()) {
                // Réception d'un packet de données entrant
                DatagramPacket inputPacket = new DatagramPacket(receivedData, receivedData.length);
                try {
                    multicastSocket.receive(inputPacket);
                    System.out.println("Serveur Multicast - Packet Multicast reçu.");

                    // Conversion du DatagramPacket en données ArrayList<UserHandler>
                    byte[] toto = inputPacket.getData();
                    ByteArrayInputStream baois = new ByteArrayInputStream(toto);
                    ObjectInputStream ois = new ObjectInputStream(baois);
                    inFromMulticast = (ArrayList<UserHandler>) ois.readObject();
                    ois.close();

                    // Gestion des données reçues
                    GestionInfoMulticast(inFromMulticast);
                    
                } catch(ClassNotFoundException classNotFoundException) {
                    classNotFoundException.printStackTrace();
                }
            }
        }catch(IOException ioException){
            System.out.println("Erreur Serveur Multicast: " + ioException);
        }
    }

    /**
     * Arrêt du serveur Multicast
     *
     */
    public void ShutdownMulticast() {
        try {
            // Arrêt de la boucle du timer d'auto-message
            autoSendTimer.cancel();

            // Arrêt de l'écoute du canal multicast
            multicastSocket.leaveGroup(multicastGroupName);

            // Purge de la liste des connectés
            Configuration.ONLINE_USER_LIST.clear();

            // Cloture du port d'coute du serveur multicast
            multicastSocket.close();
            System.out.println("Le serveur multicast vient de se fermer.");
            
        } catch(IOException ioException) {
            System.out.println("Les serveur Multicast " + multicastSocket + " est déjà déconnecté !");
        }
    }

    /**
     * Préparaion des données User pour le packet de premier contact
     *
     * @return
     */
    public UserHandler MyLoginDatasFirstContact(){
        UserHandler myLoginDatasFC = new UserHandler();

        // Renseignement des différents champs du User
        myLoginDatasFC.setUserUniqueID(Configuration.USER_UNIQUE_ID);
        myLoginDatasFC.setUserPseudo(null);
        myLoginDatasFC.setUserOnlineStatus("ATTENTE DE CONNECTION");
        myLoginDatasFC.setUserTCPServerIP(Configuration.TCP_SERVER_IP);
        myLoginDatasFC.setUserTCPServerPort(Configuration.TCP_SERVER_PORT);
        myLoginDatasFC.setTimeStamp(Instant.now());

        return myLoginDatasFC;
    }

    /**
     * Préparation du packet de premier contact
     *
     * @return
     *
     */
    public ArrayList<UserHandler> FirstPacket(){
        UserHandler myUserDatas = MyLoginDatasFirstContact();
        ArrayList<UserHandler> myLoginDatas = new ArrayList<>();

        // Préparation de la table de login concernant le User
        myLoginDatas = UserListHandler.UserListUpdate(myLoginDatas, myUserDatas);

        return myLoginDatas;
    }

    /**
     * Préparation des données User pour les packets de présence
     *
     * @return
     *
     */
    public UserHandler MyLoginDatasHelloMulticast(){
        UserHandler myLoginDatasHM = new UserHandler();

        // Renseignement des différents champs du packet de présence
        myLoginDatasHM.setUserUniqueID(Configuration.USER_UNIQUE_ID);
        myLoginDatasHM.setUserPseudo(Configuration.USER_PSEUDO);
        myLoginDatasHM.setUserOnlineStatus(Configuration.USER_ONLINESTATUS);
        myLoginDatasHM.setUserTCPServerIP(Configuration.TCP_SERVER_IP);
        myLoginDatasHM.setUserTCPServerPort(Configuration.TCP_SERVER_PORT);
        myLoginDatasHM.setTimeStamp(Instant.now());

        return myLoginDatasHM;
    }

    /**
     * Préparation du packet de présence
     *
     * @return
     *
     */
    public ArrayList<UserHandler> HelloMulticastPacket(){
        UserHandler helloPacketUser = MyLoginDatasHelloMulticast();
        ArrayList<UserHandler> helloPacketList = new ArrayList<>();

        // Préparation de la table de login du packet de présence
        helloPacketList = UserListHandler.UserListUpdate(helloPacketList, helloPacketUser);

        return helloPacketList;
    }

     /**
     * Envoi d'un message de présence sur le serveur multicast
     * --> envoi le packet de données de présence en multicast
     *
     * @param packet
     *
     */
    public void SendUDPPacket(ArrayList<UserHandler> packet){
        try {
            // Conversion du packet de présence en données byte[]
            senderSocket = new DatagramSocket();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(packet);
            dataToSend = baos.toByteArray();
            baos.close();
            oos.close();

            // Préparation et envoi du paquet UDP sur le canal multicast
            DatagramPacket packetToSend = new DatagramPacket(dataToSend, dataToSend.length, InetAddress.getByName(Configuration.MULTICAST_IP), Configuration.MULTICAST_PORT);
            senderSocket.send(packetToSend);
            senderSocket.close();
            System.out.println("Envoi du packet UDP.");
            
        } catch(IOException ioException){
            System.out.println("Envoi du packet UDP multicast non effectué !");
        }
    }

    /**
     * Envoi automatique d'un message de présence toutes les 'TIMER' minutes
     * --> envoi le packet de données de présence en multicast
     *
     */
    private void AutomaticHelloMultiCast() {
        autoSendTimer = new Timer();
        autoSendTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                // Le jeu d'instructions est lancé toutes les AUTO_TIMER minutes.
                ArrayList<UserHandler> autoPacket = HelloMulticastPacket();
                SendUDPPacket(autoPacket);
            }
        }, 0, 1000 * 60 * Configuration.MULTICAST_AUTO_TIMER);  // 1000 ms/sec * 60 sec/min * AUTO_TIMER variable.
    }

    /**
     * Gestion des données réceptionnées depuis le serveur multicast
     * --> Mise à jour de la table des utilisateurs connectés au multicast
     *
     * @param packet
     *
     */
    private void GestionInfoMulticast(ArrayList<UserHandler> packet) {
        UserHandler user = MyLoginDatasHelloMulticast();
        Configuration.ONLINE_USER_LIST = packet;
        Configuration.ONLINE_USER_LIST = UserListHandler.UserListUpdate(packet, user );
    }
}
