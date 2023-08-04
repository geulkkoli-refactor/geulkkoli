package com.geulkkoli.domain.hashtag.service;

import com.geulkkoli.domain.hashtag.HashTag;
import com.geulkkoli.domain.hashtag.HashTagRepository;
import com.geulkkoli.domain.hashtag.HashTagType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest
@Transactional
class HashTagFindServiceTest {

    @Autowired
    HashTagFindService hashTagFindService;

    @Autowired
    HashTagRepository hashTagRepository;

    @AfterEach
    void afterEach() {
        hashTagRepository.deleteAllInBatch();
    }


    @DisplayName("해시태그 카테고리와 상태 일반 해시태그 이름을 받아 게시글을 찾기")
    @Test
    void findHashTagByCatogoryStautsAndGeneral() {
        HashTag hashTag = new HashTag("일반글",  HashTagType.GENERAL);
        HashTag saveHashTag = hashTagRepository.save(hashTag);
        HashTag hashTag2 = new HashTag("공지글",   HashTagType.MANAGEMENT);
        HashTag saveHashTag2 = hashTagRepository.save(hashTag2);

        List<HashTag> hashTags = hashTagFindService.findHashTagByCatogoryStautsAndGeneral(hashTag.getHashTagName(), hashTag2.getHashTagName(), "");

        assertThat(hashTags).contains(saveHashTag,saveHashTag2);
    }


    @DisplayName("해시태그 두 개로 해시태그 찾기")
    @Test
    void findHashTags() {
        HashTag hashTag = new HashTag("소설",  HashTagType.GENERAL);
        HashTag subHastTag = new HashTag("로맨스", HashTagType.GENERAL);
        HashTag saveHashTag = hashTagRepository.save(hashTag);
        HashTag saveSubHashTag = hashTagRepository.save(subHastTag);

        List<HashTag> hashTags = hashTagFindService.findHashTags(saveHashTag.getHashTagName(), saveSubHashTag.getHashTagName());


        assertThat(hashTags).contains(saveHashTag,saveSubHashTag);
    }
}