package com.geulkkoli.web.favorite;

import com.geulkkoli.application.user.CustomAuthenticationPrinciple;
import com.geulkkoli.domain.favorites.service.FavoriteService;
import com.geulkkoli.domain.favorites.Favorites;
import com.geulkkoli.domain.post.Post;
import com.geulkkoli.domain.post.service.PostFindService;
import com.geulkkoli.domain.post.service.PostService;
import com.geulkkoli.domain.user.User;
import com.geulkkoli.domain.user.service.UserFindService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/favorite")
public class FavoriteController {

    private final FavoriteService favoriteService;
    private final PostFindService postFindService;
    private final UserFindService userFindService;

    @PostMapping("/pressFavorite/{postId}")
    public ResponseEntity<String> pressFavoriteButton(@PathVariable("postId") Long postId,
                                                      @AuthenticationPrincipal CustomAuthenticationPrinciple user) throws Exception {
        log.info("pressFavoriteButton");
        Post post = postFindService.findById(postId);

        try {
            User loginUser = userFindService.findById(Long.parseLong(user.getUserId()));
            Optional<Favorites> optionalFavorites = favoriteService.checkFavorite(post, loginUser);
            if (optionalFavorites.isEmpty()) {
                favoriteService.addFavorite(post, loginUser);
                return ResponseEntity.ok("Add Success");
            } else {
                favoriteService.undoFavorite(optionalFavorites.get());
                return ResponseEntity.ok("Cancel Success");
            }
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("로그인이 필요합니다.");
        }
    }
}