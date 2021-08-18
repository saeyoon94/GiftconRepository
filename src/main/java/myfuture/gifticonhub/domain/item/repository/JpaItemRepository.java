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
    public Optional<Item> findById(Long id) {
        Item item = em.find(Item.class, id);
        return Optional.ofNullable(item);
    }

    @Override
    public List<Item> findByMemberId(Long memberId) {
        List<Item> items = em.createQuery("select i from item i join i.member m on m.id=:memberId", Item.class)
                .setParameter("memberId", memberId)
                .getResultList();
        return items;
    }
}
