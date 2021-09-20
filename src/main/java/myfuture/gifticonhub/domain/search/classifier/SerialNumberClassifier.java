package myfuture.gifticonhub.domain.search.classifier;

import lombok.extern.slf4j.Slf4j;
import myfuture.gifticonhub.domain.search.model.ClassVo;
import myfuture.gifticonhub.domain.search.model.SerialNumberClassVo;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class SerialNumberClassifier implements Classifier{

    private static final String SERIAL_NUMBER_PATTERN =
            "^(\\d\\d\\d\\d)(-| - | )?(\\d\\d\\d\\d)(-| - | )?(\\d\\d\\d\\d)((-| - | )?(\\d\\d\\d\\d))?((-| - | )?(\\d\\d))?$*";

    private static final Pattern pattern = Pattern.compile(SERIAL_NUMBER_PATTERN);
    private Matcher matcher;

    @Override
    public boolean canClassify(String text) {
        log.info("SerialNumberClassifier canClassify");
        matcher = pattern.matcher(text);
        return matcher.find();
    }

    @Override
    public ClassVo classify(String text) {
        ClassVo classVo = new SerialNumberClassVo(this.parseSerialNumber(text));
        return classVo;
    }

    private String parseSerialNumber(String serialNumber) {
        return serialNumber.replaceAll("\\D", "");


    }
}
