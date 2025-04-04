package com.sprint.mission.discodeit.service.basic;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.LoginRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import jakarta.transaction.Transactional;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@Transactional
class BasicAuthServiceTest {

  @Mock
  private UserRepository userRepository;

  @Mock
  private UserMapper userMapper;

  @InjectMocks
  private BasicAuthService basicAuthService;

  @Test
  void 로그인_성공_테스트() {
    // given
    LoginRequest loginRequest = new LoginRequest("Yang", "123456");

    User user = new User("Yang","ypd06426@naver.com", "123456", null);

    UserDto userDto = new UserDto(UUID.randomUUID(), "Yang", "ypd06426@naver.com", null, true);

    when(userRepository.findByUsername("Yang")).thenReturn(Optional.of(user));
    when(userMapper.toDto(user)).thenReturn(userDto);

    // when
    UserDto result = basicAuthService.login(loginRequest);

    // then
    assertThat(result).isEqualTo(userDto);
  }

  @Test
  void 로그인_실패_유저없음_예외발생() {
    // given
    LoginRequest loginRequest = new LoginRequest("Unknown", "password");

    when(userRepository.findByUsername("Unknown")).thenReturn(Optional.empty());

    // when & then
    assertThrows(NoSuchElementException.class, () -> {
      basicAuthService.login(loginRequest);
    });
  }

  @Test
  void 로그인_실패_비밀번호_불일치() {
    // given
    LoginRequest loginRequest = new LoginRequest("Yang", "wrongpass");

    User user = new User("Yang","ypd06426@naver.com", "123456", null);

    when(userRepository.findByUsername("Yang")).thenReturn(Optional.of(user));

    // when & then
    assertThrows(IllegalArgumentException.class, () -> {
      basicAuthService.login(loginRequest);
    });
  }
}
