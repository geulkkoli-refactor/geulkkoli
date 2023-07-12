package com.geulkkoli.domain.topic;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class TopicTest {

    @DisplayName("Topic 객체 생성 테스트")
    @Test
    void getTopic() {
        Topic topic = Topic.builder()
                .topicName("테스트")
                .build();

        assertAll(
                () -> assertThat(topic).has(new Condition<>(t -> t.getTopicName().equals("테스트"), "topicName equals")),
                () -> assertThat(topic).has(new Condition<>(t -> t.getUseDate() == null, "useDate equals")),
                () -> assertThat(topic).has(new Condition<>(t -> t.getUpComingDate() == null, "upComingDate equals")));
    }

    @DisplayName("이용 날짜 설정 테스트")
    @Test
    void settingUseDate() {
        Topic topic = Topic.builder()
                .topicName("test")
                .build();

        topic.settingUseDate(LocalDate.now());

        assertThat(topic.getUseDate()).isNotNull();
        assertThat(topic.getUseDate()).isEqualTo(LocalDate.now());
    }
}