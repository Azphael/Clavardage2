package Clavardage.MODEL;

import java.util.ArrayList;

public interface DatabaseHandler {

    /**
     * Cette méthode doit permettre de rajouter des utilisateurs autorisés
     *
     * @param user
     */
    void StoreAuthorizedUser(AuthorizedUserHandler user);

    /**
     * Cette méthode doit permettre d'archiver des conversations
     *
     * @param chatRoom
     */
    void StoreChatRoom(PayloadHandler chatRoom);

    /**
     * Cette méthode doit permettre de conserver les informations d'utilisateurs connectés
     *
     * @param userList
     */
    void StoreUserList(UserListHandler userList);

    /**
     * Cette méthode doit permettre de récupérer l'archive de discussion associée à la ChatRoom
     * @param chatRoomID
     * @return
     */
    ArrayList<PayloadHandler> GetChatRoom(int chatRoomID);

    /**
     * Cette méthode doit permettre de retrouver les informations du User à partir de son userID
     *
     * @param userID
     */
    void GetUserDatas(int userID);

    /**
     * Cette méthode doit permettre de mettre à jour la valeur du pseudo d'un User
     *
     * @param newPseudo
     */
    void SetPseudo(String newPseudo);



    void CloseDatabase();
}
