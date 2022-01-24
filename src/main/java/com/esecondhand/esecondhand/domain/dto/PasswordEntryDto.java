package com.esecondhand.esecondhand.domain.dto;


import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PasswordEntryDto {
    private String oldPassword;
    private String newPassword;
}
