package shop.jitlee.parchment.controller.api;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class testApiController {

    @PostMapping("/test")
    public int test(@PathVariable Long id) {
        return 4;
    }
}
