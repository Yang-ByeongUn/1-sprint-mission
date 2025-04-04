package com.sprint.mission.discodeit.service;

<<<<<<< HEAD
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import java.util.List;
=======
import com.sprint.mission.discodeit.entity.User;
import java.util.Map;
>>>>>>> 67d19b8276e693bf808f9d9f2844e6ad4539f06b
import java.util.Optional;
import java.util.UUID;

public interface UserService {
<<<<<<< HEAD

  UserDto create(UserCreateRequest userCreateRequest,
      Optional<BinaryContentCreateRequest> profileCreateRequest);

  UserDto find(UUID userId);

  List<UserDto> findAll();

  UserDto update(UUID userId, UserUpdateRequest userUpdateRequest,
      Optional<BinaryContentCreateRequest> profileCreateRequest);

  void delete(UUID userId);
=======
    User createUser(String username);

    Map<UUID, User> getUsers();

    Optional<User> getUser(UUID uuid);

    Optional<User> updateUser(UUID uuid, String username);

    Optional<User> deleteUser(UUID uuid);
>>>>>>> 67d19b8276e693bf808f9d9f2844e6ad4539f06b
}
