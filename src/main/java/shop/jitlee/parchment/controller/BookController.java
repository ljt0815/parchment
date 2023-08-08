package shop.jitlee.parchment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import shop.jitlee.parchment.config.auth.PrincipalDetails;
import shop.jitlee.parchment.entity.Book;
import shop.jitlee.parchment.service.BookService;

import javax.servlet.http.HttpServletResponse;

@Controller
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @GetMapping("/book/uploadForm")
    public String uploadForm() {
        return "uploadForm";
    }

    @GetMapping("/books/{folderName}/{fileName}")
    public void bookPage(@AuthenticationPrincipal PrincipalDetails principalDetails,
                         @PathVariable String folderName,
                         @PathVariable String fileName,
                         HttpServletResponse res) {
        if (principalDetails == null) {
            res.setStatus(HttpStatus.NOT_FOUND.value());
            return ;
        }
        bookService.imgFileResponse(folderName, principalDetails.getUsername(), fileName, res);
    }

    @GetMapping("/book/viewer/{uuid}")
    public String viewer(@PathVariable String uuid, Model model) {
        model.addAttribute("uuid", uuid);
        return "viewer";
    }
}
