package myfuture.gifticonhub.domain.member.service;

import lombok.RequiredArgsConstructor;
import myfuture.gifticonhub.domain.member.model.LoginDto;
import myfuture.gifticonhub.domain.member.model.Member;
import myfuture.gifticonhub.domain.member.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService{

    @Autowired
    private final MemberRepository memberRepository;


    @Override
    public Optional<Member> login(LoginDto loginDto) {
        Optional<Member> member = memberRepository.findByUserId(loginDto.getUserId())
                .filter(m -> m.getPassword().equals(loginDto.getPassword()));
        return member;
    }
}
