package com.esecondhand.esecondhand.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "saved_filter")
public class SavedFilter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "brand_id", referencedColumnName = "id")
    private Brand brand;

    @ManyToOne
    @JoinColumn(name = "color_id", referencedColumnName = "id")
    private Color color;

    @ManyToOne
    @JoinColumn(name = "size_id", referencedColumnName = "id")
    private Size size;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    private Double maxPrice;

    private Double minPrice;

    private String sortingColumn;

    private String sortingOrder;

    private String name;

    @Enumerated(EnumType.STRING)
    private Gender gender;
}
