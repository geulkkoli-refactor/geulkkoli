package com.geulkkoli.domain.favorites.service;

import com.geulkkoli.domain.favorites.Favorites;
import com.geulkkoli.domain.favorites.FavoritesRepository;
import com.geulkkoli.domain.post.Post;
import com.geulkkoli.domain.user.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Slf4j
@Service
@Transactional
public class FavoriteService {

    private final FavoritesRepository favoritesRepository;

    public FavoriteService (FavoritesRepository favoritesRepository) {
        this.favoritesRepository = favoritesRepository;
    }

    //좋아요 추가
    public Long addFavorite(Post post, User user) {
        return favoritesRepository.save(user.pressFavorite(post)).getFavoritesId();
    }

    //ID로 좋아요 찾기
    public Favorites findById (Long favoriteId) {
        return favoritesRepository.findById(favoriteId)
                .orElseThrow(() -> new NoSuchElementException("No favorite id matches:"+favoriteId));
    }

    public List<Favorites> showFavoriteByPost (Post post) {
        return favoritesRepository.findAllByPost(post);
    }

    public List<Favorites> showFavoriteByUser (User user) {
        return favoritesRepository.findAllByUser(user);
    }

    public void undoFavorite (Favorites favorite) {
        Favorites deleteFavorite = favorite.getUser().cancelFavorite(favorite);
        favoritesRepository.delete(deleteFavorite);
    }

    public Optional<Favorites> checkFavorite(Post post, User user) {
        return favoritesRepository.findByUserAndPost(user, post);
    }
}