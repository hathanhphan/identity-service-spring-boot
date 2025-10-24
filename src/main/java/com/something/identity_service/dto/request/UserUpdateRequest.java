package com.something.identity_service.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class UserUpdateRequest {
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
}
