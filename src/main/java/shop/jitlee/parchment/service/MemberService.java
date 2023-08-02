package shop.jitlee.parchment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.jitlee.parchment.entity.Member;
import shop.jitlee.parchment.entity.RoleType;
import shop.jitlee.parchment.repository.MemberRepository;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Transactional
    public Member find(String username) {
        Member member = memberRepository.findByUsername(username).orElseGet(()->{
            return null;
        });
        return member;
    }
    @Transactional
    public void join(Member member) {
        member.setRole(RoleType.USER);
        String rawPassword = member.getPassword();
        member.setPassword(bCryptPasswordEncoder.encode(rawPassword));
        memberRepository.save(member);
        try {
            memberRepository.save(member);
        } catch (DataIntegrityViolationException e) {
            System.out.println("중복에러");
        }
    }

    public Long findByUsernameGetId(String username) {
        return memberRepository.findByUsernameGetId(username);
    }
}
