package myfuture.gifticonhub.global.formatter;

import myfuture.gifticonhub.domain.item.model.UploadFile;
import org.springframework.format.Formatter;

import java.text.ParseException;
import java.util.Locale;

public class uploadFileFormatter implements Formatter<UploadFile> {
    @Override
    public UploadFile parse(String text, Locale locale) throws ParseException {
        String[] splitedText = text.split(":");
        return new UploadFile(splitedText[0],splitedText[1]);
    }

    @Override
    public String print(UploadFile object, Locale locale) {
        return  object.getUploadFileName() + ":" + object.getStoredFileName();
    }
}
