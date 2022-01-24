package com.esecondhand.esecondhand.service.serviceImpl;

import com.esecondhand.esecondhand.domain.dto.UserPreviewDto;
import com.esecondhand.esecondhand.domain.entity.AppUser;
import com.esecondhand.esecondhand.domain.entity.Follower;
import com.esecondhand.esecondhand.domain.entity.User;
import com.esecondhand.esecondhand.domain.mapper.FollowerMapper;
import com.esecondhand.esecondhand.domain.repository.FollowerRepository;
import com.esecondhand.esecondhand.domain.repository.UserRepository;
import com.esecondhand.esecondhand.exception.ObjectDoesntExistsException;
import com.esecondhand.esecondhand.service.FollowerService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FollowerServiceImpl implements FollowerService {

    private final UserRepository userRepository;

    private final FollowerRepository followerRepository;

    private final FollowerMapper followerMapper;

    public FollowerServiceImpl(UserRepository userRepository, FollowerRepository followerRepository, FollowerMapper followerMapper) {
        this.userRepository = userRepository;
        this.followerRepository = followerRepository;
        this.followerMapper = followerMapper;
    }
    @Override
    public void addToFollowed(Long userId) throws ObjectDoesntExistsException {
        AppUser appUser = (AppUser) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        User user = userRepository.findById(userId).orElse(null);

        if(user == null){
            throw new ObjectDoesntExistsException("User with given id doesn't exists");
        }

        Follower follower = new Follower();
        follower.setFollower(appUser.getUser());
        follower.setFollowing(user);

        followerRepository.save(follower);

    }

    @Override
    public List<UserPreviewDto> getFollowers(Long userId) throws ObjectDoesntExistsException {
        User user = userRepository.findById(userId).orElse(null);

        if(user == null){
            throw new ObjectDoesntExistsException("User with given id doesn't exists");
        }

        List<Follower> followers = followerRepository.getUserFollowers(userId);

        List<UserPreviewDto> followersList = followerMapper.mapUserFollowers(followers);

        return followersList;
    }

    @Override
    public List<UserPreviewDto> getFollowing(Long userId) throws ObjectDoesntExistsException {
        User user = userRepository.findById(userId).orElse(null);

        if(user == null){
            throw new ObjectDoesntExistsException("User with given id doesn't exists");
        }

        List<Follower> following = followerRepository.getUserFollowedUsers(userId);

        List<UserPreviewDto> followersList = followerMapper.mapUserFollowedUsers(following);

        return followersList;
    }

    @Override
    public void deleteFromFollowed(Long userId) {
        AppUser appUser = (AppUser) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        Follower follower = followerRepository.findFollower(appUser.getUser().getId(), userId);
        followerRepository.deleteById(follower.getId());
    }

}
