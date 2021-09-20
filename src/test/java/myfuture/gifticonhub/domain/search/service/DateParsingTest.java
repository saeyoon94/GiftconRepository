package myfuture.gifticonhub.domain.search.service;


import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class DateParsingTest {

    @Test
    void parse() {
        //날짜 문자열 -> yyyymmdd -> localDate
        String[] strings = {"2021-07-04", "~ 2021-07-04", "2021.07.04", "~2021-07-04", " 2021-07-04", "20210321", "2021 03 23",
                "2021/03/23", "2021 / 03 / 23", "21 - 03 - 23", "210103", "21-01-03", "2021-03-23 ~ 2021-03-25", "2021년 4월 14일", "21년 3월 4일"};

        for (String str : strings) {
            Pattern pattern = Pattern.compile("^*(~|~ | )?((19|20)?\\d\\d)?(년 |[- /.년]| - | / | . )?(0[1-9]|1[012]|[1-9]월)(월 |[- /.월]| - | / | . )?(0[1-9]|[12][0-9]|3[01]|[1-9]일)(일)?$");
            Matcher matcher = pattern.matcher(str);
            matcher.find();
            String date = matcher.group();
            System.out.println("date = " + date);
            String[] splited = date.split("\\D");
            String parsed = Arrays.stream(splited).filter(s -> !s.isBlank()).map(s -> s.length() == 1 ? "0" + s : s)
                    .collect(Collectors.joining());
            System.out.println("parsed = " + parsed);
            }
        }

}
