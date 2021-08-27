package myfuture.gifticonhub.domain.item.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import myfuture.gifticonhub.domain.item.model.Item;
import myfuture.gifticonhub.domain.member.model.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Repository
@Slf4j
@RequiredArgsConstructor
public class JpaItemRepository implements ItemRepository{

    @Autowired
    private final EntityManager em;

    @Override
    public Item save(Item item) {
        em.persist(item);
        return item;
    }

    @Override
    public Optional<Item> findById(Long id, Long memberId) {
        Item item = em.find(Item.class, id);
        if (item.getMember().getId() != memberId) {
            log.info("Illegal User Access!!"); //사용자가 자신의 아이템에만 접근할 수 있도록 통제
        }
        return Optional.ofNullable(item);
    }

    @Override
    public List<Item> findByMemberId(Long memberId) {
        List<Item> items = em.createQuery("SELECT i FROM Item i JOIN i.member m ON m.id=:memberId", Item.class)
                .setParameter("memberId", memberId)
                .getResultList();
        return items;
    }

    @Override
    public List<Item> findAll() {
        List<Item> items = em.createQuery("SELECT i FROM Item i", Item.class).getResultList();
        return items;
    }
}
