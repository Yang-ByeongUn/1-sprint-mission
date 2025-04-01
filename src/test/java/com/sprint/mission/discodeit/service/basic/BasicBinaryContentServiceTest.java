package com.sprint.mission.discodeit.service.basic;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.exception.binaryContnent.BinaryContentNotFoundException;
import com.sprint.mission.discodeit.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class BasicBinaryContentServiceTest {
  @Autowired
  private BasicBinaryContentService basicBinaryContentService;
  @Autowired
  BinaryContentMapper binaryContentMapper;
  @Autowired
  private BinaryContentStorage binaryContentStorage;
  @Autowired
  private BinaryContentService binaryContentService;

  @Test
  void BinaryContent_생성_테스트(){
    String fileName = "test.png";
    byte[] bytes = new byte[]{1, 2, 3};
    String contentType = "image/png";

    BinaryContentCreateRequest request = new BinaryContentCreateRequest(fileName, contentType, bytes);
    BinaryContent binaryContent = new BinaryContent(fileName, (long) bytes.length, contentType);

    BinaryContentDto result = basicBinaryContentService.create(request);
    assertThat(result.size()).isEqualTo(binaryContentMapper.toDto(binaryContent).size());
  }
  @Test
  void BinaryContent_찾기_테스트(){
    String fileName = "test.png";
    byte[] bytes = new byte[]{1, 2, 3};
    String contentType = "image/png";

    BinaryContentCreateRequest request = new BinaryContentCreateRequest(fileName, contentType, bytes);
    BinaryContent binaryContent = new BinaryContent(fileName, (long) bytes.length, contentType);
    BinaryContentDto binaryContentDto = binaryContentMapper.toDto(binaryContent);

    BinaryContentDto result = basicBinaryContentService.create(request);
    assertThat(basicBinaryContentService.find(result.id()).size()).isEqualTo(binaryContentDto.size());
  }
  @Test
  void BinaryContent_삭제_테스트(){
    String fileName = "test.png";
    byte[] bytes = new byte[]{1, 2, 3};
    String contentType = "image/png";

    BinaryContentCreateRequest request = new BinaryContentCreateRequest(fileName, contentType, bytes);
    BinaryContent binaryContent = new BinaryContent(fileName, (long) bytes.length, contentType);
    BinaryContentDto binaryContentDto = binaryContentMapper.toDto(binaryContent);

    BinaryContentDto result = basicBinaryContentService.create(request);
    assertThat(basicBinaryContentService.find(result.id()).size()).isEqualTo(binaryContentDto.size());

    binaryContentService.delete(result.id());
    assertThrows(BinaryContentNotFoundException.class, () -> {
      basicBinaryContentService.find(result.id());
    });


  }

}