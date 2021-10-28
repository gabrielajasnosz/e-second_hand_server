package com.esecondhand.esecondhand.domain.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "item")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @ManyToOne
    @JoinColumn(name="user_id", referencedColumnName = "id")
    @JsonIgnore
    private User user;
    private String description;

    @ManyToOne
    @JoinColumn(name="category_id", referencedColumnName = "id")
    @JsonIgnore
    private Category category;

    @ManyToOne
    @JoinColumn(name="brand_id", referencedColumnName = "id")
    @JsonIgnore
    private Brand brand;

    @ManyToOne
    @JoinColumn(name="color_id", referencedColumnName = "id")
    @JsonIgnore
    private Color color;
    private Double price;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name="size_id", referencedColumnName = "id")
    private Size size;

    @Enumerated(EnumType.STRING)
    private Gender gender;
    private Date creationDate;
}
