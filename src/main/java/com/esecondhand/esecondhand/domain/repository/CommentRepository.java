package com.esecondhand.esecondhand.domain.repository;

import com.esecondhand.esecondhand.domain.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    Long countAllByReceiverId(Long id);

    @Query("SELECT c FROM Comment c WHERE c.receiver.id = ?1 order by c.creationDate desc")
    List<Comment> findAllByReceiverId(Long id, Pageable pageable);

    @Query("SELECT avg(c.rating) FROM Comment c WHERE c.receiver.id = ?1")
    Double findUserAvgRating(Long id);



}
