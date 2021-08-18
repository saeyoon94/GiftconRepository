package myfuture.gifticonhub.domain.member.service;

import myfuture.gifticonhub.domain.member.model.LoginDto;
import myfuture.gifticonhub.domain.member.model.Member;

import java.util.Optional;

public interface LoginService {

    Optional<Member> login(LoginDto loginDto);
}
