package com.esecondhand.esecondhand.domain.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;

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
    private String role;
    private String city;
    private String zipCode;
    private LocalDateTime creationDate;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    private String password;
    private boolean enabled;
    private String profilePictureLocation;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return isEnabled() == user.isEnabled() &&
                Objects.equals(getId(), user.getId()) &&
                Objects.equals(getEmail(), user.getEmail()) &&
                Objects.equals(getDisplayName(), user.getDisplayName()) &&
                Objects.equals(getPhoneNumber(), user.getPhoneNumber()) &&
                Objects.equals(getRole(), user.getRole()) &&
                Objects.equals(getCity(), user.getCity()) &&
                Objects.equals(getZipCode(), user.getZipCode()) &&
                Objects.equals(getCreationDate(), user.getCreationDate()) &&
                getGender() == user.getGender() &&
                Objects.equals(getPassword(), user.getPassword()) &&
                Objects.equals(getProfilePictureLocation(), user.getProfilePictureLocation());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getEmail(), getDisplayName(), getPhoneNumber(), getRole(), getCity(), getZipCode(), getCreationDate(), getGender(), getPassword(), isEnabled(), getProfilePictureLocation());
    }
}
