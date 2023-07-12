package com.geulkkoli.web.admin;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.*;
import java.util.Objects;

@Getter
@Setter
public class DailyTopicDto {

    private String date;
    private String topic;

    public DailyTopicDto(){}

    @Builder
    public DailyTopicDto(String date, String topic) {
        this.date = date;
        this.topic = topic;
    }

    public static String dateToString (LocalDate date) {
        return date.toString();
    }

    public static LocalDate stringToDate (String date) {
        String [] split = date.split("-");
        return LocalDate.of(
                Integer.parseInt(split[0].strip()),
                Integer.parseInt(split[1].strip()),
                Integer.parseInt(split[2].strip())
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DailyTopicDto)) return false;
        DailyTopicDto that = (DailyTopicDto) o;
        return Objects.equals(date, that.date) && Objects.equals(topic, that.topic);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, topic);
    }

    @Override
    public String toString() {
        return "DailyTopicDto{" +
                "date='" + date + '\'' +
                ", topic='" + topic + '\'' +
                '}';
    }
}
