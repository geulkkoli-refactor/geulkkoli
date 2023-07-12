package com.geulkkoli.domain.admin;

import com.geulkkoli.domain.post.Post;
import com.geulkkoli.domain.user.User;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 신고 이력을 담담하는 엔티티 입니다.
 * 신고 이력은 신고된 게시글, 신고자, 신고 날짜, 신고 이유를 담고 있습니다.
 * 한 게시글에 여러번 신고가 들어올 수 있으므로 일대다 연관관계로 설정했습니다.
 * 관리자는 이 신고 이력을 통해 신고된 게시글을 찾을 수 있습니다.
 * 회원 서비스에서 원한다면 신고자는 자신이 신고한 게시글을 찾을 수 있습니다.
 */
@Getter
@Entity
@Table(uniqueConstraints = {@UniqueConstraint(name = "unique_report", columnNames = {"reported_post_id", "reporter_id"})})
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reportId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reported_post_id", nullable = false)
    private Post reportedPost;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reporter_id", nullable = false)
    private User reporter;

    @CreatedDate
    @Column(nullable = false, updatable = false, name = "reported_at")
    private LocalDateTime reportedAt;

    @Column(nullable = false, length = 100)
    private String reason;

    protected Report() {
    }

    private Report(Post reportedPost, User reporter, LocalDateTime reportedAt, String reason) {
        this.reportedPost = reportedPost;
        this.reporter = reporter;
        this.reportedAt = reportedAt;
        this.reason = reason;
    }

    /**
     * 신고 이력을 생성하는 정적 팩토리 메서드 입니다.
     */

    public static Report of(Post reportedPost, User reporter, LocalDateTime reportedAt, String reason){
        return new Report(reportedPost, reporter, reportedAt, reason);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Report)) return false;
        Report report = (Report) o;
        return Objects.equals(reportId, report.reportId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(reportId);
    }
}
