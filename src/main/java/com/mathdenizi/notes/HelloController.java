package com.mathdenizi.notes;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by mathdenizi
 * Date: 21.06.25
 */
@RestController
public class HelloController {

    @GetMapping("/hello")
    public String hello() {
        return "Hello World!";
    }

    @GetMapping("/contact")
    public String sayContact() {
        return "Hello Contact!";
    }
}
