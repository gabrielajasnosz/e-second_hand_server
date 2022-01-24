package com.esecondhand.esecondhand.domain.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "follower")
public class Follower {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "follower_id", referencedColumnName = "id")
    private User follower;

    @ManyToOne
    @JoinColumn(name = "following_id", referencedColumnName = "id")
    private User following;
}
