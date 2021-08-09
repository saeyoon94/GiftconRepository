package myfuture.gifticonhub.domain.member.service;

import myfuture.gifticonhub.domain.member.model.Member;
import myfuture.gifticonhub.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Transactional
//No EntityManager with actual transaction available for current thread - cannot reliably process 'persist' call
//@Transactional을 안 붙이니 위와 같은 에러 발생. JPA가 트랜잭션 단위로 동작해서 그런 것 같은데, 리포지토리가 아니라 서비스 계층에
//하는게 맞는건지, 자세한 메커니즘은 어떻게 되는지 학습 필요
@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService{

    @Autowired
    private final MemberRepository memberRepository;

    @Override
    public Member join(Member member) {
        Member savedMember = memberRepository.save(member);
        return savedMember;
    }

    @Override
    public Optional<Member> findOne(Long memberId) {
        return memberRepository.findById(memberId);
    }
}
