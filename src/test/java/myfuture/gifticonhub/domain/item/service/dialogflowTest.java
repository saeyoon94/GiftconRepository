package myfuture.gifticonhub.domain.item.service;

import com.google.cloud.dialogflow.v2.*;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.UUID;

//@SpringBootTest
public class dialogflowTest {

    @Test
    void nlu() throws IOException {
        SessionsClient sessionsClient = SessionsClient.create();
        String projectId = "ocr-project-324514";
        //String sessionId = UUID.randomUUID().toString();
        String sessionId = "3123144";
        SessionName session = SessionName.ofProjectSessionName(projectId, sessionId);
        System.out.println(session);

        TextInput.Builder textInput = TextInput.newBuilder().setText("스타벅스").setLanguageCode("ko");
        QueryInput queryInput = QueryInput.newBuilder().setText(textInput).build();

        DetectIntentResponse detectIntentResponse = sessionsClient.detectIntent(session, queryInput);
        //QueryResult queryResult = detectIntentResponse.getQueryResult();
        //System.out.println(queryResult);

    }
}
