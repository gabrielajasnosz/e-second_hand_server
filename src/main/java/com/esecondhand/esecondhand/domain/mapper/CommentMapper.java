package com.esecondhand.esecondhand.domain.mapper;

import com.esecondhand.esecondhand.domain.dto.CommentDto;
import com.esecondhand.esecondhand.domain.entity.Comment;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CommentMapper {
    public List<CommentDto> mapToCommentDtoList(List<Comment> entity) {
        return entity.stream().map(this::mapToCommentDto).collect(Collectors.toList());
    }

    public CommentDto mapToCommentDto(Comment entity) {

        return CommentDto.builder()
                .id(entity.getId())
                .comment(entity.getComment())
                .creatorId(entity.getCreator().getId())
                .creatorName(entity.getCreator().getDisplayName())
                .rating(entity.getRating())
                .creationDate(entity.getCreationDate())
                .build();
    }
}
