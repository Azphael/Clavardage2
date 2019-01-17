package CONTROL;

import com.mongodb.BasicDBObject;
import com.mongodb.BulkWriteOperation;
import com.mongodb.BulkWriteResult;
import com.mongodb.Cursor;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.ParallelScanOptions;
import com.mongodb.ServerAddress;

import java.util.List;
import java.util.Set;

import static java.util.concurrent.TimeUnit.SECONDS;

public class DBHandler {

    public void main(){

        // To directly connect to a single MongoDB server (note that this will not auto-discover the primary even
        // if it's a member of a replica set:
        // MongoClient mongoClient = new MongoClient();
        // or
        // MongoClient mongoClient = new MongoClient( "localhost" );
        // or
        // MongoClient mongoClient = new MongoClient( "localhost" , 27017 );
        // or, to connect to a replica set, with auto-discovery of the primary, supply a seed list of members
        MongoClient mongoClient = new MongoClient(Arrays.asList(new ServerAddress("localhost", 27017),
                new ServerAddress("localhost", 27018),
                new ServerAddress("localhost", 27019)));

        DB db = mongoClient.getDB( "mydb" );
    }

    public void createNewMongoDB(String database){
//        database.ge
    }

    /**
     *
     * BDD MongoDB d'authentification
     *
     *      String LoginID
     *      String Password
     *      String userUniqueID
     *      String LastName
     *      String Name
     *      Boolean AdminRights
     *
     */

    /**
     *
     * ArrayList<PacketHandler> des personnes connectées
     *
     *     Elements Constitutif d'un élément de la liste:
     *          String userUniqueID;
     *          String userPseudo;
     *          String userOnlineStatus;
     *          InetAddress userTCPServerIP;
     *          int userTCPServerPort;
     *
     */

    /**
     *
     * BDD MongoDB des discussions archivées
     *
     *      String ChatRoomID
     *      String IDLocuteur1
     *      String PseudoLocuteur1
     *      String IDLocuteur2
     *      String PseudoLocuteur2
     *
     *      String Emetteur
     *      String Date
     *      String MessageText (Y compris message d'envoi/réception de fichiers)
     *
     */

}
