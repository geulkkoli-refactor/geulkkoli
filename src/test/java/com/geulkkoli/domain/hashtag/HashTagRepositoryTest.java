package com.geulkkoli.domain.hashtag;

import com.geulkkoli.domain.post.Post;
import com.geulkkoli.domain.post.PostRepository;
import com.geulkkoli.domain.user.User;
import com.geulkkoli.domain.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest
@Transactional
class HashTagRepositoryTest {


    @Autowired
    private HashTagRepository hashTagRepository;


    @AfterEach
    void afterEach() {
        hashTagRepository.deleteAll();
    }

    @Test
    void findHashTagIds() {
        //given
        HashTag hashTag = new HashTag("일반글",  HashTagType.GENERAL);
        HashTag save = hashTagRepository.save(hashTag);
        HashTag hashTag2 = new HashTag("공지글",   HashTagType.MANAGEMENT);
        HashTag save2 = hashTagRepository.save(hashTag2);
        List<String> hashTagNames = new ArrayList<>();
        hashTagNames.add("일반글");
        hashTagNames.add("공지글");
        //when
        //then
        List<Long> hashIdsByHashTagNames = hashTagRepository.hashIdsByHashTagNames(hashTagNames);

        assertThat(hashIdsByHashTagNames.get(0)).isEqualTo(save.getHashTagId());
        assertThat(hashIdsByHashTagNames.get(1)).isEqualTo(save2.getHashTagId());
    }


    @Test
    void findByHashTagName() {
        //given
        HashTag hashTag = HashTag.builder()
                .hashTagName("일반")
                .hashTagType(HashTagType.GENERAL)
                .build();
        HashTag save = hashTagRepository.save(hashTag);
        HashTag hashTag2 = HashTag.builder()
                .hashTagName("공지")
                .hashTagType(HashTagType.MANAGEMENT)
                .build();
        HashTag save2 = hashTagRepository.save(hashTag2);
        //when
        HashTag byHashTagName = hashTagRepository.findByHashTagName("일반");
        //then
        assertThat(byHashTagName.getHashTagId()).isEqualTo(save.getHashTagId());
    }
}