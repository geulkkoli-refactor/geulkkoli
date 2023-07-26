package com.geulkkoli.domain.topic.service;

import com.geulkkoli.domain.topic.Topic;
import com.geulkkoli.domain.topic.TopicRepository;
import com.geulkkoli.web.admin.DailyTopicDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class TopicService {
    private final TopicRepository topicRepository;
    public DailyTopicDto showTodayTopic(LocalDate date) {
        Topic todayTopic = topicRepository.findTopicByUpComingDate(date);
        todayTopic.settingUseDate(date);
        return DailyTopicDto.builder()
                .date(date.toString())
                .topic(todayTopic.getTopicName())
                .build();
    }
}
