package com.geulkkoli.domain.favorites;

import com.geulkkoli.domain.post.Post;
import com.geulkkoli.domain.post.PostRepository;
import com.geulkkoli.domain.user.NoSuchCommnetException;
import com.geulkkoli.domain.user.User;
import com.geulkkoli.domain.user.UserRepository;
import com.geulkkoli.web.blog.dto.WriteRequestDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@Slf4j
@SpringBootTest
@Transactional
@ActiveProfiles("test")
class FavoritesRepositoryTest {

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FavoritesRepository favoritesRepository;

    private User user;
    private Post post01, post02;

    @AfterEach
    void afterEach () {
        favoritesRepository.deleteAllInBatch();
        postRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    @BeforeEach
    void init(){
        User save = User.builder()
                .email("test@naver.com")
                .userName("test")
                .nickName("test")
                .phoneNo("00000000000")
                .password("123")
                .gender("male").build();

        user = userRepository.save(save);
    }

    @BeforeEach
    void beforeEach () {
        WriteRequestDTO writeRequestDTO01 = WriteRequestDTO.builder()
                .title("testTitle01")
                .postBody("test postbody 01")
                .nickName(user.getNickName())
                .build();
        post01 = user.writePost(writeRequestDTO01);
        postRepository.save(post01);

        WriteRequestDTO writeRequestDTO02 = WriteRequestDTO.builder()
                .title("testTitle02")
                .postBody("test postbody 02")
                .nickName(user.getNickName())
                .build();
        post02 = user.writePost(writeRequestDTO02);
        postRepository.save(post02);
    }

    @Test
    public void 좋아요_저장 () throws Exception {
        //given
        Favorites favorite = user.pressFavorite(post01);

        //when
        Favorites save = favoritesRepository.save(favorite);

        //then
        assertThat(save.getUser().getNickName()).isEqualTo(user.getNickName());
    }

    @Test
    public void 좋아요_하나_찾기 () throws Exception {
        //given
        Favorites save = favoritesRepository.save(user.pressFavorite(post01));

        //when
        Favorites find = favoritesRepository.findById(save.getFavoritesId())
                .orElseThrow(() -> new NoSuchCommnetException("no found"));

        //then
        assertThat(save).isEqualTo(find);

    }

    @Test
    public void 좋아요_전체_찾기 () throws Exception {
        //given
        Favorites save = favoritesRepository.save(user.pressFavorite(post01));
        Favorites save2 = favoritesRepository.save(user.pressFavorite(post02));

        //when
        List<Favorites> all = favoritesRepository.findAll();

        //then
        assertThat(all.size()).isEqualTo(2);
    }

    @Test
    public void 좋아요_취소 () throws Exception {
        //given
        Favorites save = favoritesRepository.save(user.pressFavorite(post01));
        Favorites save2 = favoritesRepository.save(user.pressFavorite(post02));

        //when
        Favorites delete = user.cancelFavorite(save);
        favoritesRepository.delete(delete);

        List<Favorites> all = favoritesRepository.findAll();

        //then
        assertThat(all.get(0)).isEqualTo(save2);
    }
}