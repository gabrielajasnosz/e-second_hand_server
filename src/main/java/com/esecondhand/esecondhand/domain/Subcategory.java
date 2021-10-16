package com.esecondhand.esecondhand.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "subcategory")
public class Subcategory {
    @Id
    private int id;
    private String name;
    private int mainCategoryId;

}
