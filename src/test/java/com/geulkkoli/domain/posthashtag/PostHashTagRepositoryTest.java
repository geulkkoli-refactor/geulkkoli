package com.geulkkoli.domain.posthashtag;

import com.geulkkoli.domain.hashtag.HashTag;
import com.geulkkoli.domain.hashtag.HashTagRepository;
import com.geulkkoli.domain.hashtag.HashTagType;
import com.geulkkoli.domain.post.Post;
import com.geulkkoli.domain.post.PostRepository;
import com.geulkkoli.domain.user.User;
import com.geulkkoli.domain.user.UserRepository;
import com.geulkkoli.web.post.dto.AddDTO;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import javax.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;

@Slf4j
@DataJpaTest
@ActiveProfiles("test")
class PostHashTagRepositoryTest {

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private HashTagRepository hashTagRepository;
    @Autowired
    private PostHashTagRepository postHashTagRepository;


    @Test
    void 게시글_해시태그_저장(){
        //given
        User user = creatUser("test@naver.com", "test", "test", "00000000000", "123", "male");
        Post post = createPost(user, "testTitle01", "test postbody 01");
        HashTag 일반글 = creatHashTag("일반글", HashTagType.GENERAL);
        PostHashTag newPostHashtag = createPostHashTag(post, 일반글);

        //when
        PostHashTag postHashTag = postHashTagRepository.save(newPostHashtag);

        //then
        assertThat(postHashTag.getHashTag().getHashTagName()).isEqualTo("일반글");
    }

    @Test
    void 게시글_다중_해시태그_저장() {
        //given
        User user = creatUser("test@naver.com", "test", "test", "00000000000", "123", "male");
        Post post01 = createPost(user, "test", "ttest");

        HashTag tag1 = creatHashTag("일반글", HashTagType.GENERAL);
        HashTag tag3 = creatHashTag("코미디", HashTagType.CATEGORY);
        HashTag tag4 = creatHashTag("판타지", HashTagType.CATEGORY);

        PostHashTag save1 = postHashTagRepository.save(createPostHashTag(post01, tag1));
        PostHashTag save2 = postHashTagRepository.save(createPostHashTag(post01, tag3));
        PostHashTag save3 = postHashTagRepository.save(createPostHashTag(post01, tag4));


        //when
        PostHashTag postHashTag1 = postHashTagRepository.save(save1);
        PostHashTag postHashTag2 = postHashTagRepository.save(save2);
        PostHashTag postHashTag3 = postHashTagRepository.save(save3);


        //then
        assertThat(post01.getPostHashTags()).hasSize(3);
        assertThat(post01.getPostHashTags()).contains(postHashTag1);
        assertThat(post01.getPostHashTags()).contains(postHashTag2);
        assertThat(post01.getPostHashTags()).contains(postHashTag3);
    }

    @Test
    void 게시글_해시태그_불러오기(){
        //given
        User user = creatUser("test@naver.com", "test", "test", "00000000000", "123", "male");
        Post post01 = createPost(user, "testTitle01", "test postbody 01");

        HashTag tag1 = creatHashTag("일반글", HashTagType.GENERAL);
        HashTag tag3 = creatHashTag("코미디", HashTagType.CATEGORY);
        HashTag tag4 = creatHashTag("판타지", HashTagType.CATEGORY);

        PostHashTag save1 = postHashTagRepository.save(createPostHashTag(post01, tag1));
        PostHashTag save2 = postHashTagRepository.save(createPostHashTag(post01, tag3));
        PostHashTag save3 = postHashTagRepository.save(createPostHashTag(post01, tag4));
        //when
        List<PostHashTag> list = post01.getPostHashTags();

        //then
        assertAll(
                () -> assertThat(list).hasSize(3),
                () -> assertThat(list).contains(save1),
                () -> assertThat(list).contains(save2),
                () -> assertThat(list).contains(save3));
    }

    @Test
    void 해시태그로_게시글_전부_찾기()  {
        //given

        User user = creatUser("test@naver.com", "test", "test", "00000000000", "123", "male");
        Post post01 = createPost(user, "testTitle01", "test postbody 01");
        Post post02 = createPost(user, "testTitle02", "test postbody 02");

        HashTag tag1 = creatHashTag("일반글", HashTagType.GENERAL);

        PostHashTag save01 = postHashTagRepository.save(createPostHashTag(post01, tag1));
        PostHashTag save02 = postHashTagRepository.save(createPostHashTag(post02, tag1));

        //when
        List<PostHashTag> postHashTagList = postHashTagRepository.findAllByHashTag(tag1);
        //then
        assertAll(
                () -> assertThat(postHashTagList).hasSize(2),
                () -> assertThat(postHashTagList).contains(save01),
                () -> assertThat(postHashTagList).contains(save02));
    }

    private PostHashTag createPostHashTag(Post post, HashTag hashTag) {
        return post.addHashTag(hashTag);
    }

    private HashTag creatHashTag(String hashTagName, HashTagType hashTagType) {
        return hashTagRepository.save(new HashTag(hashTagName, hashTagType));
    }

    private Post createPost(User user, String title, String postBody) {
        AddDTO addDTO01 = AddDTO.builder()
                .title(title)
                .postBody(postBody)
                .nickName(user.getNickName())
                .build();
        return postRepository.save(user.writePost(addDTO01));
    }

    private User creatUser(String userName, String nickName, String email, String phoneNo, String password, String gender) {
        User user = User.builder()
                .email(email)
                .userName(userName)
                .nickName(nickName)
                .phoneNo(phoneNo)
                .password(password)
                .gender(gender).build();
        return userRepository.save(user);
    }

}