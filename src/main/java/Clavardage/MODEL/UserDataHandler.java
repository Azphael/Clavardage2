package Clavardage.MODEL;

import java.io.Serializable;
import java.net.InetAddress;
import java.time.Instant;

public class UserDataHandler implements Serializable {

    public UserDataHandler() {
    }

    private static final long serialVersionUID = 1L;

    private String userUniqueID;
    private String userPseudo;
    private String userOnlineStatus;
    private Instant timeStamp;          // Date en temps UTC en nanosec. Pour avoir la date locale: ZonedDateTime zdt = timeStamp.atZone(ZoneId.of("Europe/Paris"))
    private InetAddress userTCPServerIP;
    private int userTCPServerPort;

    public String getUserUniqueID() { return userUniqueID; }

    public void setUserUniqueID(String userUniqueID) { this.userUniqueID = userUniqueID; }

    public String getUserPseudo() { return userPseudo; }

    public void setUserPseudo(String userPseudo) { this.userPseudo = userPseudo; }

    public String getUserOnlineStatus() { return userOnlineStatus; }

    public void setUserOnlineStatus(String userOnlineStatus) { this.userOnlineStatus = userOnlineStatus; }

    public Instant getTimeStamp() { return timeStamp; }

    public void setTimeStamp(Instant timeStamp) { this.timeStamp = timeStamp; }

    public InetAddress getUserTCPServerIP() { return userTCPServerIP; }

    public void setUserTCPServerIP(InetAddress userTCPServerIP) { this.userTCPServerIP = userTCPServerIP; }

    public int getUserTCPServerPort() { return userTCPServerPort; }

    public void setUserTCPServerPort(int userTCPServerPort) { this.userTCPServerPort = userTCPServerPort; }
}
