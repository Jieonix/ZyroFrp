package cn.zyroo.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;


@Entity
public class Announcements {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String title;

  @Lob
  private String content;

  @CreationTimestamp
  private LocalDateTime created_at;

  @CreationTimestamp
  private LocalDateTime updated_at;

  private int is_pinned;



  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public LocalDateTime getCreated_at() {
    return created_at;
  }

  public void setCreated_at(LocalDateTime created_at) {
    this.created_at = created_at;
  }

  public LocalDateTime getUpdated_at() {
    return updated_at;
  }

  public void setUpdated_at(LocalDateTime updated_at) {
    this.updated_at = updated_at;
  }

  public int getIs_pinned() {
    return is_pinned;
  }

  public void setIs_pinned(int is_pinned) {
    this.is_pinned = is_pinned;
  }

}
