package com.sprint.mission.discodeit.exception;

import lombok.ToString;


public enum ErrorCode {
  //CHANNEL
  CHANNEL_NOT_FOUND("Channel not found"),
  PRIVATE_CHANNEL_UPDATE("Private channel can't update"), //PRIVATE채널은 업데이트 불가
  DUPLICATED_CHANNEL_NAME("Duplicated channel name"),
  DUPLICATED_PRIVATE_CHANNEL_USERS("Duplicated private channel users"),
  //MESSAGE
  MESSAGE_NOT_FOUND("Message not found"),
  //BINARY_CONTENT
  BINARY_CONTENT_NOT_FOUND("Binary content not found"),
  BINARY_CONTENT_STORAGE_FILE_NOT_FOUND("Binary Content Storage File Not Found"),
  BINARY_CONTENT_SAME_FILE_EXISTS("BinaryContent same file exists"),
  //READ_STATUS
  READ_STATUS_NOT_FOUND("ReadStatus not found"),
  READ_STATUS_CREATE_FAILED("Failed create ReadStatue"),
  //USER
  USER_NOT_FOUND("User not found"),
  USER_CREATE_FAILED("User create failed"),
  USER_ALREADY_EXISTS("User already exists"),
  //USER_STATUS
  USER_STATUS_NOT_FOUND("UserStatus not found"),
  USER_STATUS_CREATE_FAILED("Failed create UserStatus ");

  private final String message;

  ErrorCode(String message) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }
}
