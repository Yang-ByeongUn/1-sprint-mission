package com.sprint.mission.discodeit.service.basic;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import jakarta.transaction.Transactional;
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
class BasicUserServiceTest {
  @Autowired
  BasicMessageService basicMessageService;
  @Autowired
  BasicChannelService basicChannelService;
  @Autowired
  BasicUserService basicUserService;

  @Test
  void UserService_생성_테스트(){

    UserCreateRequest userCreateRequest1 = new UserCreateRequest("Yang1", "yang1@naver.com", "123456");
    Optional<BinaryContentCreateRequest> createRequest = Optional.empty();
    UserDto userDto1 = basicUserService.create(userCreateRequest1, createRequest);

    assertThat(userDto1.username()).isEqualTo("Yang1");
  }
  @Test
  void UserService_찾기_테스트(){

    UserCreateRequest userCreateRequest1 = new UserCreateRequest("Yang1", "yang1@naver.com", "123456");
    Optional<BinaryContentCreateRequest> createRequest = Optional.empty();
    UserDto userDto1 = basicUserService.create(userCreateRequest1, createRequest);

    assertThat(userDto1.username()).isEqualTo("Yang1");

    assertThat(basicUserService.find(userDto1.id()).email()).isEqualTo("yang1@naver.com");
  }
  @Test
  void UserService_업데이트_테스트(){

    UserCreateRequest userCreateRequest1 = new UserCreateRequest("Yang1", "yang1@naver.com", "123456");
    Optional<BinaryContentCreateRequest> createRequest = Optional.empty();
    UserDto userDto1 = basicUserService.create(userCreateRequest1, createRequest);
    assertThat(userDto1.username()).isEqualTo("Yang1");

    UserUpdateRequest request = new UserUpdateRequest("Yang2", "yang2@naver.com", "123456");
    basicUserService.update(userDto1.id(), request, createRequest);

    assertThat(basicUserService.find(userDto1.id()).email()).isEqualTo("yang2@naver.com");
    assertThat(userDto1.online()).isTrue();
  }
  @Test
  void UserService_삭제_테스트(){

    UserCreateRequest userCreateRequest1 = new UserCreateRequest("Yang1", "yang1@naver.com", "123456");
    Optional<BinaryContentCreateRequest> createRequest = Optional.empty();
    UserDto userDto1 = basicUserService.create(userCreateRequest1, createRequest);
    assertThat(userDto1.username()).isEqualTo("Yang1");
    assertThat(basicUserService.find(userDto1.id()).email()).isEqualTo("yang1@naver.com");
    //
    basicUserService.delete(userDto1.id());
    //
    assertThrows(UserNotFoundException.class, ()-> basicUserService.find(userDto1.id()));
  }
}