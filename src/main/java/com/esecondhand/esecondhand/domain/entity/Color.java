package com.esecondhand.esecondhand.domain.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Builder
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "color")
public class Color {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String hexCode;

}

