package myfuture.gifticonhub.domain.item.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import myfuture.gifticonhub.domain.member.model.Member;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

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

    @ManyToOne(fetch = FetchType.LAZY)
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

    private LocalDateTime lastModified;

    private Long price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PRESENTED_MEMBER_ID")
    private Member presentedBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BOUGHT_MEMBER_ID")
    private Member boughtFrom;

    @Enumerated(EnumType.STRING)
    private ItemCategory itemCategory;

    @Enumerated(EnumType.STRING)
    private ItemStatus itemStatus;

    @Column(length = 20) //시리얼번호 규칙을 파악하자
    private String serialNumber;

    @Embedded
    private UploadFile uploadFile;

    public Item(Member member, String itemName, String brandName, LocalDate registeredDate,
                LocalDate expirationDate, Long price, ItemCategory itemCategory, ItemStatus itemStatus,
                String serialNumber, UploadFile uploadFile) {
        this.member = member;
        this.itemName = itemName;
        this.brandName = brandName;
        this.registeredDate = registeredDate;
        this.price = price;
        this.expirationDate = expirationDate;
        this.itemCategory = itemCategory;
        this.itemStatus = itemStatus;
        this.serialNumber = serialNumber;
        this.uploadFile = uploadFile;
    }
}
