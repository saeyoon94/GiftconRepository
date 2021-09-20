package myfuture.gifticonhub.domain.search.classifier;

import lombok.extern.slf4j.Slf4j;
import myfuture.gifticonhub.domain.search.model.ClassVo;
import myfuture.gifticonhub.domain.search.model.ExpirationDateClassVo;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
public class ExpirationDateClassifier implements Classifier{

    private static final String DATE_PATTERN =
            "^*(~|~ | )?((19|20)?\\d\\d)?(년 |[- /.년]| - | / | . )?(0[1-9]|1[012]|[1-9]월)(월 |[- /.월]| - | / | . )?(0[1-9]|[12][0-9]|3[01]|[1-9]일)(일)?$";

    private static final Pattern pattern = Pattern.compile(DATE_PATTERN);
    private Matcher matcher;

    @Override
    public boolean canClassify(String text) {
        log.info("ExpirationDateClassifier canClassify");
        matcher = pattern.matcher(text);
        return matcher.find();
    }

    @Override
    public ClassVo classify(String text) {

        ClassVo classVo = new ExpirationDateClassVo(this.parseDate(matcher.group()));
        return classVo;
    }

    private LocalDate parseDate(String date) {
        //날짜 문자열 -> yyyymmdd -> localDate
        String[] splited = date.split("\\D");
        //빈 스트링은 없애주고 월과 일이 숫자 1개로만 된 경우 앞에 0 붙여주기
        String parsed = Arrays.stream(splited).filter(s -> !s.isBlank()).map(s -> s.length() == 1 ? "0" + s : s)
                .collect(Collectors.joining());

        //연도가 생략된 경우 붙여주기
        if (parsed.length() == 6) {
            parsed = "20" + parsed;
        } else if (parsed.length() == 4) {
            parsed = LocalDate.now().getYear() + parsed;
        }

        return LocalDate.parse(parsed, DateTimeFormatter.ofPattern("yyyyMMdd"));


    }


}
