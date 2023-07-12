package com.geulkkoli.domain.admin;

import com.geulkkoli.domain.post.Post;
import com.geulkkoli.domain.user.User;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class ReportTest {
    @DisplayName("Report 객체 생성 테스트")
    @Test
    void testReport() {
        User user = User.builder()
                .email("email@gmail.com")
                .userName("userName")
                .password("password")
                .nickName("nickName")
                .phoneNo("0102221111")
                .gender("male")
                .build();

        User user2 = User.builder()
                .email("email2@gmail.com")
                .userName("userName2")
                .password("password2")
                .nickName("nickName2")
                .phoneNo("0102223111")
                .gender("male")
                .build();

        Post post = Post.builder()
                .user(user)
                .nickName("나")
                .postBody("나나")
                .title("테스트").build();

        LocalDateTime now = LocalDateTime.now();
        Report report = Report.of(post, user, now, "테스트를 위한 처단");

        assertAll(() -> assertThat(report).has(new Condition<>(r -> r.getReportedPost().equals(post), "post equals")),
                () -> assertThat(report).has(new Condition<>(r -> r.getReporter().equals(user), "reportedUser equals")),
                () -> assertThat(report).has(new Condition<>(r -> r.getReportedAt().equals(now), "reportedDate equals")),
                () -> assertThat(report).has(new Condition<>(r -> r.getReason().equals("테스트를 위한 처단"), "reportedReason equals")));
    }
}