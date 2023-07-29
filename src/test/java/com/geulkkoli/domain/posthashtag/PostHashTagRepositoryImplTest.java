package com.geulkkoli.domain.posthashtag;

import com.geulkkoli.domain.hashtag.HashTag;
import com.geulkkoli.domain.hashtag.HashTagRepository;
import com.geulkkoli.domain.hashtag.HashTagType;
import com.geulkkoli.domain.post.Post;
import com.geulkkoli.domain.post.service.PostService;
import com.geulkkoli.domain.user.User;
import com.geulkkoli.domain.user.UserRepository;
import com.geulkkoli.web.post.dto.AddDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest
class PostHashTagRepositoryImplTest {

    @Autowired
    private PostService postService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostHashTagRepository postHashTagRepository;

    @Autowired
    private HashTagRepository hashTagRepository;

    private User user;
    private Post post01;
    private HashTag tag1;

    @BeforeEach
    void init() {
        User save = User.builder()
                .email("test@naver.com")
                .userName("test")
                .nickName("test")
                .phoneNo("00000000000")
                .password("123")
                .gender("male").build();

        user = userRepository.save(save);


    }

    @Test
    void findAllByHashTagName() {
        HashTag tag2 = hashTagRepository.save(new HashTag("일반", HashTagType.GENERAL));
        HashTag tag1 = hashTagRepository.save(new HashTag("공지글", HashTagType.MANAGEMENT));
        HashTag tag3 = hashTagRepository.save(new HashTag("로맨스", HashTagType.GENERAL));
        HashTag tag4 = hashTagRepository.save(new HashTag("코미디", HashTagType.GENERAL));
        HashTag tag5 = hashTagRepository.save(new HashTag("단편소설", HashTagType.GENERAL));
        HashTag tag6 = hashTagRepository.save(new HashTag("시", HashTagType.GENERAL));
        HashTag tag7 = hashTagRepository.save(new HashTag("이상", HashTagType.GENERAL));
        HashTag tag8 = hashTagRepository.save(new HashTag("게임", HashTagType.GENERAL));
        HashTag tag9 = hashTagRepository.save(new HashTag("판게아", HashTagType.GENERAL));

        hashTagRepository.save(new HashTag("소설", HashTagType.CATEGORY));
        hashTagRepository.save(new HashTag("완결", HashTagType.STATUS));
        AddDTO addDTO = new AddDTO(user.getUserId(), "title", "body", "nick", "로맨스", "소설", "완결");
        AddDTO addDTO1 = new AddDTO(user.getUserId(), "title", "body", "nick1", "일반", "소설", "완결");
        AddDTO addDTO2 = new AddDTO(user.getUserId(), "title", "body", "nick2", "일반", "소설", "완결");
        Post save = postService.savePost(addDTO, user);
        Post save1 = postService.savePost(addDTO1, user);
        Post save2 = postService.savePost(addDTO2, user);

        List<Post> posts = postHashTagRepository.findAllByHashTagNames(List.of("로맨스","소설", "완결"));

        assertThat(posts).hasSize(3);
    }
}