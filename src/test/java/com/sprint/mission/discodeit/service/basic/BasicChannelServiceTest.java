package com.sprint.mission.discodeit.service.basic;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.binaryContnent.BinaryContentNotFoundException;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.UserService;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
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
class BasicChannelServiceTest {

  @Autowired
  BasicChannelService basicChannelService;
  @Autowired
  UserService userService;
  @Autowired
  private UserRepository userRepository;
  @Autowired
  private ChannelRepository channelRepository;
  @Autowired
  private ChannelService channelService;

  @Test
  void Channel_생성_테스트() {
    PublicChannelCreateRequest request = new PublicChannelCreateRequest("A_Channel2", "A채널 입니다.");
    ChannelDto channelDto = basicChannelService.create(request);
    assertThat(channelDto.name()).isEqualTo("A_Channel2");

    UserCreateRequest userCreateRequest1 = new UserCreateRequest("Yang1", "yang1@naver.com", "123456");
    UserCreateRequest userCreateRequest2 = new UserCreateRequest("Yang2", "yang2@naver.com", "123456");
    UserCreateRequest userCreateRequest3 = new UserCreateRequest("Yang3", "yang3@naver.com", "123456");
    Optional<BinaryContentCreateRequest> createRequest = Optional.empty();

    UserDto userDto1 = userService.create(userCreateRequest1, createRequest);
    UserDto userDto2 = userService.create(userCreateRequest2, createRequest);
    UserDto userDto3 = userService.create(userCreateRequest3, createRequest);

    List<UUID> list = new ArrayList<>();
    list.add(userDto1.id());
    list.add(userDto2.id());
    list.add(userDto3.id());

    PrivateChannelCreateRequest privateRequest = new PrivateChannelCreateRequest(list);
    ChannelDto privateChannelDto = basicChannelService.create(privateRequest);

    assertThat(privateChannelDto.participants().size()).isEqualTo(3);
  }

  @Test
  void Channel_찾기_테스트() {
    PublicChannelCreateRequest request = new PublicChannelCreateRequest("A_Channel2", "A채널 입니다.");
    ChannelDto channelDto = basicChannelService.create(request);
    assertThat(channelDto.name()).isEqualTo("A_Channel2");

    assertThat(channelRepository.findById(channelDto.id()).get().getDescription()).isEqualTo("A채널 입니다.");
  }

  @Test
  void Channel_업데이트_테스트() {
    PublicChannelCreateRequest request = new PublicChannelCreateRequest("A_Channel2", "A채널 입니다.");
    ChannelDto channelDto = basicChannelService.create(request);
    assertThat(channelDto.name()).isEqualTo("A_Channel2");

    PublicChannelUpdateRequest updateRequest = new PublicChannelUpdateRequest("B_channel", "B채널 입니다.");
    basicChannelService.update(channelDto.id(), updateRequest);

    ChannelDto updatedChannelDto = channelService.find(channelDto.id());
    assertThat(updatedChannelDto.name()).isEqualTo("B_channel");
    assertThat(updatedChannelDto.description()).isEqualTo("B채널 입니다.");

  }

  @Test
  void Channel_삭제_테스트() {
    PublicChannelCreateRequest request = new PublicChannelCreateRequest("A_Channel2", "A채널 입니다.");
    ChannelDto channelDto = basicChannelService.create(request);
    assertThat(channelDto.name()).isEqualTo("A_Channel2");

    basicChannelService.delete(channelDto.id());
    assertThrows(ChannelNotFoundException.class, () -> {
      basicChannelService.find(channelDto.id());
    });
  }

}