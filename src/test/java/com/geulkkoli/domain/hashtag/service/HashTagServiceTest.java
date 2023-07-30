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

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
 class HashTagServiceTest {
    @Autowired
    HashTagService hashTagService;

    @Autowired
    HashTagRepository hashTagRepository;

    @AfterEach
    void tearDown() {
        hashTagRepository.deleteAllInBatch();
    }

    @DisplayName("해시태그 새로 만들어 저장하기")
    @Test
    void save() {
        HashTag newHashTag = hashTagService.createNewHashTag("새로운 해시태그","일반");

        assertThat(newHashTag.getHashTagName()).isEqualTo("새로운 해시태그");
        assertThat(newHashTag.getHashTagType()).isEqualTo(HashTagType.GENERAL);
    }
}
