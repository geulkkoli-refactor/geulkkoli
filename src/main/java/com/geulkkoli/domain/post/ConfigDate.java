package com.geulkkoli.domain.post;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class ConfigDate {

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreatedDate
    private String createdAt;

    @Column(name = "updated_at")
    @LastModifiedDate
    private String updatedAt;

    @PrePersist
    public void onPrePersist() {
        this.createdAt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd a hh:mm:ss"));
        this.updatedAt = createdAt;
    }

    @PreUpdate
    public void onPreUpdate() {
        this.updatedAt = LocalDateTime.now().format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM));
    }

}
