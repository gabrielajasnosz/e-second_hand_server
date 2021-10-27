package com.esecondhand.esecondhand.domain;


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
    private Long userId;
    private String description;
    private Long categoryId;
    private Long brandId;
    private Long colorId;
    private BigDecimal price;
    private Long sizeId;

    @Enumerated(EnumType.STRING)
    private Gender gender;
    private Date creationDate;
}
