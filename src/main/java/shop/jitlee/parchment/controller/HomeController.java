package shop.jitlee.parchment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import shop.jitlee.parchment.config.auth.PrincipalDetails;
import shop.jitlee.parchment.entity.Book;
import shop.jitlee.parchment.entity.Member;
import shop.jitlee.parchment.service.BookService;
import shop.jitlee.parchment.service.MemberService;


@Controller
@RequiredArgsConstructor
public class HomeController {

    private final MemberService memberService;
    private final BookService bookService;

    @GetMapping("/")
    public String main(@AuthenticationPrincipal PrincipalDetails principalDetails,
                       @PageableDefault(size=12, sort="id", direction = Sort.Direction.DESC) Pageable pageable,
                       Model model) {

        Member member = memberService.find(principalDetails.getUsername());
        Page<Book> pagingBook = bookService.getBookList(member, pageable);
//        List<Book> books = pagingBook.getContent();
        model.addAttribute("books", pagingBook);
        return "index";
    }
}
