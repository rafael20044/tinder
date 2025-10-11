package io.ionic.starter.model;

import java.util.Date;

public class Message {
  private String id;
  private String senderId;
  private String senderName;
  private String text;
  private Date timestamp;
  private boolean read;
  private String type;

  // Constructores
  public Message() {}

  public Message(String senderId, String senderName, String text) {
    this.senderId = senderId;
    this.senderName = senderName;
    this.text = text;
    this.timestamp = new Date();
    this.read = false;
    this.type = "text";
  }

  // Getters y Setters
  public String getId() { return id; }
  public void setId(String id) { this.id = id; }

  public String getSenderId() { return senderId; }
  public void setSenderId(String senderId) { this.senderId = senderId; }

  public String getSenderName() { return senderName; }
  public void setSenderName(String senderName) { this.senderName = senderName; }

  public String getText() { return text; }
  public void setText(String text) { this.text = text; }

  public Date getTimestamp() { return timestamp; }
  public void setTimestamp(Date timestamp) { this.timestamp = timestamp; }

  public boolean isRead() { return read; }
  public void setRead(boolean read) { this.read = read; }

  public String getType() { return type; }
  public void setType(String type) { this.type = type; }
}
