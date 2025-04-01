package com.sprint.mission.discodeit.service.basic;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.dto.data.MessageDto;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import com.sprint.mission.discodeit.exception.message.MessageNotFoundException;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.UserService;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
@Rollback
class BasicMessageServiceTest {

  @Autowired
  BasicMessageService basicMessageService;
  @Autowired
  BasicChannelService basicChannelService;
  @Autowired
  BasicUserService basicUserService;

  @Test
  void Message_생성_테스트(){
    PublicChannelCreateRequest request = new PublicChannelCreateRequest("A_Channel2", "A채널 입니다.");
    ChannelDto channelDto = basicChannelService.create(request);
    assertThat(channelDto.name()).isEqualTo("A_Channel2");

    UserCreateRequest userCreateRequest1 = new UserCreateRequest("Yang1", "yang1@naver.com", "123456");
    Optional<BinaryContentCreateRequest> createRequest = Optional.empty();
    UserDto userDto1 = basicUserService.create(userCreateRequest1, createRequest);

    MessageCreateRequest messageCreateRequest = new MessageCreateRequest("안녕", channelDto.id(), userDto1.id());
    List<BinaryContentCreateRequest> binaryContentCreateRequests = new ArrayList<>();
    //
    MessageDto requestMessageDto = basicMessageService.create(messageCreateRequest, binaryContentCreateRequests);
    MessageDto replyMessageDto = basicMessageService.find(requestMessageDto.id());
    //
    assertThat(replyMessageDto.channelId()).isEqualTo(channelDto.id());
  }
  @Test
  void Message_찾기_테스트() throws InterruptedException {
    PublicChannelCreateRequest request = new PublicChannelCreateRequest("A_Channel2", "A채널 입니다.");
    ChannelDto channelDto = basicChannelService.create(request);
    assertThat(channelDto.name()).isEqualTo("A_Channel2");

    UserCreateRequest userCreateRequest1 = new UserCreateRequest("Yang1", "yang1@naver.com", "123456");
    Optional<BinaryContentCreateRequest> createRequest = Optional.empty();
    UserDto userDto1 = basicUserService.create(userCreateRequest1, createRequest);

    UserCreateRequest userCreateRequest2 = new UserCreateRequest("Yang2", "yang2@naver.com", "123456");
    Optional<BinaryContentCreateRequest> createRequest2 = Optional.empty();
    UserDto userDto2 = basicUserService.create(userCreateRequest2, createRequest2);

    MessageCreateRequest messageCreateRequest1 = new MessageCreateRequest("안녕1", channelDto.id(), userDto1.id());
    MessageCreateRequest messageCreateRequest2 = new MessageCreateRequest("안녕1", channelDto.id(), userDto2.id());
    MessageCreateRequest messageCreateRequest3 = new MessageCreateRequest("안녕2", channelDto.id(), userDto1.id());
    List<BinaryContentCreateRequest> binaryContentCreateRequests = new ArrayList<>();
    //
    MessageDto requestMessageDto1 = basicMessageService.create(messageCreateRequest1, binaryContentCreateRequests);
    MessageDto requestMessageDto2 = basicMessageService.create(messageCreateRequest2, binaryContentCreateRequests);
    MessageDto requestMessageDto3 = basicMessageService.create(messageCreateRequest3, binaryContentCreateRequests);
    Pageable pageable = PageRequest.of(0, 10);
    //
    Thread.sleep(5000); //바로 찾으면 현재 시각 기준으로도 찾기에 못 찾음
    //
    PageResponse<MessageDto> pageResponse = basicMessageService.findAllByChannelId(channelDto.id(), null, pageable);
    pageResponse.content().forEach(System.out::println);
    //
    assertThat(pageResponse.content().size()).isEqualTo(3);
  }
  @Test
  void Message_업데이트_테스트() throws InterruptedException {
    PublicChannelCreateRequest request = new PublicChannelCreateRequest("A_Channel2", "A채널 입니다.");
    ChannelDto channelDto = basicChannelService.create(request);
    assertThat(channelDto.name()).isEqualTo("A_Channel2");

    UserCreateRequest userCreateRequest1 = new UserCreateRequest("Yang1", "yang1@naver.com", "123456");
    Optional<BinaryContentCreateRequest> createRequest = Optional.empty();
    UserDto userDto1 = basicUserService.create(userCreateRequest1, createRequest);

    MessageCreateRequest messageCreateRequest1 = new MessageCreateRequest("안녕1", channelDto.id(), userDto1.id());
    List<BinaryContentCreateRequest> binaryContentCreateRequests = new ArrayList<>();
    //
    MessageDto requestMessageDto1 = basicMessageService.create(messageCreateRequest1, binaryContentCreateRequests);
    MessageDto messageDto = basicMessageService.find(requestMessageDto1.id());
    //
    assertThat(messageDto.channelId()).isEqualTo(channelDto.id());
    //
    MessageUpdateRequest messageUpdateRequest = new MessageUpdateRequest("안녕2");
    MessageDto update = basicMessageService.update(messageDto.id(), messageUpdateRequest);
    MessageDto updatedMessageDto = basicMessageService.find(requestMessageDto1.id());
    //
    assertThat(update.content()).isEqualTo("안녕2");
    assertThat(updatedMessageDto.content()).isEqualTo("안녕2");
  }
  @Test
  void Message_삭제_테스트(){
    PublicChannelCreateRequest request = new PublicChannelCreateRequest("A_Channel2", "A채널 입니다.");
    ChannelDto channelDto = basicChannelService.create(request);
    assertThat(channelDto.name()).isEqualTo("A_Channel2");

    UserCreateRequest userCreateRequest1 = new UserCreateRequest("Yang1", "yang1@naver.com", "123456");
    Optional<BinaryContentCreateRequest> createRequest = Optional.empty();
    UserDto userDto1 = basicUserService.create(userCreateRequest1, createRequest);

    MessageCreateRequest messageCreateRequest1 = new MessageCreateRequest("안녕1", channelDto.id(), userDto1.id());
    List<BinaryContentCreateRequest> binaryContentCreateRequests = new ArrayList<>();
    //
    MessageDto requestMessageDto1 = basicMessageService.create(messageCreateRequest1, binaryContentCreateRequests);
    MessageDto messageDto = basicMessageService.find(requestMessageDto1.id());
    //
    assertThat(messageDto.channelId()).isEqualTo(channelDto.id());
    //
    basicMessageService.delete(messageDto.id());
    //
    assertThrows(MessageNotFoundException.class, ()->basicMessageService.find(requestMessageDto1.id()));
  }
}