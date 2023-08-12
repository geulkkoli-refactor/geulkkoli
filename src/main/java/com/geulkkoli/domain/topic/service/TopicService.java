package com.geulkkoli.domain.topic.service;

import com.geulkkoli.domain.topic.Topic;
import com.geulkkoli.domain.topic.TopicRepository;
import com.geulkkoli.web.admin.DailyTopicDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class TopicService {
    private final TopicRepository topicRepository;
    public DailyTopicDTO showTodayTopic(LocalDate date) {
        Topic todayTopic = topicRepository.findTopicByUpComingDate(date);
        todayTopic.settingUseDate(date);
        return DailyTopicDTO.builder()
                .date(date.toString())
                .topic(todayTopic.getTopicName())
                .build();
    }
}
