package com.esecondhand.esecondhand.domain.dto;

import com.esecondhand.esecondhand.service.serviceImpl.ValidEmail;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
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
    private Date creationDate;



}
