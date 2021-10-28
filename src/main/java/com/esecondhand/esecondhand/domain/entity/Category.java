package com.esecondhand.esecondhand.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Category {
    @Id
    private Long id;
    private String name;
    @Enumerated(EnumType.STRING)
    private Gender gender;


    @ManyToOne
    @JoinColumn(name="parent_id")
    private Category parentId;

    @OneToMany(mappedBy="parentId")
    @JsonIgnore
    private List<Category> subCategories=new ArrayList<>();


}

