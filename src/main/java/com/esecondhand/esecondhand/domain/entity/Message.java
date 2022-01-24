package com.esecondhand.esecondhand.domain.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "author_id", referencedColumnName = "id")
    private User author;

    @OneToOne
    @JoinColumn(name = "chat_id", referencedColumnName = "id")
    private Chat chat;

    private String content;

    private LocalDateTime creationDate;

    private boolean isSeen;

}
