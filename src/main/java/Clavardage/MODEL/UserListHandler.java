package Clavardage.MODEL;

import java.io.Serializable;
import java.util.List;

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
    public static List<UserDataHandler> UserListUpdate(List<UserDataHandler> arrayList, UserDataHandler user) {
        List<UserDataHandler> updateArrayList = arrayList;
        UserDataHandler userToAdd = user;

        int index = -1;
        for (int i = 0; i < updateArrayList.size(); i++) {
            if (updateArrayList.get(i).getUserUniqueID().equals(userToAdd.getUserUniqueID())){
                index = i;
            }
        }

        if (index != -1) {
            updateArrayList.remove(index);
        }
        if (!(userToAdd.getUserOnlineStatus() == "LOGOUT")){
            updateArrayList.add(userToAdd);
        }

        return updateArrayList;
    }
}