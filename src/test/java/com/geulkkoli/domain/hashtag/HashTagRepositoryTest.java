package com.geulkkoli.domain.hashtag;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@DataJpaTest
class HashTagRepositoryTest {


    @Autowired
    private HashTagRepository hashTagRepository;


    @AfterEach
    void afterEach() {
        hashTagRepository.deleteAllInBatch();
    }

    @DisplayName("해시태그 이름들로 여러 해시태그 찾기")
    @Test
    void findAllHashTagByHashTagName() {
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
        List<HashTag> hashIdsByHashTagNames = hashTagRepository.findAllHashTagByHashTagNames(hashTagNames);

        assertThat(hashIdsByHashTagNames).contains(save,save2);
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