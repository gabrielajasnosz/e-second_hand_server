package com.esecondhand.esecondhand.domain.repository;

import com.esecondhand.esecondhand.domain.entity.Message;
import com.esecondhand.esecondhand.domain.entity.SavedFilter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    @Query("SELECT m FROM Message m WHERE m.chat.id in ?1 and m.id = (select max(mv.id) from Message mv where mv.chat.id=m.chat.id)")
    List<Message> findMessagesPreviewFromAllChats(List<Long> chatsIds);

    @Query("SELECT m FROM Message m WHERE m.chat.id = ?1 and m.id = (select max(mv.id) from Message mv where mv.chat.id=?1)")
    Message findMessagePreviewByChatId(Long chatId);

    List<Message> findAllByChatIdOrderByCreationDateAsc(Long chatId);

}
