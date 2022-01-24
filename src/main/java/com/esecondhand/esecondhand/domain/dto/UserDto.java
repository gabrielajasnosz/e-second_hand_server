package com.esecondhand.esecondhand.domain.dto;

import lombok.*;

import java.time.LocalDateTime;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
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
    private Double rating;
    private boolean isFollowedByUser;
    private Long chatWithUserId;
}
