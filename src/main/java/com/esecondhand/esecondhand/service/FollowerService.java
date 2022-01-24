package com.esecondhand.esecondhand.service;

import com.esecondhand.esecondhand.domain.dto.UserPreviewDto;
import com.esecondhand.esecondhand.exception.ObjectDoesntExistsException;

import java.util.List;
import java.util.Map;

public interface FollowerService {

    void addToFollowed(Long userId) throws ObjectDoesntExistsException;

    List<UserPreviewDto> getFollowers(Long userId) throws ObjectDoesntExistsException;
    List<UserPreviewDto> getFollowing(Long userId) throws ObjectDoesntExistsException;

    void deleteFromFollowed(Long userId);

}
