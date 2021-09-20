package myfuture.gifticonhub.domain.item.service;

import org.junit.jupiter.api.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class regexTest {
    @Test
    void regexDate() {
        String[] strings = {"2021-07-04", "~ 2021-07-04", "2021.07.04", "~2021-07-04", " 2021-07-04", "20210321", "2021 03 23",
                "2021/03/23", "2021 / 03 / 23", "21 - 03 - 23", "210103", "21-01-03", "2021-03-23 ~ 2021-03-25", "2021년 4월 14일", "21년 3월 4일"};

        for (String anni_date : strings) {
            Pattern pattern = Pattern.compile("^*(~|~ | )?((19|20)?\\d\\d)?(년 |[- /.년]| - | / | . )?(0[1-9]|1[012]|[1-9]월)(월 |[- /.월]| - | / | . )?(0[1-9]|[12][0-9]|3[01]|[1-9]일)(일)?$");
            Matcher matcher = pattern.matcher(anni_date);
            if (matcher.find()) {
                System.out.println(anni_date + " = " + matcher.group() + " : ok");
            } else {
                System.out.println(anni_date + " : fail");
            }
        }

    }


    @Test
    void regexSerial() {
        String[] strings = {"1234-1234-1234-1234", "1234-1234-1234", "1234 1234 1234 번호복사", "1234 1234 1234 12", "1234 1234 1234",
                            "1234 1234 1234 1234", "1234 - 1234 - 1234", "1234 1234 1234 1234 12", "123412341234", "1234 - 1234 - 1234 - 1234"};

        for (String anni_date : strings) {
            Pattern pattern = Pattern.compile("^(\\d\\d\\d\\d)(-| - | )?(\\d\\d\\d\\d)(-| - | )?(\\d\\d\\d\\d)((-| - | )?(\\d\\d\\d\\d))?((-| - | )?(\\d\\d))?$*");
            Matcher matcher = pattern.matcher(anni_date);
            if (matcher.find()) {
                System.out.println(anni_date + " = " + matcher.group() + " : ok");
            } else {
                System.out.println(anni_date + " : fail");
            }
        }

    }


}
