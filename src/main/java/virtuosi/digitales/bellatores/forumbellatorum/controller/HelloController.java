package virtuosi.digitales.bellatores.forumbellatorum.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import virtuosi.digitales.bellatores.forumbellatorum.security.UserPrincipal;

@RestController
@RequestMapping("/api")
public class HelloController {

    @GetMapping("/hello")
    public String sayHello(@AuthenticationPrincipal UserPrincipal user) {
        return "Hola, " + user.getUsername() + "! Accediste a un endpoint protegido.";
    }
}
