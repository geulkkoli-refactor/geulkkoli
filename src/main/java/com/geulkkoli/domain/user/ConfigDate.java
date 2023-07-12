package com.geulkkoli.domain.user;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class ConfigDate {

    @Column(name = "sign_up_date", nullable = false, updatable = false)
    @CreatedDate
    private String signUpDate;

    @PrePersist
    public void onPrePersist() {
            this.signUpDate= LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));

    }
}
