package com.geulkkoli.domain.hashtag.service;

import com.geulkkoli.domain.hashtag.HashTag;
import com.geulkkoli.domain.hashtag.HashTagRepository;
import com.geulkkoli.domain.hashtag.util.HashTagSign;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Service
public class HashTagFindService {
    private final HashTagRepository hashTagRepository;

    public HashTagFindService(HashTagRepository hashTagRepository) {
        this.hashTagRepository = hashTagRepository;
    }

    public List<HashTag> findHashTag(String searchWords) {
        return Arrays.stream(searchWords.split(HashTagSign.GENERAL.getSign()))
                .map(String::strip)
                .map(hashTagRepository::findByHashTagName)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
    public List<HashTag> findHashTags(final String mainTag, final String subTag) {
        List<String> hashTagNames = new ArrayList<>();
        hashTagNames.add(mainTag);
        hashTagNames.add(subTag);
        return hashTagRepository.findAllHashTagByHashTagNames(hashTagNames);
    }

    public List<HashTag> findHashTagByCatogoryStautsAndGeneral(final String category, final String status, final String generalHashTag) {
        List<String> hashTagNames = new ArrayList<>();
        hashTagNames.add(category);
        hashTagNames.add(status);
        hashTagNames.add(generalHashTag);
        return hashTagRepository.findAllHashTagByHashTagNames(hashTagNames);
    }
}
