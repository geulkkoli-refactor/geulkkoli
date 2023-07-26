package com.geulkkoli.domain.hashtag;

import java.util.List;

public interface HashTagRepositoryCustom {
    List<Long> hashIdsByHashTagNames(List<String> hashTagNames);

}
