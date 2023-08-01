package shop.jitlee.parchment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import shop.jitlee.parchment.config.auth.PrincipalDetails;
import shop.jitlee.parchment.entity.Member;
import shop.jitlee.parchment.service.MemberService;

@Controller
@RequiredArgsConstructor
public class HomeController {

    @Value("${myPath.imgConnectPath}")
    private String connectPath;

    private final MemberService memberService;

    @GetMapping("/")
    public String main(@AuthenticationPrincipal PrincipalDetails principalDetails, Model model) {

        Member member = memberService.find(principalDetails.getUsername());
        model.addAttribute("connectPath", connectPath);
        model.addAttribute("member", member);
        return "index";
    }
}
