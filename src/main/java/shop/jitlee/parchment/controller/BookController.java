package shop.jitlee.parchment.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BookController {

    @GetMapping("/book/uploadForm")
    public String uploadForm() {
        return "uploadForm";
    }
}
