package com.geulkkoli.domain.hashtag.service;

import com.geulkkoli.domain.hashtag.HashTag;
import com.geulkkoli.domain.hashtag.HashTagRepository;
import com.geulkkoli.domain.hashtag.HashTagType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class HashTagService {
    private final HashTagRepository hashTagRepository;

    public HashTag createNewHashTag(String input, String hashTagType) {
        return hashTagRepository.save(new HashTag(input, HashTagType.find(hashTagType)));
    }
}
