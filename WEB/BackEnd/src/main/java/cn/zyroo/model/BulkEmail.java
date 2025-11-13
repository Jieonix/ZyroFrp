package cn.zyroo.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class BulkEmail {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String subject;

  @Column(columnDefinition = "TEXT")
  private String content;

  private int recipientCount;

  private String senderEmail;

  private LocalDateTime createdAt;

  // 预留字段：发送状态（成功/失败/部分失败）
  private String sendStatus = "SUCCESS";

  // 预留字段：失败数量
  private int failureCount = 0;

  public BulkEmail() {
    this.createdAt = LocalDateTime.now();
  }

  public BulkEmail(String subject, String content, int recipientCount, String senderEmail) {
    this.subject = subject;
    this.content = content;
    this.recipientCount = recipientCount;
    this.senderEmail = senderEmail;
    this.createdAt = LocalDateTime.now();
    this.sendStatus = "SUCCESS";
    this.failureCount = 0;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getSubject() {
    return subject;
  }

  public void setSubject(String subject) {
    this.subject = subject;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public int getRecipientCount() {
    return recipientCount;
  }

  public void setRecipientCount(int recipientCount) {
    this.recipientCount = recipientCount;
  }

  public String getSenderEmail() {
    return senderEmail;
  }

  public void setSenderEmail(String senderEmail) {
    this.senderEmail = senderEmail;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public String getSendStatus() {
    return sendStatus;
  }

  public void setSendStatus(String sendStatus) {
    this.sendStatus = sendStatus;
  }

  public int getFailureCount() {
    return failureCount;
  }

  public void setFailureCount(int failureCount) {
    this.failureCount = failureCount;
  }
}