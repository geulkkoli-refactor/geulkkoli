package com.geulkkoli.domain.topic;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.Objects;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "topic")
public class Topic {
    @Id
    @Column(name = "topic_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long topicId;

    @NotBlank
    @Column(name = "topic_name")
    private String topicName;

    @Column(name = "topic_useDate")
    private LocalDate useDate;

    @Column(name = "topic_upComingDate")
    private LocalDate upComingDate;

    @Builder
    public Topic(String topicName) {
        this.topicName = topicName;
    }

    public void settingUpComingDate(LocalDate upComingDate) {
        this.upComingDate = upComingDate;
    }

    public void settingUseDate(LocalDate useDate) {
        this.useDate = useDate;
    }

    @PrePersist
    private void setDefaultValues() {
        if (useDate == null) {
            useDate = LocalDate.of(2000, 1, 1);
        }
        if (upComingDate == null) {
            upComingDate = LocalDate.of(2000, 1, 1);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Topic)) return false;
        Topic topic = (Topic) o;
        return Objects.equals(topicId, topic.topicId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(topicId);
    }

    @Override
    public String toString() {
        return "Topic{" +
                "topicId=" + topicId +
                ", topicName='" + topicName + '\'' +
                ", useDate=" + useDate +
                ", upComingDate=" + upComingDate +
                '}';
    }
}
