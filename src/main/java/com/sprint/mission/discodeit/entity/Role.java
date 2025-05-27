package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Entity
@Table(name = "roles")
@Getter
@AllArgsConstructor
public class Role  extends BaseUpdatableEntity {

  @Column(unique = true, nullable = false)
  private String name;

  public Role() {

  }
}
