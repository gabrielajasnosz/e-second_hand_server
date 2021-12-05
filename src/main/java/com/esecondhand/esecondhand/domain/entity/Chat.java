package com.esecondhand.esecondhand.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "first_participant_id", referencedColumnName = "id")
    private User firstParticipant;

    @ManyToOne
    @JoinColumn(name = "second_participant_id", referencedColumnName = "id")
    private User secondParticipant;

}
