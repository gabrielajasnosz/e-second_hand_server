package com.esecondhand.esecondhand.domain.dto;

import com.esecondhand.esecondhand.service.serviceImpl.ValidEmail;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterDto {


    @NotNull
    @NotEmpty
    private String password;

    @NotNull
    @NotEmpty
    private String displayName;

    @ValidEmail
    @NotNull
    @NotEmpty
    private String email;

    @NotNull
    @NotEmpty
    private String sex;

    @NotNull
    @NotEmpty
    private LocalDateTime creationDate;



}
