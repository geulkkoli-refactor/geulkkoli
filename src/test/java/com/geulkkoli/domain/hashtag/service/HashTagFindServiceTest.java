package com.geulkkoli.domain.hashtag.service;

import com.geulkkoli.domain.hashtag.HashTag;
import com.geulkkoli.domain.hashtag.HashTagRepository;
import com.geulkkoli.domain.hashtag.HashTagType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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


    @DisplayName("게시글 이름을 받아 게시글을 찾기")
    @Test
    void findHashTags() {
        HashTag hashTag = new HashTag("일반글",  HashTagType.GENERAL);
        HashTag saveHashTag = hashTagRepository.save(hashTag);
        HashTag hashTag2 = new HashTag("공지글",   HashTagType.MANAGEMENT);
        HashTag saveHashTag2 = hashTagRepository.save(hashTag2);

        List<HashTag> hashTags = hashTagFindService.findHashTags(hashTag.getHashTagName(), hashTag2.getHashTagName(), "");

        assertThat(hashTags).contains(saveHashTag,saveHashTag2);
    }
}