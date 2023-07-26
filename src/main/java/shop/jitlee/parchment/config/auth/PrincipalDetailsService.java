package shop.jitlee.parchment.config.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import shop.jitlee.parchment.entity.Member;
import shop.jitlee.parchment.repository.MemberRepository;

@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member memberEntity = memberRepository.findByUsername(username).orElseThrow(()->
                new UsernameNotFoundException("not found loginId"));
        if(memberEntity != null) {
            return new PrincipalDetails(memberEntity);
        }
        return null;
    }
}