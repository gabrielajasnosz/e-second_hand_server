package com.esecondhand.esecondhand.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private Long id;
    private String email;
    private String displayName;
    private String phoneNumber;
    private String city;
    private String zipCode;
    private LocalDateTime creationDate;
    private String gender;
    private String profilePictureLocation;
}
