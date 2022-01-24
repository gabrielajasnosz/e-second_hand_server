package com.esecondhand.esecondhand.domain.repository;

import com.esecondhand.esecondhand.domain.entity.Comment;
import com.esecondhand.esecondhand.domain.entity.Follower;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FollowerRepository extends JpaRepository<Follower, Long> {
    @Query("SELECT f FROM Follower f WHERE f.following.id = ?1 order by f.following.displayName asc")
    List<Follower> getUserFollowers(Long id);

    @Query("SELECT f FROM Follower f WHERE f.follower.id = ?1 order by f.follower.displayName asc")
    List<Follower> getUserFollowedUsers(Long id);

    @Query("SELECT f.following.id FROM Follower f WHERE f.follower.id = ?1 order by f.follower.displayName asc")
    List<Long> getUserFollowedUsersIds(Long id);

    @Query("SELECT f FROM Follower f WHERE f.follower.id = ?1 AND f.following.id = ?2 order by f.following.displayName asc")
    Follower findFollower(Long followerId, Long followingId);

    Long countAllByFollowingId(Long followerId);

    Long countAllByFollowerId(Long followingId);

}
