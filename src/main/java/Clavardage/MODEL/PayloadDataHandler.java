package Clavardage.MODEL;

import java.io.*;
import java.util.Base64;

public class PayloadDataHandler {

    /**
     * Récupération du texte envoyé
     * --> Conversion de données de type Byte[] en données de type String
     *
     * @param payload
     * @return
     *
     */
    public static String PayloadToString(Clavardage.MODEL.PayloadHandler payload) {
        String convertedByte = Base64.getEncoder().encodeToString(payload.getPayloadData());
        return convertedByte;
    }

    /**
     * Mise en place du Payload d'envoi de message texte
     * --> Conversion de données de type String en données de type Byte[]
     *
     * @param message
     * @param payload
     */
    public static void StringToPayload(String message, Clavardage.MODEL.PayloadHandler payload){
        byte[] convertedMessage = Base64.getDecoder().decode(message);
        payload.setPayloadData(convertedMessage);
    }

    /**
     * Récupération et sauvegarde du fichier envoyé
     * --> Conversion de données de type Byte[] en données de type fichier
     *
     * @param fileToCreate
     * @param payload
     */
    public static void PayloadToFile(String fileToCreate, Clavardage.MODEL.PayloadHandler payload) {
        try{
            File downloadFile = new File(fileToCreate);
            FileOutputStream foStream = new FileOutputStream(downloadFile);
            foStream.write(payload.getPayloadData());
            foStream.flush();
            foStream.close();
            System.out.println("TCP CLIENT===C Création du fichier : " + fileToCreate);
        }catch(IOException ioException){
            ioException.printStackTrace();
        }
    }

    /**
     * Mise en place du Payload d'envoi de fichier
     * --> Conversion d'un fichier de données en données de type Byte[]
     *
     * @param fileToSend
     * @param payload
     *
     */
    public static void FileToPayload(String fileToSend, PayloadHandler payload){
        File file = new File(fileToSend);
        if (file.isFile()){
            try{
                DataInputStream diStream = new DataInputStream(new FileInputStream(file));
                long length = (int) file.length();
                byte[] fileBytes = new byte[(int) length];
                int read = 0;
                int numRead;
                while (read < fileBytes.length && (numRead = diStream.read(fileBytes, read, fileBytes.length - read)) >= 0){
                    read = read + numRead;
                }
                payload.setPayloadSize(length);
                payload.setPayloadData(fileBytes);
                payload.setStatus("Success");
            }catch(Exception exception){
                exception.printStackTrace();
                payload.setStatus("Error");
            }
        }
        else {
            System.out.println("TCP CLIENT===C Le chemin spécifié ne pointe pas sur un fichier.");
            payload.setStatus("Error");
        }
    }
}
