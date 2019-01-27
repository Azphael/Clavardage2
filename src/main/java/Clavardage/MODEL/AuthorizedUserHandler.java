package Clavardage.MODEL;

import java.io.Serializable;

public class AuthorizedUserHandler implements Serializable {

    public AuthorizedUserHandler(){
    }

    private static long serialVersionUID = 1L;

    private String userUniqueID;
    private String userLoginID;
    private String userPassword;
    private Boolean adminRights;
    private String userName;
    private String userLastName;
    private String userMail;

    public static long getSerialVersionUID() { return serialVersionUID; }

    public static void setSerialVersionUID(long serialVersionUID) { AuthorizedUserHandler.serialVersionUID = serialVersionUID; }

    public String getUserUniqueID() { return userUniqueID; }

    public void setUserUniqueID(String userUniqueID) { this.userUniqueID = userUniqueID; }

    public String getUserLoginID() { return userLoginID; }

    public void setUserLoginID(String userLoginID) { this.userLoginID = userLoginID; }

    public String getUserPassword() { return userPassword; }

    public void setUserPassword(String userPassword) { this.userPassword = userPassword; }

    public Boolean getAdminRights() { return adminRights; }

    public void setAdminRights(Boolean adminRights) { this.adminRights = adminRights; }

    public String getUserName() { return userName; }

    public void setUserName(String userName) { this.userName = userName; }

    public String getUserLastName() { return userLastName; }

    public void setUserLastName(String userLastName) { this.userLastName = userLastName; }

    public String getUserMail() { return userMail; }

    public void setUserMail(String userMail) { this.userMail = userMail; }
}
