package com.esecondhand.esecondhand.domain.repository;

import com.esecondhand.esecondhand.domain.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    @Query("SELECT m FROM Message m WHERE m.chat.id in ?1 and m.id = (select max(mv.id) from Message mv where mv.chat.id=m.chat.id)")
    List<Message> findMessagesPreviewFromAllChats(List<Long> chatsIds);

    @Query("SELECT m FROM Message m WHERE m.chat.id = ?1 and m.id = (select max(mv.id) from Message mv where mv.chat.id=?1)")
    Message findMessagePreviewByChatId(Long chatId);

    List<Message> findAllByChatIdOrderByCreationDateAsc(Long chatId);

    @Query("SELECT count(m) FROM Message m WHERE m.chat.id in ?1 and m.author.id != ?2 and m.isSeen = false")
    Long findUnreadCounter(List<Long> chatId, Long userId);


}
