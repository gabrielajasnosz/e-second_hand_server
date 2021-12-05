package com.esecondhand.esecondhand.domain.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PasswordEntryDto {
    private String oldPassword;
    private String newPassword;
}
