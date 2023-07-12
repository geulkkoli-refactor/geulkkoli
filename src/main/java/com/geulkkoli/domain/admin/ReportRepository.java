package com.geulkkoli.domain.admin;

import com.geulkkoli.domain.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {

    Long countByReportedPost(Post post);

    //distinct: 중복 제거 (distinct를 사용하지 않으면 중복된 값이 나옴)
    @Query("select distinct r.reportedPost from Report r")
    List<Post> findDistinctByReportedPost();
}
