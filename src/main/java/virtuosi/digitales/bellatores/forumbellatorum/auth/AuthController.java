package virtuosi.digitales.bellatores.forumbellatorum.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import virtuosi.digitales.bellatores.forumbellatorum.auth.dto.AuthRequest;
import virtuosi.digitales.bellatores.forumbellatorum.auth.dto.AuthResponse;
import virtuosi.digitales.bellatores.forumbellatorum.auth.dto.RegisterRequest;
import virtuosi.digitales.bellatores.forumbellatorum.services.AuthenticationService;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authenticationService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }
}

