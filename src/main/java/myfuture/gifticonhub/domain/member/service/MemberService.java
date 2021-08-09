package myfuture.gifticonhub.domain.member.service;

import myfuture.gifticonhub.domain.member.model.Member;

import java.util.Optional;

public interface MemberService {

    public Member join(Member member);
    public Optional<Member> findOne(Long memberId);
}
