package CONTROL;

import java.io.Serializable;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class PayloadHandler implements Serializable {

    public PayloadHandler() {
    }

    private static final long serialVersionUID = 1L;

    private String userSourceID = null;
    private String userDestinationID = null;
    private Instant timeStamp;                  // Date prise en temps UTC en nanosec. Pour avoir la date locale: ZonedDateTime zdt = timeStamp.atZone(ZoneId.of("Europe/Paris"))
    private String chatRoomID = null;
    private String messageType = null;

    private long payloadSize;
    private byte[] payloadData = null;
    private String status = null;



    public String getUserSourceID() { return userSourceID; }

    public void setUserSourceID(String userSourceID) { this.userSourceID = userSourceID; }

    public String getUserDestinationID() { return userDestinationID; }

    public void setUserDestinationID(String userDestinationID) { this.userDestinationID = userDestinationID; }

    public Instant getTimeStamp() { return timeStamp; }

    public void setTimeStamp(Instant timeStamp) { this.timeStamp = timeStamp; }

    public String getChatRoomID() { return chatRoomID; }

    public void setChatRoomID(String chatRoomID) { this.chatRoomID = chatRoomID; }

    public String getMessageType() { return messageType; }

    public void setMessageType(String messageType) { this.messageType = messageType; }

    public long getPayloadSize() { return payloadSize; }

    public void setPayloadSize(long payloadSize) { this.payloadSize = payloadSize; }

    public byte[] getPayloadData() { return payloadData; }

    public void setPayloadData(byte[] payloadData) { this.payloadData = payloadData; }

    public String getStatus() { return status; }

    public void setStatus(String status) { this.status = status; }
}