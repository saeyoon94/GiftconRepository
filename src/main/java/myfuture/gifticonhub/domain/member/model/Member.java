package myfuture.gifticonhub.domain.member.model;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Slf4j
@SequenceGenerator(
        name = "MEMBER_SEQ_GENERATOR",
        sequenceName = "MEMBER_SEQ",
        initialValue = 1, allocationSize = 1 //나중에 서비스할떄는 50~100사이로 두자
)
@Table(
        uniqueConstraints={
                @UniqueConstraint(columnNames={"userId","phoneNumber"})
        }
)
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE,
                    generator = "MEMBER_SEQ_GENERATOR")
    private Long id;
    @Column(nullable = false, length = 15)
    private String userId;
    @Column(nullable = false, length = 100)
    private String password;
    @Column(nullable = false, length = 16)
    private String salt;
    @Column(nullable = false, length = 5)
    private String userName;
    @Column(nullable = false)
    private LocalDate birthDay;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Sex sex;
    private String mail;
    @Column(nullable = false)
    private String phoneNumber;

    public Member(String userId, String password, String userName, LocalDate birthDay, Sex sex, String mail, String phoneNumber) {
        this.userId = userId;
        this.password = password;
        this.userName = userName;
        this.birthDay = birthDay;
        this.sex = sex;
        this.mail = mail;
        this.phoneNumber = phoneNumber;
    }
}
