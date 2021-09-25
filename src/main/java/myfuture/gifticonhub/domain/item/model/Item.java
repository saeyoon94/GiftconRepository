package myfuture.gifticonhub.domain.item.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import myfuture.gifticonhub.domain.member.model.Member;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

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
                LocalDate expirationDate, Long price, ItemCategory itemCategory,
                String serialNumber, UploadFile uploadFile) {
        this.member = member;
        this.itemName = itemName;
        this.brandName = brandName;
        this.registeredDate = registeredDate;
        this.price = price;
        this.expirationDate = expirationDate;
        this.itemCategory = itemCategory;
        this.serialNumber = serialNumber;
        this.uploadFile = uploadFile;

        this.updateStatus(LocalDate.now());
    }

    //만기일 변경 시 아이템 상태변경
    public boolean updateStatus(LocalDate now) {
        if (this.getItemStatus() == ItemStatus.Already_Used) {
            return false;
        }
        ItemStatus newItemStatus;
        if (this.getExpirationDate().isAfter(now.plusDays(2))) {
            newItemStatus = ItemStatus.Available;
        } else if (this.getExpirationDate().isBefore(now)) {
            newItemStatus = ItemStatus.Expired;
        } else {
            newItemStatus = ItemStatus.Impending;
        }
        return this.changeStatusIfNecessary(newItemStatus);
    }

    public boolean changeStatusIfNecessary(ItemStatus itemStatus) {
        if (this.getItemStatus() != itemStatus) {
            this.setItemStatus(itemStatus);
            return true;
        }
        return false;
    }

    //아이템 사용여부 체크할 때 아이템 상태 변경
    public void applyUsedStatus(Boolean isUsed) {
        if (this.getItemStatus() != ItemStatus.Already_Used && isUsed) {
            this.setItemStatus(ItemStatus.Already_Used);
        } else if (this.getItemStatus() == ItemStatus.Already_Used && !isUsed) {
            this.setItemStatus(null); //안그러면 updateStatus메소드 첫 분기에 걸려버림
            this.updateStatus(LocalDate.now());
        }
    }
}
