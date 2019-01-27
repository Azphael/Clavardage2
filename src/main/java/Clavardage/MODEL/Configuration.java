package Clavardage.MODEL;

import java.io.File;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import static java.net.InetAddress.getByName;

public class Configuration {

    /**
     * Nom et version de l'application
     *
     */
    public static String APPLICATION_NAME = "ClavAppController 0.1";

    /**
     *  Nom de l'entreprise
     */
    public static File COMPANY_LOGO = new File("@Images/placeholder_logo_500x400.png");
    public static String COMPANY_NAME = "TROLOLO Inc.";

    /**
    *  Variables pour le Multicast
    */
    public static String MULTICAST_IP = "237.13.13.13";
    public static int MULTICAST_PORT = 13013;
    public static String MULTICAST_LOGSERVER_IP = "";
    public static int MULTICAST_LOGSERVER_PORT = 0;
    public static int MULTICAST_AUTO_TIMER = 5;          // Timer de 5 minutes pour envoi automatique de message multicast de présence
    public static ArrayList<Clavardage.MODEL.UserDataHandler> ONLINE_USER_LIST;
    public static Boolean MULTICAST_LAST_CONNECTED = true;

    /**
     *  Variables pour le TCP
     */
    public static InetAddress ROUTABLE_IP;
    public static InetAddress TCP_SERVER_IP;            // par default est sur localhost, mais doit être placé sur l'adresse routable donnée par ROUTABLE_IP;
    public static int TCP_SERVER_PORT = 13213;
    public static int TCP_SERVER_BACKLOG = 50;
    public static int TCP_CLIENT_PORT = 13313;
    public static int RCP_CLIENT_BACKLOG = 50;
    public static int TCP_FILE_SERVER_PORT = 13213;
    public static int TCP_FILE_CLIENT_PORT = 13313;

    /**
     *  Variable de localisation des fichiers à tranférer
     */
    public static String PATH_TO_DOWNLOAD = "../FileDestination";            // Chemin d'accès où placer le fichier à récupérer
    public static String FILE_TO_DOWNLOAD = "destination.pdf";               // Nom du fichier à récupérer
    public static String DOWNLOAD = PATH_TO_DOWNLOAD.concat(FILE_TO_DOWNLOAD);

    public static String PATH_TO_UPLOAD = "../FileSource";                   // Chemin d'accès où placer le fichier à envoyer
    public static String FILE_TO_UPLOAD = "source.pdf";                      // Nom du fichier à envoyer
    public static String UPLOAD = PATH_TO_UPLOAD.concat(FILE_TO_UPLOAD);

    public static int FILE_SIZE = 7000000;                                   // Taille (ou +) du fichier transféré --> en dur en attendant de pouvoir le récupérer par l'intermédiaire d'une interface ou autre moyen

    /**
     *  Variables d'identification User
     */
    public static String USER_ID;
    public static String USER_PWD;
    public static String USER_UNIQUE_ID;
    public static Boolean USER_ADMINRIGHTS;
    public static String USER_PSEUDO = null;
    public static String USER_LASTNAME;
    public static String USER_NAME;
    public static String USER_FULL_NAME = USER_LASTNAME.concat(USER_NAME);
    public static String USER_ONLINESTATUS;

    /**
     *  Variables d'identité du distant User
     */
    private static String DISTANT_UNIQUE_ID;
    private static String DISTANT_PSEUDO;
    private static String DISTANT_ONLINESTATUS;

    /**
     *  Variables pour l'affichage de l'historique de discussion
     */
    /*
    protected static boolean LOG_TO_FILE = false;
    private static String LOG_FILE_PATH = "D:/INSA/IFCI-IR-CT1/COO-POO/POO/Projet Clavardage.ClavAppController/Prog/temp_tests/";   // Chemin d'accès où placer le fichier historique de conversation
    private static String LOG_FILE_BASE_NAME = "ChatLog";
    private static String LOG_FILE_ID_NAME = USER_UNIQUE_ID.concat(DISTANT_UNIQUE_ID);
    protected static String LOG_FILE = LOG_FILE_PATH.concat(LOG_FILE_BASE_NAME.concat(LOG_FILE_ID_NAME.concat(".txt")));
    protected static String LOG_FORMAT_DATE = "yyyy-MM-dd HH:mm:ss";
    */

    /**
     * Variables pour l'interface utilisateur
     */
    /*
    protected static String GUI_BIENVENUE = "Bienvenue sur le salon de discussion de " + ENTREPRISE;
    protected static String GUI_FORMAT_HEURE = "HH:mm:ss";
    protected static String GUI_CHOIX_PSEUDO = "Choisissez votre pseudonyme:";
    protected static String GUI_PSEUDO_ERREUR_INVALIDE = "Ce pseudonyme est déjà utilisé";
    protected static String GUI_PSEUDO_ERREUR_VIDE = "Votre pseudonyme ne peut être vide.";
    protected static String GUI_CONFIRMATION_FERMETURE = "Voulez-vous vraiment quitter ?";
    protected static String GUI_BOUTON_VALIDER = "Valider";
    protected static String GUI_BOUTON_ANNULER = "Annuler";
    protected static String GUI_BOUTON_QUITTER = "Quitter";
    protected static int GUI_LARGEUR = 640;
    protected static int GUI_HAUTEUR = 480;
    protected static int GUI_TAILLE_FONT = 14;
    protected static int GUI_MARGE_CHAT = 10;
    */



    /**
     *  Renvoi l'adresse IPv4 routable de l'ordinateur Host
     *
     */
    public static InetAddress myRoutableIP;

    public static InetAddress MyRoutableIP() {
        try (final DatagramSocket socket = new DatagramSocket()) {
            socket.connect(getByName("8.8.8.8"), 13113);
            myRoutableIP = socket.getLocalAddress();
        } catch (UnknownHostException e) {
            System.out.println("No Internet");
        } catch (SocketException e) {
            e.printStackTrace();
        }

        return myRoutableIP;
    }

}
