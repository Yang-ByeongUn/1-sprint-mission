package com.sprint.mission.discodeit.service.basic;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.data.UserStatusDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserStatusCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.exception.userStatus.UserStatusNotFoundException;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import jakarta.transaction.Transactional;
import java.time.Instant;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
@Rollback
class BasicUserStatusServiceTest {

  @Autowired
  BasicMessageService basicMessageService;
  @Autowired
  BasicChannelService basicChannelService;
  @Autowired
  BasicUserService basicUserService;
  @Autowired
  BasicUserStatusService basicUserStatusService;
  @Autowired
  private UserStatusService userStatusService;
  @Autowired
  private UserService userService;

  @Test
  void BasicUserStatusService_생성_테스트() {
    UserCreateRequest userCreateRequest1 = new UserCreateRequest("Yang1", "yang1@naver.com", "123456");
    Optional<BinaryContentCreateRequest> createRequest = Optional.empty();
    UserDto userDto1 = basicUserService.create(userCreateRequest1, createRequest);
    //
    UserStatusDto userStatusDto = userStatusService.findByUserId(userDto1.id());
    //
    assertThat(userStatusDto.userId()).isEqualTo(userDto1.id());
  }

  @Test
  void BasicUserStatusService_업데이트_테스트() {
    UserCreateRequest userCreateRequest1 = new UserCreateRequest("Yang1", "yang1@naver.com", "123456");
    Optional<BinaryContentCreateRequest> createRequest = Optional.empty();
    UserDto userDto1 = basicUserService.create(userCreateRequest1, createRequest);
    //
    UserStatusDto userStatusDto = basicUserStatusService.findByUserId(userDto1.id());

    Instant specificTime = Instant.parse("2024-12-25T15:00:00Z");
    UserStatusUpdateRequest updateRequest = new UserStatusUpdateRequest(specificTime);
    basicUserStatusService.update(userStatusDto.id(), updateRequest);
    //
    UserStatusDto updatedUserStatusDto = basicUserStatusService.findByUserId(userDto1.id());
    assertThat(updatedUserStatusDto.lastActiveAt()).isEqualTo(specificTime);
  }

  @Test
  void BasicUserStatusService_삭제_테스트() {
    UserCreateRequest userCreateRequest1 = new UserCreateRequest("Yang1", "yang1@naver.com", "123456");
    Optional<BinaryContentCreateRequest> createRequest = Optional.empty();
    UserDto userDto1 = basicUserService.create(userCreateRequest1, createRequest);
    //
    UserStatusDto userStatusDto = basicUserStatusService.findByUserId(userDto1.id());
    assertThat(userStatusDto.userId()).isEqualTo(userDto1.id());
    //
    userService.delete(userDto1.id());
    //
    Optional<UserStatusDto> findUserStatusByUserId = userStatusService.findAll().stream().filter(s -> s.userId().equals(userDto1.id())).findAny();
    //
    assertThat(findUserStatusByUserId.isPresent()).isFalse();
    assertThrows(UserStatusNotFoundException.class, () -> userStatusService.find(userStatusDto.id()));
    assertThrows(UserNotFoundException.class, () -> basicUserStatusService.findByUserId(userDto1.id()));

  }


}