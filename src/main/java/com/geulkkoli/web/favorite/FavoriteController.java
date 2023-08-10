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
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/pressFavorite/{postId}")
    public ResponseEntity<FavoriteRequestDTO> pressFavoriteButton(@PathVariable("postId") Long postId,
                                                      @AuthenticationPrincipal CustomAuthenticationPrinciple user) throws Exception {
        log.info("pressFavoriteButton");
        Post post = postFindService.findById(postId);

        try {
            User loginUser = userFindService.findById(Long.parseLong(user.getUserId()));
            Optional<Favorites> optionalFavorites = favoriteService.checkFavorite(post, loginUser);
            if (optionalFavorites.isEmpty()) {
                favoriteService.addFavorite(post, loginUser);
                FavoriteRequestDTO addSuccess = new FavoriteRequestDTO("Add Success", post.getFavorites().size());
                return ResponseEntity.ok(addSuccess);
            } else {
                favoriteService.undoFavorite(optionalFavorites.get());
                FavoriteRequestDTO cancelSuccess = new FavoriteRequestDTO("Cancel Success", post.getFavorites().size());
                return ResponseEntity.ok(cancelSuccess);
            }
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new FavoriteRequestDTO("Error", post.getFavorites().size())
            );
        }
    }
}