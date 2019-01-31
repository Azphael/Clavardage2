package Clavardage.NETWORK;

import Clavardage.MODEL.Configuration;
import Clavardage.MODEL.UserDataHandler;
import Clavardage.MODEL.UserListHandler;
import Clavardage.VIEW.MainFrameController;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.time.Instant;
import java.util.*;

public class Multicast extends Thread {

    public static Multicast uniqueMulticastInstance = null;

    private static MulticastSocket multicastSocket;
    private static DatagramSocket senderSocket;
    private static InetAddress multicastGroupName;
    private static byte[] receivedData = new byte[1024];
    private static byte[] dataToSend = new byte[1024];
    private static Timer autoSendTimer;
    private static List<UserDataHandler>   inFromMulticast = new ArrayList<>();


    /**
     * Lancement serveur Multicast
     * --> mise en attente de messages entrants
     * --> lancement du timer d'envoi automatique de message de présence
     *
     */
    public void run(){
        try{
            // Purge de la liste des connectés
            // Configuration.ONLINE_USER_LIST.clear();

            // Récupéraition de l'IP routable
            Configuration.TCP_SERVER_IP = Configuration.MyRoutableIP();

            // Ouverture du port d'écoute du serveur multicast
            multicastSocket = new MulticastSocket(Configuration.MULTICAST_PORT);
            System.out.println("MULTICAST====X Serveur Multicast créé -> port " + multicastSocket.getPort());

            // Accès au groupe multicast donné
            multicastGroupName = InetAddress.getByName(Configuration.MULTICAST_IP);
            multicastSocket.joinGroup(multicastGroupName);
            System.out.println("MULTICAST====X Serveur Multicast : group \"" + multicastGroupName + "\" rejoint.");

            // Envoi du premier message de présence à fin de récupération de la liste des connectés
            List<UserDataHandler> fPacket = FirstPacket();
            SendUDPPacket(fPacket);

            // Lancement du timer d'envoi automatique d'un message de présence
            AutomaticHelloMultiCast();

            // Serveur Multicast en attente de messages entrants
            while(!multicastSocket.isClosed()) {
                // Réception d'un packet de données entrant
                DatagramPacket inputPacket = new DatagramPacket(receivedData, receivedData.length);
                try {
                    multicastSocket.receive(inputPacket);
                    System.out.println("MULTICAST====X Serveur Multicast - Packet Multicast reçu.");

                    // Conversion du DatagramPacket en données ArrayList<UserDataHandler>
                    byte[] receivedDatas = inputPacket.getData();
                    ByteArrayInputStream baois = new ByteArrayInputStream(receivedDatas);
                    ObjectInputStream ois = new ObjectInputStream(baois);
                    inFromMulticast = (List<UserDataHandler>) ois.readObject();
                    ois.close();

                    // Gestion des données reçues
                    GestionInfoMulticast(inFromMulticast);
                    
                } catch(ClassNotFoundException classNotFoundException) {
                    classNotFoundException.printStackTrace();
                }
            }
        }catch(IOException ioException){
            System.out.println("MULTICAST====X Erreur Serveur Multicast: " + ioException);
        }
    }

    /**
     * Singleton Instance
     *
     * @return
     *
     */
    public static Multicast getInstance() {
        if (uniqueMulticastInstance == null){
            uniqueMulticastInstance = new Multicast();
        }
        return uniqueMulticastInstance;
    }

    /**
     * Arrêt du serveur Multicast
     *
     */
    public static void ShutdownMulticast() {
        try {
            // Arrêt de la boucle du timer d'auto-message
            autoSendTimer.cancel();

            if (!multicastSocket.isClosed()) {
                // Envoi du message de logout
                List<UserDataHandler> lPacket = LastPacket();
                SendUDPPacket(lPacket);

                // Arrêt de l'écoute du canal multicast
                multicastSocket.leaveGroup(multicastGroupName);

                // Purge de la liste des connectés
                Configuration.ONLINE_USER_LIST.clear();

                // Cloture du port d'écoute du serveur multicast
                multicastSocket.close();
                System.out.println("MULTICAST====X Le serveur multicast vient de se fermer.");
            }
        } catch(IOException ioException) {
            System.out.println("MULTICAST====X Les serveur Multicast " + multicastSocket + " est déjà déconnecté !");
        }
    }

