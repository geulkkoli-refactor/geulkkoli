package com.geulkkoli.web.user.dto.mypage.calendar;

import lombok.Getter;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@Getter
public class CalendarDto {

    @NotEmpty
    private String email;

    @NotEmpty
    private String signUpDate;

    private List<LocalDate> allPostDatesByOneUser;

    public CalendarDto(String email, String signUpDate, List<String> allPostDatesByOneUser) {
        this.email = email;
        this.signUpDate = signUpDate;
        this.allPostDatesByOneUser = pare(allPostDatesByOneUser);
    }

    private List<LocalDate> pare(List<String> allPostDatesByOneUser) {
        return allPostDatesByOneUser.stream()
                .map(date -> date.substring(0,10))
                .map(date2 -> LocalDate.parse(date2, DateTimeFormatter.ofPattern("yyyy/MM/dd")))
                .collect(Collectors.toList());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CalendarDto)) return false;
        CalendarDto that = (CalendarDto) o;
        return Objects.equals(email, that.email) && Objects.equals(signUpDate, that.signUpDate) && Objects.equals(allPostDatesByOneUser, that.allPostDatesByOneUser);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, signUpDate, allPostDatesByOneUser);
    }

    @Override
    public String toString() {
        return "CalendarDto{" +
                "email='" + email + '\'' +
                ", signUpDate='" + signUpDate + '\'' +
                ", allPostDatesByOneUser=" + allPostDatesByOneUser +
                '}';
    }
}
