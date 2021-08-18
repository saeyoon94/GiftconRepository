package myfuture.gifticonhub.global.session;

import lombok.Data;
import lombok.RequiredArgsConstructor;

//세션에 담을 Dto.. 그냥 Member객체를 전부 담으면 메모리에 부담이 갈 것 같아서 필요한 일부 내용만 담기로 함.
@Data
public class SessionDto {
    private final Long id;
    private final String userId;

}
