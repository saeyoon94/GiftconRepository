package myfuture.gifticonhub.domain.member.repository;

import myfuture.gifticonhub.domain.member.model.Member;

import java.util.Optional;

public interface MemberRepository {
    Member save(Member member);
    Optional<Member> findById(Long id);
    Optional<Member> findByUserId(String userId);
}
