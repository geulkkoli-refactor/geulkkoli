package com.geulkkoli.web.follow;

import com.geulkkoli.application.follow.FollowInfos;
import com.geulkkoli.application.user.CustomAuthenticationPrinciple;
import com.geulkkoli.domain.follow.service.FollowFindService;
import com.geulkkoli.domain.follow.service.FollowService;
import com.geulkkoli.domain.user.User;
import com.geulkkoli.domain.user.service.UserFindService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class FollowApiController {
    private final FollowService followService;


    private final FollowFindService followFindService;

    private final UserFindService userFindService;

    @GetMapping("/follow/{userId}")
    public ResponseEntity<?> followUser(@PathVariable("userId") String userId, @AuthenticationPrincipal CustomAuthenticationPrinciple authUser) {
        try {
            User followee = userFindService.findById(Long.parseLong(userId));
            User follower = userFindService.findById(Long.parseLong(authUser.getUserId()));
            followService.follow(followee, follower);
            return ResponseEntity.ok().build();
        } catch (NoSuchElementException e) {
            log.error("NoSuchElementException: {}", e.getMessage());
            return ResponseEntity.status(404).build();
        }
    }

    @GetMapping("/unfollow/{userId}")
    public ResponseEntity<?> unfollowUser(@PathVariable("userId") String
                                                  userId, @AuthenticationPrincipal CustomAuthenticationPrinciple authUser) {
        try {
            User followee = userFindService.findById(Long.parseLong(userId));
            log.info("followee: {}", followee);
            User follower = userFindService.findByEmail(authUser.getUserName()).orElseThrow(() -> new NoSuchElementException("해당 유저가 없습니다."));
            followService.unFollow(followee, follower);
            return ResponseEntity.ok().build();
        } catch (NoSuchElementException e) {
            log.error("NoSuchElementException: {}", e.getMessage());
            return ResponseEntity.status(404).build();
        }
    }

    @GetMapping("/followees/{lastFollowId}")
    public ResponseEntity<FollowInfos> getFollowees(@PathVariable String
                                                            lastFollowId, @PageableDefault(size = 10) Pageable pageable, @AuthenticationPrincipal CustomAuthenticationPrinciple
                                                            authUser) {
        Optional<User> byEmail = userFindService.findByEmail(authUser.getUserName());
        if (byEmail.isPresent()) {
            User user = byEmail.get();
            FollowInfos someFolloweeByFollowerId = followFindService.findSomeFolloweeByFollowerId(user.getUserId(), Long.parseLong(lastFollowId), pageable);
            return ResponseEntity.ok().body(someFolloweeByFollowerId);
        }
        log.error("해당 유저가 없습니다.");
        return ResponseEntity.status(404).build();
    }

    @GetMapping("/followers/{lastFollowId}")
    public ResponseEntity<FollowInfos> getFollowers(@PathVariable String
                                                            lastFollowId, @PageableDefault Pageable pageable, @AuthenticationPrincipal CustomAuthenticationPrinciple
                                                            authUser) {
        Optional<User> byEmail = userFindService.findByEmail(authUser.getUserName());
        if (byEmail.isPresent()) {
            User user = byEmail.get();
            FollowInfos someFollowerByFolloweeId = followFindService.findSomeFollowerByFolloweeId(user.getUserId(), Long.parseLong(lastFollowId), pageable);
            List<Long> userIds = followFindService.findUserIdByFollowedEachOther(someFollowerByFolloweeId.userIds(), user.getUserId(), pageable.getPageSize());
            someFollowerByFolloweeId.checkSubscribe(userIds);
            return ResponseEntity.ok().body(someFollowerByFolloweeId);
        }
        log.error("해당 유저가 없습니다.");
        return ResponseEntity.status(404).build();
    }
}
