package com.geulkkoli.domain.favorites;

import com.geulkkoli.domain.favorites.Favorites;
import com.geulkkoli.domain.post.Post;
import com.geulkkoli.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavoritesRepository extends JpaRepository<Favorites, Long> {
    Optional<Favorites> findByUserAndPost (User user, Post post);
    List<Favorites> findAllByPost (Post post);
    List<Favorites> findAllByUser (User user);
}
