package com.esecondhand.esecondhand.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;


@Entity
@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class ItemPicture {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="item_id", referencedColumnName = "id")
    @JsonIgnore
    private Item item;
    private LocalDateTime creationDate;
    private String fileUrl;
    private boolean isMainPicture;

}
