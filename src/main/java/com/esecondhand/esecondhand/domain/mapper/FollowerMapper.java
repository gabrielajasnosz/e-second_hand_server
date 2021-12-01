package com.esecondhand.esecondhand.domain.mapper;

import com.esecondhand.esecondhand.domain.dto.CommentDto;
import com.esecondhand.esecondhand.domain.dto.UserPreviewDto;
import com.esecondhand.esecondhand.domain.entity.Comment;
import com.esecondhand.esecondhand.domain.entity.Follower;
import com.esecondhand.esecondhand.domain.entity.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class FollowerMapper {

    public List<UserPreviewDto> mapUserFollowers(List<Follower> followers){
        return followers.stream().map(this::mapToFollower).collect(Collectors.toList());
    }
    public List<UserPreviewDto> mapUserFollowedUsers(List<Follower> followers){
        return followers.stream().map(this::mapToFollowedUsers).collect(Collectors.toList());
    }

    public UserPreviewDto mapToFollower(Follower entity) {
        UserPreviewDto dto = new UserPreviewDto();
        dto.setId(entity.getFollower().getId());
        dto.setDisplayName(entity.getFollower().getDisplayName());
        return dto;
    }
    public UserPreviewDto mapToFollowedUsers(Follower entity) {
        UserPreviewDto dto = new UserPreviewDto();
        dto.setId(entity.getFollowing().getId());
        dto.setDisplayName(entity.getFollowing().getDisplayName());
        return dto;
    }
}
