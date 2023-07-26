package com.geulkkoli.domain.posthashtag.service;

import com.geulkkoli.domain.hashtag.HashTag;
import com.geulkkoli.domain.hashtag.HashTagRepository;
import com.geulkkoli.domain.hashtag.HashTagType;
import com.geulkkoli.domain.post.Post;
import com.geulkkoli.domain.post.PostRepository;
import com.geulkkoli.domain.post.service.PostService;
import com.geulkkoli.domain.posthashtag.service.PostHashTagService;
import com.geulkkoli.domain.user.User;
import com.geulkkoli.domain.user.UserRepository;
import com.geulkkoli.web.post.dto.AddDTO;
import com.geulkkoli.web.post.dto.PostRequestListDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest
@Transactional
class PostHashTagServiceTest {

    @Autowired
    private PostHashTagService postHashTagService;

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private HashTagRepository hashTagRepository;
    @Autowired
    private PostService postService;

    private User user;
    private Post post, post01, post02, post03;
    private HashTag tag1, tag2, tag3, tag4, tag5, tag6, tag7, tag8, tag9;

    List<Post> posts;

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

    @BeforeEach
    void beforeEach() {
        for (int i = 0; i < 10; i++) {
            AddDTO addDTO = AddDTO.builder()
                    .title("testTitle" + i)
                    .postBody("test postbody " + i)
                    .nickName(user.getNickName())
                    .build();
            post = user.writePost(addDTO);
            postRepository.save(post);
        }


        AddDTO addDTO01 = AddDTO.builder()
                .title("testTitle01")
                .postBody("test postbody 01")
                .nickName(user.getNickName())
                .build();
        post01 = user.writePost(addDTO01);
        postRepository.save(post01);

        AddDTO addDTO02 = AddDTO.builder()
                .title("testTitle02")
                .postBody("test postbody 02")
                .nickName(user.getNickName())
                .build();
        post02 = user.writePost(addDTO02);
        postRepository.save(post02);

        AddDTO addDTO03 = AddDTO.builder()
                .title("testTitle03")
                .postBody("test postbody 03")
                .nickName(user.getNickName())
                .build();
        post03 = user.writePost(addDTO03);
        postRepository.save(post03);

        posts = postRepository.findAll();

        tag1 = hashTagRepository.save(new HashTag("일반글", HashTagType.GENERAL));
        tag2 = hashTagRepository.save(new HashTag("공지글", HashTagType.MANAGEMENT));
        tag3 = hashTagRepository.save(new HashTag("판타지", HashTagType.GENERAL));
        tag4 = hashTagRepository.save(new HashTag("코미디", HashTagType.GENERAL));
        tag5 = hashTagRepository.save(new HashTag("단편소설", HashTagType.GENERAL));
        tag6 = hashTagRepository.save(new HashTag("시", HashTagType.GENERAL));
        tag7 = hashTagRepository.save(new HashTag("이상", HashTagType.GENERAL));
        tag8 = hashTagRepository.save(new HashTag("게임", HashTagType.GENERAL));
        tag9 = hashTagRepository.save(new HashTag("판게아", HashTagType.GENERAL));

        hashTagRepository.save(new HashTag("소설", HashTagType.CATEGORY));
        hashTagRepository.save(new HashTag("완결", HashTagType.STATUS));
    }

    @Test
    @DisplayName("태그에 따른 게시글을 잘 가져오는지 테스트")
     void searchPostContainAllHashTagsTest() {
        //given
        List<HashTag> hashTags = new ArrayList<>(Set.of(tag1, tag2));
        List<HashTag> hashTags2 = new ArrayList<>(Set.of(tag4, tag5));


        List<List<HashTag>> hashTagLists = new ArrayList<>();
        hashTagLists.add(new ArrayList<>(Set.of(tag1, tag3)));
        hashTagLists.add(new ArrayList<>(Set.of(tag2, tag8, tag5)));
        hashTagLists.add(new ArrayList<>(Set.of(tag1, tag2, tag4)));
        hashTagLists.add(new ArrayList<>(Set.of(tag1, tag2, tag7, tag8)));
        hashTagLists.add(new ArrayList<>(Set.of(tag1, tag3, tag6)));
        hashTagLists.add(new ArrayList<>(Set.of(tag1, tag2, tag6, tag7, tag3)));
        hashTagLists.add(new ArrayList<>(Set.of(tag1, tag2, tag8, tag5, tag4)));
        hashTagLists.add(new ArrayList<>(Set.of(tag2, tag6, tag7)));
        hashTagLists.add(new ArrayList<>(Set.of(tag8, tag2, tag3, tag7)));
        hashTagLists.add(new ArrayList<>(Set.of(tag1, tag5, tag8)));

        for (int i = 0; i < 10; i++) {
            postHashTagService.addHashTagsToPost(posts.get(i), hashTagLists.get(i));
        }

        //when
        List<Post> posts = postHashTagService.searchPostContainAllHashTags(hashTags);
        List<Post> posts2 = postHashTagService.searchPostContainAllHashTags(hashTags2);


        //then
        assertThat(posts).hasSize(4);
        assertThat(posts2).hasSize(1);

    }


    @Test
    @DisplayName("실제로 검색 타입, 검색어에 따라 잘 찾을 수 있는지")
    public void searchPostsListByHashTagVer2() {
        //given
        postHashTagService.addHashTagsToPost(post01, new ArrayList<>(Set.of(tag1, tag3)));
        for (int i = 0; i < 35; i++) {
            ArrayList<HashTag> hashTags = new ArrayList<>(Set.of(tag1, tag3));

            Post post1 = postService.savePost(AddDTO.builder()
                    .title("test01")
                    .postBody("TestingCode01"+i)
                    .tagListString("#신과 함께")
                    .tagCategory("#소설")
                    .tagStatus("#완결")
                    .nickName("test")
                    .authorId(1L)
                    .build(), user);

            postHashTagService.addHashTagsToPost(post1,hashTags);
        }

        String searchWords = "01 #일반글";

        //when
        Pageable pageable = PageRequest.of(5, 5);
        Page<PostRequestListDTO> listDTOS = postHashTagService.searchPostsListByHashTag(pageable, searchWords);
        List<PostRequestListDTO> collect = listDTOS.get().collect(Collectors.toList());

        //then
        assertThat(collect.size()).isEqualTo(5);

    }

}