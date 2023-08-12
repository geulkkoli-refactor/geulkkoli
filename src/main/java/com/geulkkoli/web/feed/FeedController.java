package com.geulkkoli.web.feed;

import com.geulkkoli.application.follow.FollowInfos;
import com.geulkkoli.domain.favorites.Favorites;
import com.geulkkoli.domain.follow.service.FollowFindService;
import com.geulkkoli.domain.post.Post;
import com.geulkkoli.domain.post.service.PostFindService;
import com.geulkkoli.domain.user.User;
import com.geulkkoli.domain.user.service.UserFindService;
import com.geulkkoli.web.blog.dto.ArticleDTO;
import com.geulkkoli.web.blog.dto.ArticlePagingRequestDTO;
import com.geulkkoli.web.blog.dto.PagingDTO;
import com.geulkkoli.web.blog.dto.UserProfileDTO;
import com.geulkkoli.web.comment.dto.CommentBodyDTO;
import com.geulkkoli.web.follow.dto.FollowResultDTO;
import com.geulkkoli.web.follow.dto.FollowsCountDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Comparator;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toUnmodifiableList;

@Slf4j
@RequestMapping("/feed")
@Controller
public class FeedController {

    private final UserFindService userFindService;
    private final PostFindService postFindService;

    private final FollowFindService followFindService;


    public FeedController(UserFindService userFindService, PostFindService postFindService, FollowFindService followFindService) {
        this.userFindService = userFindService;
        this.postFindService = postFindService;
        this.followFindService = followFindService;
    }

    @GetMapping("/{nickName}")
    public ModelAndView getMyPage(@PathVariable("nickName") String nickName, @PageableDefault(size = 5, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        User user = userFindService.findByNickName(nickName);
        Integer countFolloweeByFollowerId = followFindService.countFolloweeByFollowerId(user.getUserId());
        Integer countFollowerByFolloweeId = followFindService.countFollowerByFolloweeId(user.getUserId());
        FollowsCountDTO followsCountDTO = FollowsCountDTO.of(countFolloweeByFollowerId, countFollowerByFolloweeId);
        ModelAndView mv = new ModelAndView("feed/mypage");
        mv.addObject("followsCount", followsCountDTO);
        return mv;
    }

    @GetMapping("/{nickName}/followees")
    public ModelAndView getFollowees(@PathVariable("nickName") String nickName, @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        User user = userFindService.findByNickName(nickName);
        Integer followee = followFindService.countFolloweeByFollowerId(user.getUserId());
        FollowInfos followeeUserInfos = followFindService.findSomeFolloweeByFollowerId(user.getUserId(), null, pageable);
        ModelAndView modelAndView = new ModelAndView("feed/mypage/followdetail", "followers", followeeUserInfos);
        modelAndView.addObject("allCount", followee);

        return modelAndView;
    }

    @GetMapping("/{nickName}/followers")
    public ModelAndView getFollowers(@PathVariable("nickName") String nickName, @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        User user = userFindService.findByNickName(nickName);
        Integer follower = followFindService.countFollowerByFolloweeId(user.getUserId());
        FollowInfos followInfos = followFindService.findSomeFollowerByFolloweeId(user.getUserId(), null, pageable);
        List<Long> userIdByFollowedEachOther = followFindService.findUserIdByFollowedEachOther(followInfos.userIds(), user.getUserId(), pageable.getPageSize());
        followInfos.checkSubscribe(userIdByFollowedEachOther);

        ModelAndView modelAndView = new ModelAndView("feed/mypage/followerdetail", "followers", followInfos);
        modelAndView.addObject("allCount", follower);

        return modelAndView;
    }

    @GetMapping("/{nickName}/favorites")
    public ModelAndView getFavorite(@PathVariable("nickName") String nickName, @PageableDefault(size = 5, sort = "postId", direction = Sort.Direction.DESC) Pageable pageable) {
        log.info("nickName = {}", nickName);
        User user = userFindService.findByNickName(nickName);
        List<Favorites> favorites = user.getFavorites().stream().collect(toUnmodifiableList());
        List<Post> favoritePosts = favorites.stream().sorted(Comparator.comparing(Favorites::getFavoritesId).reversed()).map(Favorites::getPost).collect(toList());

        int totalFavoritePosts = favoritePosts.size();
        int startIndex = pageable.getPageNumber() * pageable.getPageSize();
        int endIndex = Math.min(startIndex + pageable.getPageSize(), totalFavoritePosts);
        List<Post> subPosts = favoritePosts.subList(startIndex, endIndex);

        Page<Post> favoritsPost = new PageImpl<>(subPosts, pageable, favoritePosts.size());
        Page<ArticlePagingRequestDTO> readInfos = favoritsPost.map(ArticlePagingRequestDTO::toDTO);
        PagingDTO pagingDTO = PagingDTO.listDTOtoPagingDTO(readInfos);
        log.info("pagingDTO = {}", pagingDTO);
        ModelAndView modelAndView = new ModelAndView("feed/favorites", "pagingResponses", pagingDTO);
        modelAndView.addObject("loggingNickName", nickName);

        return modelAndView;
    }

    @GetMapping("{nickName}/favorites/{postId}")
    public ModelAndView getFavoritePost(@PathVariable("nickName") String nickName, @PathVariable("postId") Long postId) {
        User user = userFindService.findByNickName(nickName);
        Post findPost = postFindService.findById(postId);
        ArticleDTO post = ArticleDTO.toDTO(findPost);
        FollowResultDTO followResult = new FollowResultDTO(false, followFindService.checkFollow(user, findPost.getUser()));
        String checkFavorite = "exist";

        UserProfileDTO authorUser = UserProfileDTO.toDTO(findPost.getUser());
        ModelAndView modelAndView = new ModelAndView("blog/read" + postId);
        modelAndView.addObject("post", post);
        modelAndView.addObject("authorUser", authorUser);
        modelAndView.addObject("followResult", followResult);
        modelAndView.addObject("checkFavorite", checkFavorite);
        modelAndView.addObject("commentList", post.getCommentList());
        modelAndView.addObject("comments", new CommentBodyDTO());
        return modelAndView;
    }

}
