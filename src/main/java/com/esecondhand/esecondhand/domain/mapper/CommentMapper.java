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
        CommentDto dto = new CommentDto();
        dto.setId(entity.getId());
        dto.setComment(entity.getComment());
        dto.setCreatorId(entity.getCreator().getId());
        dto.setCreatorName(entity.getCreator().getDisplayName());
        dto.setRating(entity.getRating());
        dto.setCreationDate(entity.getCreationDate());
        return dto;
    }
}