    /**
     * Préparation des données User pour le packet de premier contact
     *
     * @return
     */
    public static UserDataHandler MyLoginDatasFirstContact(){
        UserDataHandler myLoginDatasFC = new UserDataHandler();

        // Renseignement des différents champs du User
        myLoginDatasFC.setUserUniqueID(Configuration.USER_UNIQUE_ID);
        myLoginDatasFC.setUserPseudo("????");
        myLoginDatasFC.setUserOnlineStatus("WAITING TO CONNECT");
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
    public static List<UserDataHandler> FirstPacket(){
        UserDataHandler loginDatasFC = MyLoginDatasFirstContact();
        List<UserDataHandler> firstPacketList = new ArrayList<>();

        // Préparation de la table de login concernant le User
        firstPacketList = UserListHandler.UserListUpdate(firstPacketList, loginDatasFC);

        return firstPacketList;
    }

    /**
     * Préparation des données User pour les packets de présence
     *
     * @return
     *
     */
    public static UserDataHandler MyLoginDatasHelloMulticast(){
        UserDataHandler myLoginDatasHM = new UserDataHandler();

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
    public static List<UserDataHandler> HelloMulticastPacket(){
        UserDataHandler helloPacketUser = MyLoginDatasHelloMulticast();
        List<UserDataHandler> helloPacketList = new ArrayList<>();

        // Préparation de la table de login du packet de présence
        helloPacketList = UserListHandler.UserListUpdate(helloPacketList, helloPacketUser);

        return helloPacketList;
    }

    /**
     * Préparation des données User pour le packet de logout
     *
     * @return
     */
    public static UserDataHandler MyLoginDatasLogout(){
        UserDataHandler myLogoutDatasLO = new UserDataHandler();

        // Renseignement des différents champs du User
        myLogoutDatasLO.setUserUniqueID(Configuration.USER_UNIQUE_ID);
        myLogoutDatasLO.setUserPseudo("");
        myLogoutDatasLO.setUserOnlineStatus("LOGOUT");
        myLogoutDatasLO.setUserTCPServerIP(null);
        myLogoutDatasLO.setTimeStamp(Instant.now());

        return myLogoutDatasLO;
    }

    /**
     * Préparation du packet de logout
     *
     * @return
     *
     */
    public static List<UserDataHandler> LastPacket(){
        UserDataHandler logOutDatas = MyLoginDatasLogout();
        List<UserDataHandler> byeByePacketList = new ArrayList<>();

        // Préparation de la table de logout
        byeByePacketList = UserListHandler.UserListUpdate(byeByePacketList, logOutDatas);

        return byeByePacketList;
    }

    /**
     * Envoi d'un message de présence sur le serveur multicast
     * --> envoi le packet de données de présence en multicast
     *
     * @param packet
     *
     */
    public static void SendUDPPacket(List<UserDataHandler> packet){
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
            System.out.println("MULTICAST====X Envoi du packet UDP.");
            
        } catch(IOException ioException){
            System.out.println("MULTICAST====X Envoi du packet UDP multicast non effectué !");
        }
    }

    /**
     * Envoi automatique d'un message de présence toutes les 'TIMER' minutes
     * --> envoi le packet de données de présence en multicast
     *
     */
    private static void AutomaticHelloMultiCast() {
        autoSendTimer = new Timer();
        autoSendTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                // Le jeu d'instructions est lancé toutes les AUTO_TIMER minutes.
                if (Configuration.MULTICAST_LAST_CONNECTED == true){
                    Configuration.ONLINE_USER_LIST = UserListHandler.UserListUpdate(Configuration.ONLINE_USER_LIST, MyLoginDatasHelloMulticast());
                    SendUDPPacket(Configuration.ONLINE_USER_LIST);
                } else {
                    List<UserDataHandler> autoPacket = HelloMulticastPacket();
                    SendUDPPacket(autoPacket);
                }
            }
        }, 0, 1000 * 60 * Configuration.MULTICAST_AUTO_TIMER);  // 1000 ms/sec * 60 sec/min * AUTO_TIMER variable.
    }

    /**
     * Gestion des données réceptionnées depuis le serveur multicast
     * --> Mise à jour de la table des utilisateurs connectés au multicast
     * --> Si plus le dernier connecté, passage de relai
     * --> Mise à jour de la listeView d'affichage des utilisateurs connectés
     *
     * @param packet
     *
     */
    private static void GestionInfoMulticast(List<UserDataHandler> packet) {
        if (!packet.isEmpty()) {
            for (int i = 0; i < packet.size(); i++) {
                System.out.println("1 :> " + packet.size() + " - " + packet.get(0).getUserUniqueID() + " - " + packet.get(0).getUserPseudo() + " - " + packet.get(0).getUserOnlineStatus());
            }
            // Cas ou le premier objet UserData de la liste reçue a un Pseudo Anonyme (????) et que le user local était le dernier connecté
            UserDataHandler userToAdd = packet.get(0);
            if (userToAdd.getUserPseudo().equals("????") && (!(userToAdd.getUserUniqueID().equals(Configuration.USER_UNIQUE_ID))) && Configuration.MULTICAST_LAST_CONNECTED.equals(true)) {
                System.out.println("MULTICAST====X Un nouvel arrivant sur le canal multicast: envoi de la liste de connecté et passage de relai du dernier connecté");
                Configuration.ONLINE_USER_LIST = UserListHandler.UserListUpdate(Configuration.ONLINE_USER_LIST, userToAdd);
                SendUDPPacket(Configuration.ONLINE_USER_LIST);
                Configuration.MULTICAST_LAST_CONNECTED = false;
            }
            Configuration.ONLINE_USER_LIST = UserListHandler.UserListUpdate(Configuration.ONLINE_USER_LIST, userToAdd);
            System.out.println("2 :> " + Configuration.ONLINE_USER_LIST.size() + " - " + Configuration.ONLINE_USER_LIST.get(0).getUserUniqueID() + " - " + Configuration.ONLINE_USER_LIST.get(0).getUserPseudo() + " - " + Configuration.ONLINE_USER_LIST.get(0).getUserOnlineStatus());

            // Cas ou le packet reçu contient plus de 2 objets UserData
            if (packet.size() > 1) {
                for (int i = 1; i < packet.size(); i++) {
                    userToAdd = packet.get(i);
                    System.out.println("MULTICAST====X Traitement d'un paquet UDP.");
                    if (!(userToAdd.getUserUniqueID().equals(Configuration.USER_UNIQUE_ID))) {
                        System.out.println("MULTICAST====X Mise à jour de la table des connectés.");
                        Configuration.ONLINE_USER_LIST = UserListHandler.UserListUpdate(Configuration.ONLINE_USER_LIST, userToAdd);
                    }
                    System.out.println("MULTICAST====X Mise à jour de la table des connectés avec les paramètre du user local.");
                    Configuration.ONLINE_USER_LIST = UserListHandler.UserListUpdate(Configuration.ONLINE_USER_LIST, MyLoginDatasHelloMulticast());

                    System.out.println("3 :> " + Configuration.ONLINE_USER_LIST.size() + " - " + Configuration.ONLINE_USER_LIST.get(i).getUserUniqueID() + " - " + Configuration.ONLINE_USER_LIST.get(i).getUserPseudo() + " - " + Configuration.ONLINE_USER_LIST.get(i).getUserOnlineStatus());
                }
            }
            for (int i = 0; i < Configuration.ONLINE_USER_LIST.size(); i++) {
                System.out.println("4 :> " + Configuration.ONLINE_USER_LIST.size() + " - " + Configuration.ONLINE_USER_LIST.get(i).getUserUniqueID() + " - " + Configuration.ONLINE_USER_LIST.get(i).getUserPseudo() + " - " + Configuration.ONLINE_USER_LIST.get(i).getUserOnlineStatus());
            }

            // MainFrameController.UpdateOnlineUserListView();
        } else {
            System.out.println("5 :> Packet vide");
        }
    }
}
