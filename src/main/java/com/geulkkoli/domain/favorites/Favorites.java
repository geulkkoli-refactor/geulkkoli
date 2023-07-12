package com.geulkkoli.domain.favorites;

import com.geulkkoli.domain.post.Post;
import com.geulkkoli.domain.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Objects;

@Entity
@NoArgsConstructor
@Getter
public class Favorites {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long favoritesId;

    //어던 게시글의 좋아요 인지
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    //누가 좋아요를 눌렀는지
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "favorites_user_id")
    private User user;

    public Favorites (User user, Post post) {
        this.user = user;
        this.post = post;
    }

    @Override
    public boolean equals(Object obj) {
        Favorites favorites = (Favorites) obj;
        return getFavoritesId() != null && Objects.equals(getFavoritesId(), favorites.getFavoritesId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}
