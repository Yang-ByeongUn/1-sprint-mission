package com.sprint.mission.discodeit.entity;

<<<<<<< HEAD
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)  // JPA를 위한 기본 생성자
public class User extends BaseUpdatableEntity {

  @Column(length = 50, nullable = false, unique = true)
  private String username;
  @Column(length = 100, nullable = false, unique = true)
  private String email;
  @Column(length = 60, nullable = false)
  private String password;
  @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
  @JoinColumn(name = "profile_id", columnDefinition = "uuid")
  private BinaryContent profile;
  @JsonManagedReference
  @Setter(AccessLevel.PROTECTED)
  @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  private UserStatus status;

  public User(String username, String email, String password, BinaryContent profile) {
    this.username = username;
    this.email = email;
    this.password = password;
    this.profile = profile;
  }

  public void update(String newUsername, String newEmail, String newPassword,
      BinaryContent newProfile) {
    if (newUsername != null && !newUsername.equals(this.username)) {
      this.username = newUsername;
    }
    if (newEmail != null && !newEmail.equals(this.email)) {
      this.email = newEmail;
    }
    if (newPassword != null && !newPassword.equals(this.password)) {
      this.password = newPassword;
    }
    if (newProfile != null) {
      this.profile = newProfile;
    }
  }
=======
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

public class User {
    private final UUID id;
    private final Long createdAt;
    private Long updatedAt;

    private String username;

    public User(String username) {
        this.id = UUID.randomUUID();
        this.username = username;
        this.createdAt = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
        this.updatedAt = createdAt;
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

    public String getUsername() {
        return username;
    }


    public void updateUsername(String username) {
        this.username = username;
        this.updatedAt = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
    }
>>>>>>> 67d19b8276e693bf808f9d9f2844e6ad4539f06b
}
