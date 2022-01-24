package com.esecondhand.esecondhand.service.serviceImpl;


import com.esecondhand.esecondhand.domain.dto.CommentDto;
import com.esecondhand.esecondhand.domain.dto.CommentEntryDto;
import com.esecondhand.esecondhand.domain.entity.AppUser;
import com.esecondhand.esecondhand.domain.entity.Comment;
import com.esecondhand.esecondhand.domain.entity.User;
import com.esecondhand.esecondhand.domain.mapper.CommentMapper;
import com.esecondhand.esecondhand.domain.repository.CommentRepository;
import com.esecondhand.esecondhand.domain.repository.UserRepository;
import com.esecondhand.esecondhand.exception.ObjectDoesntExistsException;
import com.esecondhand.esecondhand.service.CommentService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    private UserRepository userRepository;

    private CommentRepository commentRepository;

    private CommentMapper commentMapper;

    private final int pageSize = 5;

    public CommentServiceImpl(UserRepository userRepository, CommentRepository commentRepository, CommentMapper commentMapper) {
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
        this.commentMapper = commentMapper;
    }

    @Override
    public void addComment(CommentEntryDto commentEntryDto) throws ObjectDoesntExistsException {
        AppUser appUser = (AppUser) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        Comment comment = new Comment();
        comment.setComment(commentEntryDto.getComment());
        comment.setRating(commentEntryDto.getRating());
        comment.setCreationDate(LocalDateTime.now());

        User user = userRepository.findById(commentEntryDto.getReceiverId()).orElse(null);

        if (user == null) {
            throw new ObjectDoesntExistsException("User with given id, doesn't exist");
        }
        comment.setReceiver(user);
        comment.setCreator(appUser.getUser());

        commentRepository.save(comment);


    }

    @Override
    public List<CommentDto> getComments(Long userId, int page) {
        Pageable pageRequest = PageRequest.of(page, 5);

        List<Comment> commentsList = commentRepository.findAllByReceiverId(userId, pageRequest);

        return commentMapper.mapToCommentDtoList(commentsList);
    }
}
