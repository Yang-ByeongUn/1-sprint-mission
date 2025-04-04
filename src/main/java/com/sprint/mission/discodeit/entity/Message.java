package com.sprint.mission.discodeit.entity;

<<<<<<< HEAD
import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;

@Entity
@Table(name = "messages")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Message extends BaseUpdatableEntity {

  @Column(columnDefinition = "text", nullable = false)
  private String content;
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "channel_id", columnDefinition = "uuid")
  private Channel channel;
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "author_id", columnDefinition = "uuid")
  private User author;
  @BatchSize(size = 100)
  @OneToMany(fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
  @JoinTable(
      name = "message_attachments",
      joinColumns = @JoinColumn(name = "message_id"),
      inverseJoinColumns = @JoinColumn(name = "attachment_id")
  )
  private List<BinaryContent> attachments = new ArrayList<>();

  public Message(String content, Channel channel, User author, List<BinaryContent> attachments) {
    this.channel = channel;
    this.content = content;
    this.author = author;
    this.attachments = attachments;
  }

  public void update(String newContent) {
    if (newContent != null && !newContent.equals(this.content)) {
      this.content = newContent;
    }
  }
=======
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

public class Message {
    private final UUID id;
    private final Long createdAt;
    private Long updatedAt;

    private String text;
    private final UUID authorId;
    private final UUID channelId;

    public void updateText(String text) {
        this.text = text;
        this.updatedAt = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
    }

    public UUID getId() {
        return id;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }

    public String getText() {
        return text;
    }
    public String toString(){
        return "\nuuid: "+ id + " text: " + text + " authorId: " + authorId;
    }

    public Message(String text, UUID authorId, UUID channelId){
        this.authorId = authorId;
        this.channelId = channelId;
        this.id = UUID.randomUUID();
        this.createdAt = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
        this.updatedAt = createdAt;
        this.text = text;
    }

    public UUID getAuthorId() {
        return authorId;
    }

    public UUID getChannelId() {
        return channelId;
    }
>>>>>>> 67d19b8276e693bf808f9d9f2844e6ad4539f06b
}
