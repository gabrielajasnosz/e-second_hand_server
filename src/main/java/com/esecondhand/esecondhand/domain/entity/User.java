package com.esecondhand.esecondhand.domain.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String displayName;
    private String phoneNumber;
    private String city;
    private String zipCode;
    private LocalDateTime creationDate;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    private String password;
    private boolean enabled;
    private String profilePictureLocation;
}
