package myfuture.gifticonhub.domain.item.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import myfuture.gifticonhub.domain.member.model.Member;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Slf4j
@SequenceGenerator(
        name = "ITEM_SEQ_GENERATOR",
        sequenceName = "ITEM_SEQ",
        initialValue = 1, allocationSize = 1 //나중에 서비스할떄는 50~100사이로 두자
)

@Table(
        uniqueConstraints={
                @UniqueConstraint(columnNames={"serialNumber"})
        }
)


public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "ITEM_SEQ_GENERATOR")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @Column(length = 50)
    private String itemName;

    @Column(length = 30)
    private String brandName;

    @Column(nullable = false)
    private LocalDate registeredDate;

    @Column(nullable = false)
    private LocalDate expirationDate;

    @ManyToOne
    @JoinColumn(name = "PRESENTED_MEMBER_ID")
    private Member presentedBy;

    @ManyToOne
    @JoinColumn(name = "BOUGHT_MEMBER_ID")
    private Member boughtFrom;

    @Enumerated(EnumType.STRING)
    private ItemCategory itemCategory;

    @Column(length = 20) //시리얼번호 규칙을 파악하자
    private String serialNumber;

    @Embedded
    private UploadFile uploadFile;

    public Item(Member member, String itemName, String brandName, LocalDate registeredDate,
                LocalDate expirationDate, ItemCategory itemCategory, String serialNumber, UploadFile uploadFile) {
        this.member = member;
        this.itemName = itemName;
        this.brandName = brandName;
        this.registeredDate = registeredDate;
        this.expirationDate = expirationDate;
        this.itemCategory = itemCategory;
        this.serialNumber = serialNumber;
        this.uploadFile = uploadFile;
    }
}
