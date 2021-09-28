package myfuture.gifticonhub.domain.member.service;

import myfuture.gifticonhub.domain.member.model.Member;
import myfuture.gifticonhub.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Transactional
@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService{

    @Autowired
    private final MemberRepository memberRepository;
    @Autowired
    private final EncryptionService encryptionService;

    @Override
    public Member join(Member member) {
        member.setSalt(encryptionService.getSalt());
        member.setPassword(encryptionService.hashing(member.getPassword(), member.getSalt(), EncryptionService.NUMBUR_OF_ITERATIONS));
        Member savedMember = memberRepository.save(member);
        return savedMember;
    }

    @Override
    public Optional<Member> findOne(Long memberId) {
        return memberRepository.findById(memberId);
    }
}
