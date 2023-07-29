package com.geulkkoli.domain.hashtag;

import java.util.List;

public interface HashTagRepositoryCustom {
    List<HashTag> findAllHashTagByHashTagNames(List<String> hashTagNames);

}
