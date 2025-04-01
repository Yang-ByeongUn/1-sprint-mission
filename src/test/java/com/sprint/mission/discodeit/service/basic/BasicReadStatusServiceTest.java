package com.sprint.mission.discodeit.service.basic;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.dto.data.MessageDto;
import com.sprint.mission.discodeit.dto.data.ReadStatusDto;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.request.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.exception.readStatus.ReadStatusNotFoundException;
import com.sprint.mission.discodeit.service.ReadStatusService;
import jakarta.transaction.Transactional;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
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
class BasicReadStatusServiceTest {
  @Autowired
  private BasicReadStatusService basicReadStatusService;
  @Autowired
  BasicMessageService basicMessageService;
  @Autowired
  BasicChannelService basicChannelService;
  @Autowired
  BasicUserService basicUserService;
  @Autowired
  private ReadStatusService readStatusService;

  @Test
  void ReadStatus_생성_테스트(){

    PublicChannelCreateRequest request = new PublicChannelCreateRequest("A_Channel2", "A채널 입니다.");
    ChannelDto channelDto = basicChannelService.create(request);
    assertThat(channelDto.name()).isEqualTo("A_Channel2");

    UserCreateRequest userCreateRequest1 = new UserCreateRequest("Yang1", "yang1@naver.com", "123456");
    Optional<BinaryContentCreateRequest> createRequest = Optional.empty();
    UserDto userDto1 = basicUserService.create(userCreateRequest1, createRequest);

    ReadStatusCreateRequest request1 = new ReadStatusCreateRequest(userDto1.id(), channelDto.id(), Instant.now());
    ReadStatusDto readStatusDto1 = readStatusService.create(request1);

    assertThat(readStatusDto1.userId().equals(userDto1.id())).isTrue();
  }
  @Test
  void ReadStatus_찾기_테스트(){

    PublicChannelCreateRequest request = new PublicChannelCreateRequest("A_Channel2", "A채널 입니다.");
    ChannelDto channelDto = basicChannelService.create(request);
    assertThat(channelDto.name()).isEqualTo("A_Channel2");

    UserCreateRequest userCreateRequest1 = new UserCreateRequest("Yang1", "yang1@naver.com", "123456");
    Optional<BinaryContentCreateRequest> createRequest = Optional.empty();
    UserDto userDto1 = basicUserService.create(userCreateRequest1, createRequest);

    ReadStatusCreateRequest request1 = new ReadStatusCreateRequest(userDto1.id(), channelDto.id(), Instant.now());
    ReadStatusDto readStatusDto1 = readStatusService.create(request1);

    assertThat(readStatusDto1.userId().equals(userDto1.id())).isTrue();
    //
    ReadStatusDto readStatusDto = readStatusService.find(readStatusDto1.id());
    assertThat(readStatusDto.userId().equals(userDto1.id())).isTrue();
  }
  @Test
  void ReadStatus_업데이트_테스트(){

    PublicChannelCreateRequest request = new PublicChannelCreateRequest("A_Channel2", "A채널 입니다.");
    ChannelDto channelDto = basicChannelService.create(request);
    assertThat(channelDto.name()).isEqualTo("A_Channel2");

    UserCreateRequest userCreateRequest1 = new UserCreateRequest("Yang1", "yang1@naver.com", "123456");
    Optional<BinaryContentCreateRequest> createRequest = Optional.empty();
    UserDto userDto1 = basicUserService.create(userCreateRequest1, createRequest);

    ReadStatusCreateRequest request1 = new ReadStatusCreateRequest(userDto1.id(), channelDto.id(), Instant.now());
    ReadStatusDto readStatusDto1 = readStatusService.create(request1);

    assertThat(readStatusDto1.userId().equals(userDto1.id())).isTrue();
    //
    Instant specificTime = Instant.parse("2024-12-25T15:00:00Z");
    ReadStatusUpdateRequest updateRequest = new ReadStatusUpdateRequest(specificTime);
    readStatusService.update(readStatusDto1.id(), updateRequest);
    ReadStatusDto readStatusDto = readStatusService.find(readStatusDto1.id());
    //
    assertThat(readStatusDto.lastReadAt().equals(specificTime)).isTrue();
  }
  @Test
  void ReadStatus_삭제_테스트(){

    PublicChannelCreateRequest request = new PublicChannelCreateRequest("A_Channel2", "A채널 입니다.");
    ChannelDto channelDto = basicChannelService.create(request);
    assertThat(channelDto.name()).isEqualTo("A_Channel2");

    UserCreateRequest userCreateRequest1 = new UserCreateRequest("Yang1", "yang1@naver.com", "123456");
    Optional<BinaryContentCreateRequest> createRequest = Optional.empty();
    UserDto userDto1 = basicUserService.create(userCreateRequest1, createRequest);

    ReadStatusCreateRequest request1 = new ReadStatusCreateRequest(userDto1.id(), channelDto.id(), Instant.now());
    ReadStatusDto readStatusDto1 = readStatusService.create(request1);

    assertThat(readStatusDto1.userId().equals(userDto1.id())).isTrue();
    //
    readStatusService.delete(readStatusDto1.id());
    //
    assertThrows(ReadStatusNotFoundException.class, ()-> readStatusService.find(readStatusDto1.id()));
  }

}