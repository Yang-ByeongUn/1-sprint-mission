package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.channel.DuplicatedChannelNameException;
import com.sprint.mission.discodeit.exception.channel.PrivateChannelUpdateException;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class BasicChannelService implements ChannelService {

  private final ChannelRepository channelRepository;
  //
  private final ReadStatusRepository readStatusRepository;
  private final MessageRepository messageRepository;
  private final UserRepository userRepository;
  private final ChannelMapper channelMapper;

  @Transactional
  @Override
  public ChannelDto create(PublicChannelCreateRequest request) {
    String name = request.name();
    String description = request.description();
    List<Channel> findByName = channelRepository.findByName(name);
    if (!findByName.isEmpty()) {
      List<Channel> byName = channelRepository.findByName(name);
      System.out.println("byName = " + byName);
      throw new DuplicatedChannelNameException(name);
    }
    Channel channel = new Channel(ChannelType.PUBLIC, name, description);

    channelRepository.save(channel);
    return channelMapper.toDto(channel);
  }

  @Transactional
  @Override
  public ChannelDto create(PrivateChannelCreateRequest request) {
    Channel channel = new Channel(ChannelType.PRIVATE, null, null);
    channelRepository.save(channel);

    List<ReadStatus> readStatuses = userRepository.findAllById(request.participantIds()).stream()
        .map(user -> new ReadStatus(user, channel, channel.getCreatedAt()))
        .toList();
    System.out.println("readStatuses = " + readStatuses);
    readStatusRepository.saveAll(readStatuses);

    return channelMapper.toDto(channel);
  }

  @Transactional(readOnly = true)
  @Override
  public ChannelDto find(UUID channelId) {
    return channelRepository.findById(channelId)
        .map(channelMapper::toDto)
        .orElseThrow(
            //() -> new NoSuchElementException("Channel with id " + channelId + " not found"));
            () -> new ChannelNotFoundException(channelId));
  }

  @Transactional(readOnly = true)
  @Override
  public List<ChannelDto> findAllByUserId(UUID userId) {
    List<UUID> mySubscribedChannelIds = readStatusRepository.findAllByUserId(userId).stream()
        .map(ReadStatus::getChannel)
        .map(Channel::getId)
        .toList();

    return channelRepository.findAllByTypeOrIdIn(ChannelType.PUBLIC, mySubscribedChannelIds)
        .stream()
        .map(channelMapper::toDto)
        .toList();
  }

  @Transactional
  @Override
  public ChannelDto update(UUID channelId, PublicChannelUpdateRequest request) {
    String newName = request.newName();
    String newDescription = request.newDescription();
    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(
            //() -> new NoSuchElementException("Channel with id " + channelId + " not found"));
            () -> new ChannelNotFoundException(channelId));
    if (channel.getType().equals(ChannelType.PRIVATE)) {
      //throw new IllegalArgumentException("Private channel cannot be updated");
      throw new PrivateChannelUpdateException(channelId);
    }
    channel.update(newName, newDescription);
    return channelMapper.toDto(channel);
  }

  @Transactional
  @Override
  public void delete(UUID channelId) {
    if (!channelRepository.existsById(channelId)) {
      throw new ChannelNotFoundException(channelId);
    }

    messageRepository.deleteAllByChannelId(channelId);
    readStatusRepository.deleteAllByChannelId(channelId);

    channelRepository.deleteById(channelId);
  }
}
