package com.ecommerce.security;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class RoleTestController {

    @GetMapping("/admin/hello")
    public String adminHello() {
        return "Hello Admin 👑";
    }

    @GetMapping("/user/hello")
    public String userHello() {
        return "Hello User 🙋";
    }

    @GetMapping("/public/hello")
    public String publicHello() {
        return "Hello Public 🌍";
    }
}
