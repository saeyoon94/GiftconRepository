package myfuture.gifticonhub.domain.member.model;

import javax.validation.GroupSequence;

//validation에 우선순위를 주려고 했으나 필드별로 줄 수가 없어서 원하는 기능을 구현 못함
@GroupSequence({OrderChecks.FirstOrder.class, OrderChecks.SecondOrder.class})
public interface OrderChecks {

    public interface FirstOrder {
    }

    public interface SecondOrder {

    }

}


