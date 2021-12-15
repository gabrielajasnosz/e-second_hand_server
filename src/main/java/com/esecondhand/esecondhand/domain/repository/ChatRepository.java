package com.esecondhand.esecondhand.domain.repository;

import com.esecondhand.esecondhand.domain.entity.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {

    @Query("SELECT c FROM Chat c WHERE (select count(cpp) from ChatParticipant cpp where (cpp.participant.id=?1 or cpp.participant.id=?2) and cpp.chat.id=c.id)=2")
    Chat findChat(Long userId, Long id);
}
