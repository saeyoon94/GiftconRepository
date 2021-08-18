package myfuture.gifticonhub.domain.member.repository;

import myfuture.gifticonhub.domain.member.model.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Repository
public class JpaMemberRepository implements MemberRepository{

    @Autowired
    private final EntityManager em;

    @Override
    public Member save(Member member) {
        em.persist(member);
        return member;
    }

    @Override
    public Optional<Member> findById(Long id) {
        Member member = em.find(Member.class, id);
        return Optional.ofNullable(member);
    }

    @Override
    public Optional<Member> findByUserId(String userId) {
        try {
            Member member = em.createQuery("select m from Member m where m.userId = :userId", Member.class)
                    .setParameter("userId", userId)
                    .getSingleResult();
            return Optional.of(member);
        } catch (NoResultException e) {
            log.error("없는 아이디입니다.");
            return Optional.empty();
        } catch (NonUniqueResultException e) {
            log.error("중복된 아이디가 있습니다.");
            return Optional.empty();
        }
    }
}
