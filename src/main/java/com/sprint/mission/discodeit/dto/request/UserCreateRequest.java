package com.sprint.mission.discodeit.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

@NotNull
public record UserCreateRequest(
    String username,
    @Email
    String email,
    String password
) {

}
