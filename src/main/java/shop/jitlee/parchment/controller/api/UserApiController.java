package shop.jitlee.parchment.controller.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import shop.jitlee.parchment.dto.ResponseDto;
import shop.jitlee.parchment.dto.UserJoinDto;
import shop.jitlee.parchment.entity.Member;
import shop.jitlee.parchment.entity.RoleType;
import shop.jitlee.parchment.service.MemberService;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class UserApiController {

    private final MemberService memberService;

    @PostMapping("/user/idDupChk")
    public ResponseDto<Integer> idDupChk(@RequestBody Map<String,String> id) {

        String username = id.get("id");
        Member findMember = memberService.find(username);
        if (findMember == null)
            return new ResponseDto<>(HttpStatus.OK.value(), -1);
        return new ResponseDto<>(HttpStatus.OK.value(), 1);
    }

    @PostMapping("/user/join")
    public ResponseDto<Integer> join(@Valid @RequestBody UserJoinDto userJoinDto) {
        Member member = userJoinDto.toEntity();
        member.setRole(RoleType.USER);
        try {
            memberService.join(member);
        } catch (Exception e) {
            return new ResponseDto<>(HttpStatus.OK.value(), -1);
        }
        return new ResponseDto<>(HttpStatus.OK.value(), 1);
    }
}