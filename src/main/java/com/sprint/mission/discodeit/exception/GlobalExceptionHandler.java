package com.sprint.mission.discodeit.exception;

import com.sprint.mission.discodeit.exception.binaryContnent.BinaryContentNotFoundException;
import com.sprint.mission.discodeit.exception.binaryContnent.BinaryContentSameFileExistsException;
import com.sprint.mission.discodeit.exception.binaryContnent.BinaryContentStorageFileNotFoundException;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.channel.DuplicatedChannelNameException;
import com.sprint.mission.discodeit.exception.channel.DuplicatedPrivateChannelException;
import com.sprint.mission.discodeit.exception.channel.PrivateChannelUpdateException;
import com.sprint.mission.discodeit.exception.message.MessageNotFoundException;
import com.sprint.mission.discodeit.exception.readStatus.ReadStatusCreateFiledException;
import com.sprint.mission.discodeit.exception.readStatus.ReadStatusNotFoundException;
import com.sprint.mission.discodeit.exception.user.UserAlreadyExistsException;
import com.sprint.mission.discodeit.exception.user.UserCreateFailedException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.exception.userStatus.UserStatusCreateFailedException;
import com.sprint.mission.discodeit.exception.userStatus.UserStatusNotFoundException;
import java.util.NoSuchElementException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  //전체 예외
  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<String> handleException(IllegalArgumentException e) {
    log.warn("잘못된 요청 처리 중 예외 발생: {}", e.getMessage(), e);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<String> handleException(MethodArgumentNotValidException e) {
    log.warn("유효성 검사에서 예외 발생: {}", e.getMessage(), e);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
  }

  @ExceptionHandler(NoSuchElementException.class)
  public ResponseEntity<String> handleException(NoSuchElementException e) {
    log.warn("데이터를 찾을 수 없습니다: {}", e.getMessage(), e);
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<String> handleException(Exception e) {
    log.error("서버 내부 오류 발생: {}", e.getMessage(), e);
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("알 수 없는 오류가 발생했습니다.");
  }

  //User 예외
  @ExceptionHandler(UserNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleException(UserNotFoundException e) {
    ErrorResponse runtimeException = new ErrorResponse(e.getTimeStamp(), ErrorCode.USER_NOT_FOUND.toString(),
        e.getMessage(), e.getDetails(), "RuntimeException", HttpStatus.NOT_FOUND.value());
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(runtimeException);
  }

  @ExceptionHandler(UserCreateFailedException.class)
  public ResponseEntity<ErrorResponse> handleException(UserCreateFailedException e) {
    ErrorResponse runtimeException = new ErrorResponse(e.getTimeStamp(), ErrorCode.USER_CREATE_FAILED.toString(),
        e.getMessage(), e.getDetails(), "RuntimeException", HttpStatus.BAD_REQUEST.value());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(runtimeException);
  }

  @ExceptionHandler(UserAlreadyExistsException.class)
  public ResponseEntity<ErrorResponse> handleException(UserAlreadyExistsException e) {
    ErrorResponse runtimeException = new ErrorResponse(e.getTimeStamp(), ErrorCode.USER_ALREADY_EXISTS.toString(),
        e.getMessage(), e.getDetails(), "RuntimeException", HttpStatus.BAD_REQUEST.value());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(runtimeException);
  }

  //userStatus 예외
  @ExceptionHandler(UserStatusNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleException(UserStatusNotFoundException e) {
    ErrorResponse runtimeException = new ErrorResponse(e.getTimeStamp(), ErrorCode.USER_STATUS_NOT_FOUND.toString(),
        e.getMessage(), e.getDetails(), "RuntimeException", HttpStatus.NOT_FOUND.value());
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(runtimeException);
  }

  @ExceptionHandler(UserStatusCreateFailedException.class)
  public ResponseEntity<ErrorResponse> handleException(UserStatusCreateFailedException e) {
    ErrorResponse runtimeException = new ErrorResponse(e.getTimeStamp(), ErrorCode.USER_STATUS_CREATE_FAILED.toString(),
        e.getMessage(), e.getDetails(), "RuntimeException", HttpStatus.INTERNAL_SERVER_ERROR.value());
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(runtimeException);
  }

  //readStatus 에외
  @ExceptionHandler(ReadStatusNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleException(ReadStatusNotFoundException e) {
    ErrorResponse runtimeException = new ErrorResponse(e.getTimeStamp(), ErrorCode.READ_STATUS_NOT_FOUND.toString(),
        e.getMessage(), e.getDetails(), "RuntimeException", HttpStatus.NOT_FOUND.value());
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(runtimeException);
  }

  @ExceptionHandler(ReadStatusCreateFiledException.class)
  public ResponseEntity<ErrorResponse> handleException(ReadStatusCreateFiledException e) {
    ErrorResponse runtimeException = new ErrorResponse(e.getTimeStamp(), ErrorCode.READ_STATUS_CREATE_FAILED.toString(),
        e.getMessage(), e.getDetails(), "RuntimeException", HttpStatus.INTERNAL_SERVER_ERROR.value());
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(runtimeException);
  }

  //Message 에외
  @ExceptionHandler(MessageNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleException(MessageNotFoundException e) {
    ErrorResponse runtimeException = new ErrorResponse(e.getTimeStamp(), ErrorCode.MESSAGE_NOT_FOUND.toString(),
        e.getMessage(), e.getDetails(), "RuntimeException", HttpStatus.NOT_FOUND.value());
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(runtimeException);
  }

  //Channel예외
  @ExceptionHandler(ChannelNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleException(ChannelNotFoundException e) {
    ErrorResponse runtimeException = new ErrorResponse(e.getTimeStamp(), ErrorCode.CHANNEL_NOT_FOUND.toString(),
        e.getMessage(), e.getDetails(), "RuntimeException", HttpStatus.NOT_FOUND.value());
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(runtimeException);
  }

  @ExceptionHandler(DuplicatedChannelNameException.class)
  public ResponseEntity<ErrorResponse> handleException(DuplicatedChannelNameException e) {
    ErrorResponse runtimeException = new ErrorResponse(e.getTimeStamp(), ErrorCode.DUPLICATED_CHANNEL_NAME.toString(),
        e.getMessage(), e.getDetails(), "RuntimeException", HttpStatus.CONFLICT.value());
    return ResponseEntity.status(HttpStatus.CONFLICT).body(runtimeException);
  }

  @ExceptionHandler(DuplicatedPrivateChannelException.class)
  public ResponseEntity<ErrorResponse> handleException(DuplicatedPrivateChannelException e) {
    ErrorResponse runtimeException = new ErrorResponse(e.getTimeStamp(),
        ErrorCode.DUPLICATED_PRIVATE_CHANNEL_USERS.toString(), e.getMessage(), e.getDetails(), "RuntimeException",
        HttpStatus.CONFLICT.value());
    return ResponseEntity.status(HttpStatus.CONFLICT).body(runtimeException);
  }

  @ExceptionHandler(PrivateChannelUpdateException.class)
  public ResponseEntity<ErrorResponse> handleException(PrivateChannelUpdateException e) {
    ErrorResponse runtimeException = new ErrorResponse(e.getTimeStamp(), ErrorCode.PRIVATE_CHANNEL_UPDATE.toString(),
        e.getMessage(), e.getDetails(), "RuntimeException", HttpStatus.BAD_REQUEST.value());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(runtimeException);
  }

  //BinaryContent 예외
  @ExceptionHandler(BinaryContentNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleException(BinaryContentNotFoundException e) {
    ErrorResponse runtimeException = new ErrorResponse(e.getTimeStamp(), ErrorCode.BINARY_CONTENT_NOT_FOUND.toString(),
        e.getMessage(), e.getDetails(), "RuntimeException", HttpStatus.NOT_FOUND.value());
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(runtimeException);
  }

  @ExceptionHandler(BinaryContentStorageFileNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleException(BinaryContentStorageFileNotFoundException e) {
    ErrorResponse runtimeException = new ErrorResponse(e.getTimeStamp(),
        ErrorCode.BINARY_CONTENT_STORAGE_FILE_NOT_FOUND.toString(), e.getMessage(), e.getDetails(), "RuntimeException",
        HttpStatus.NOT_FOUND.value());
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(runtimeException);
  }

  @ExceptionHandler(BinaryContentSameFileExistsException.class)
  public ResponseEntity<ErrorResponse> handleException(BinaryContentSameFileExistsException e) {
    ErrorResponse runtimeException = new ErrorResponse(e.getTimeStamp(),
        ErrorCode.BINARY_CONTENT_SAME_FILE_EXISTS.toString(), e.getMessage(), e.getDetails(), "RuntimeException",
        HttpStatus.CONFLICT.value());
    return ResponseEntity.status(HttpStatus.CONFLICT).body(runtimeException);
  }
}
