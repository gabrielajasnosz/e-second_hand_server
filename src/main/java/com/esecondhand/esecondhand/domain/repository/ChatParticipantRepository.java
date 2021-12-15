package com.esecondhand.esecondhand.domain.repository;

import com.esecondhand.esecondhand.domain.entity.Chat;
import com.esecondhand.esecondhand.domain.entity.ChatParticipant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatParticipantRepository extends JpaRepository<ChatParticipant, Long> {
    List<ChatParticipant> findAllByParticipantId(Long id);

    @Query("SELECT c.chat.id FROM ChatParticipant c WHERE c.participant.id != ?1")
    List<Long> findUserChatIds(Long userId);

    @Query("SELECT c.chat.id FROM ChatParticipant c WHERE c.participant.id = ?1")
    List<Long> findUserChats(Long userId);

    @Query("SELECT c FROM ChatParticipant c WHERE c.chat.id = ?2 and c.participant.id != ?1")
    ChatParticipant findChatParticipant(Long userId, Long chatId);
}
