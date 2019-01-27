package Clavardage.DATABASE;

import Clavardage.MODEL.AuthorizedUserHandler;
import Clavardage.MODEL.DatabaseHandler;
import Clavardage.MODEL.PayloadHandler;
import Clavardage.MODEL.UserListHandler;

import java.util.ArrayList;

public class Database implements DatabaseHandler {

    @Override
    public void StoreAuthorizedUser(AuthorizedUserHandler user) {

    }

    @Override
    public void StoreChatRoom(PayloadHandler chatRoom) {

    }

    @Override
    public void StoreUserList(UserListHandler userList) {

    }

    @Override
    public ArrayList<PayloadHandler> GetChatRoom(int chatRoomID) {
        return null;
    }

    @Override
    public void GetUserDatas(int userID) {

    }

    @Override
    public void SetPseudo(String newPseudo) {

    }

    @Override
    public void CloseDatabase() {

    }
}
