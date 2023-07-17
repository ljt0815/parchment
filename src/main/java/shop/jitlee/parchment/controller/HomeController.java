package shop.jitlee.parchment.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        System.out.println("hello");
        return "index";
    }

    @GetMapping("/main")
    public String main() {
        return "main";
    }
}
