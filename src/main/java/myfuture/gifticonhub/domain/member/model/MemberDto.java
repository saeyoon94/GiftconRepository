package myfuture.gifticonhub.domain.member.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.time.Period;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberDto {

    @NotBlank //(groups = OrderChecks.FirstOrder.class)
    @Pattern(regexp = "[a-z0-9_-]{5,20}") //, groups = OrderChecks.SecondOrder.class) //5~20자리의 영문 소문자, 숫자 (특수문자-,_)
    //이미 존재하는 아이디인지 체크하는 로직 넣고 나면 이미 존재하는 아이디라고 에러메세지 나가야 함.
    //그리고 아이디에 빈 값을 넣었을 때 다른 오류 말고 필수 정보라는 오류만 나가도록 수정 필요
    private String userId;

    @NotBlank
    @Pattern(regexp = "[a-zA-Z0-9!~@#$%^&*()?+=\\/]{8,16}") //8~16자리의 영문 대소문자, 숫자, 특수문자
    private String password;
    private String passwordValidation;

    @NotBlank
    @Pattern(regexp = "^[a-zA-Z가-힣]+$") //한글, 영문 대소문자만 가능
    private String userName;

    @NotBlank //NotBlank의 경우 문자열에는 사용가능하지만 정수형에는 불가 -> 정수형은 NotNull로도 충분하다
    //근데 정수형이면 @pattern을 사용 못해서 그냥 string으로 바꿈
    //생년월일의 경우 먼저 년도부터 확인해서 에러가 나가고, 이거 만족하면 월, 또 만족하면 일 체크 후 에러가 나간다.
    //다 유효한 값인 경우 다시 검증하여 지금보다 100년 이상 과거이면 '정말이세요?' 미래이면 '미래에서 오셨군요. ^^' 가 나간다.
    //생년월일 오류 다 년도 아래에 나오도록 수정하자.
    //그리고 글로벌 에러가 다 비밀번호 재확인 밑에 나가는데 분리할 수 있도록 하자.
    @Pattern(regexp = "[0-9]{4}")
    private String yyyy;
    @NotBlank
    private String mm; //직접 입력하는게 아니라 셀렉트박스여서 @Pattern 사용하지 않음
    @NotBlank
    @Pattern(regexp = "[0-9]{1,2}")
    private String dd;

    @NotNull
    private Sex sex;

    @Email
    private String mail;
    @NotBlank
    @Pattern(regexp = "^\\d{2,3}-?(\\d{3,4})-?(\\d{4})$")  //전화번호 패턴
    private String phoneNumber;

    //이건 뷰 템플릿에서 넘어오는 값이 아니라 여기서 만드는 것.
    private LocalDate birthDay;

    public Member toEntity() {
        if (birthDay == null) {
            mergeBirthDay();
        }
        return new Member(userId, password, userName, birthDay, sex, mail, phoneNumber);
    }

    //비밀번호 확인 체크
    public Boolean isNotMatchedPassword() {
        return !password.equals(passwordValidation);
    }

    //생년월일
    private void mergeBirthDay() {
        if (!yyyy.isBlank() && !mm.isBlank() && !dd.isBlank()) {
            this.setBirthDay(LocalDate.of(Integer.parseInt(yyyy), Integer.parseInt(mm), Integer.parseInt(dd)));
        } else {
            // yyyy mm dd 값이 빈채로 들어오면 그냥 현재시간으로 저장
            // 정수형으로 파싱할때 에러나는걸 방지하기 위함. 어차피 @NotBlank에 걸려 DB에 저장되지 않고 에러메시지 출력.
            this.setBirthDay(LocalDate.now());
        }

    }

    public DateValidationStatus validateDate() {
        if (birthDay == null) {
            mergeBirthDay();
        }
        //생년월일이 미래
        if (birthDay.isAfter(LocalDate.now())) {
            return DateValidationStatus.FUTURE;
        }
        //생년월일이 과거 100년 이전
        else if (Period.between(birthDay, LocalDate.now()).getYears() > 100) {
            return DateValidationStatus.TOO_EARLY;
        }
        else {
            return DateValidationStatus.VALID;
        }
    }

    public static enum DateValidationStatus {
        TOO_EARLY, VALID, FUTURE
    }

}
