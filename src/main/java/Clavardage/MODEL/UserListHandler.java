package Clavardage.MODEL;

import java.io.Serializable;
import java.util.ArrayList;

public class UserListHandler implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Vérification de la présence ou non du nouveau User
     * --> mise à jour de la table et des infos du User
     *
     * @param arrayList
     * @param user
     * @return
     *
     */
    public static ArrayList<UserDataHandler> UserListUpdate(ArrayList<UserDataHandler> arrayList, UserDataHandler user) {
        ArrayList<UserDataHandler> updateArrayList = arrayList;
        UserDataHandler userToAdd = user;

        if (!updateArrayList.contains(userToAdd.getUserUniqueID())) {
            updateArrayList.add(userToAdd);
            System.out.println();
        }
        else {
            int index = updateArrayList.indexOf(userToAdd.getUserUniqueID());
            updateArrayList.set(index, userToAdd);
        }
        return updateArrayList;
    }
}