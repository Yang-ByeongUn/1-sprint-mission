package com.sprint.mission.discodeit.service.basic;

import static java.util.stream.Collectors.toList;

import com.sprint.mission.discodeit.dto.data.UserStatusDto;
import com.sprint.mission.discodeit.dto.request.UserStatusCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.user.UserAlreadyExistsException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.exception.userStatus.UserStatusNotFoundException;
import com.sprint.mission.discodeit.mapper.UserStatusMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class BasicUserStatusService implements UserStatusService {

  private final UserStatusRepository userStatusRepository;
  private final UserRepository userRepository;
  private final UserStatusMapper userStatusMapper;

  @Transactional
  @Override
  public UserStatusDto create(UserStatusCreateRequest request) {
    UUID userId = request.userId();

    User user = userRepository.findById(userId)
        .orElseThrow(
            //() -> new NoSuchElementException("UserStatus with userId " + userId + " not found"));
            () -> new UserNotFoundException(userId));
    Optional.ofNullable(user.getStatus())
        .ifPresent(status -> {
          //throw new IllegalArgumentException("UserStatus with id " + userId + " already exists");
          throw new UserAlreadyExistsException(userId);
        });

    Instant lastActiveAt = request.lastActiveAt();
    UserStatus userStatus = new UserStatus(user, lastActiveAt);
    userStatusRepository.save(userStatus);
    return userStatusMapper.toDto(userStatus);
  }

  @Override
  public UserStatusDto find(UUID userStatusId) {
    return userStatusRepository.findById(userStatusId)
        .map(userStatusMapper::toDto)
        .orElseThrow(
            //() -> new NoSuchElementException("UserStatus with id " + userStatusId + " not found"));
            () -> new UserStatusNotFoundException(userStatusId));
  }
  @Override
  public UserStatusDto findByUserId(UUID userId) {
    UserStatus userStatus = userStatusRepository.findAll().stream().filter(s -> s.getUser().getId().equals(userId))
        .findAny().orElseThrow(
            () -> new UserNotFoundException(userId)
        );

    return userStatusMapper.toDto(userStatus);
  }

  @Override
  public List<UserStatusDto> findAll() {
    return userStatusRepository.findAll().stream()
        .map(userStatusMapper::toDto)
        .toList();
  }

  @Transactional
  @Override
  public UserStatusDto update(UUID userStatusId, UserStatusUpdateRequest request) {
    Instant newLastActiveAt = request.newLastActiveAt();

    UserStatus userStatus = userStatusRepository.findById(userStatusId)
        .orElseThrow(
            () -> new UserStatusNotFoundException(userStatusId));
    userStatus.update(newLastActiveAt);

    return userStatusMapper.toDto(userStatus);
  }

  @Transactional
  @Override
  public UserStatusDto updateByUserId(UUID userId, UserStatusUpdateRequest request) {
    Instant newLastActiveAt = request.newLastActiveAt();

    UserStatus userStatus = userStatusRepository.findByUserId(userId)
        .orElseThrow(
            () -> new UserNotFoundException(userId));
    userStatus.update(newLastActiveAt);

    return userStatusMapper.toDto(userStatus);
  }

  /*@Transactional
  @Override
  public void delete(UUID userStatusId) {
    Optional<UserStatus> targetUserStatus = userStatusRepository.findById(userStatusId);
    if (!targetUserStatus.isEmpty()) {
      userStatusRepository.deleteById(targetUserStatus.get().getId());
      return;
    }
    throw new UserStatusNotFoundException(userStatusId);

  }*/
}
