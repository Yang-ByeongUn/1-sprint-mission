package com.sprint.mission.discodeit.entity;

<<<<<<< HEAD
import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "channels")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Channel extends BaseUpdatableEntity {

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private ChannelType type;
  @Column(length = 100)
  private String name;
  @Column(length = 500)
  private String description;

  public Channel(ChannelType type, String name, String description) {
    this.type = type;
    this.name = name;
    this.description = description;
  }

  public void update(String newName, String newDescription) {
    if (newName != null && !newName.equals(this.name)) {
      this.name = newName;
    }
    if (newDescription != null && !newDescription.equals(this.description)) {
      this.description = newDescription;
    }
  }

  @Override
  public String toString() {
    return "Channel{" +
        "id= " + getId()+
        "type=" + type +
        ", name='" + name + '\'' +
        ", description='" + description + '\'' +
        '}';
  }
=======
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Channel {
    private final UUID id;
    private final Long createdAt;
    private Long updatedAt;
    private String channelName;
    private List<UUID> messageList;

    public UUID getId() {
        return id;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }

    public String getChannelName() {
        return channelName;
    }

    public List<UUID> getMessageList() {
        return messageList;
    }

    public UUID addMessageToChannel(UUID messageUUID) {
        messageList.add(messageUUID);
        return messageUUID;
    }

    public String toString(){
        return "\nuuid: "+ id + " channelName: " + channelName;
    }

    public void updateChannelName(String channelName) {
        this.channelName = channelName;
        this.updatedAt = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
    }

    public Channel(String channelName){
        this.id = UUID.randomUUID();
        this.createdAt = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
        this.updatedAt = createdAt;
        this.channelName = channelName;
        this.messageList = new ArrayList<>();
    }
>>>>>>> 67d19b8276e693bf808f9d9f2844e6ad4539f06b
}
