package virtuosi.digitales.bellatores.forumbellatorum.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import virtuosi.digitales.bellatores.forumbellatorum.auth.dto.AuthRequest;
import virtuosi.digitales.bellatores.forumbellatorum.auth.dto.AuthResponse;
import virtuosi.digitales.bellatores.forumbellatorum.auth.dto.RegisterRequest;
import virtuosi.digitales.bellatores.forumbellatorum.entity.*;
import virtuosi.digitales.bellatores.forumbellatorum.repository.*;
import virtuosi.digitales.bellatores.forumbellatorum.security.*;

import java.util.Set;

import static virtuosi.digitales.bellatores.forumbellatorum.entity.User.*;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final CategoryRepository categoryRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authManager;
    private final JwtService jwtService;

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("El usuario ya existe");
        }

        Role userRole = roleRepository.findByName(RoleName.USER)
                .orElseThrow(() -> new RuntimeException("Rol USER no encontrado"));

        Set<Category> categories = request.getCategoryIds() != null && !request.getCategoryIds().isEmpty()
                ? Set.copyOf(categoryRepository.findAllById(request.getCategoryIds()))
                : Set.of();

        User user = builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(Set.of(userRole))
                .categories(categories) // si está mapeado en la entidad
                .build();

        userRepository.save(user);
        String token = jwtService.generateToken(new UserPrincipal(user));
        return new AuthResponse(token);
    }

    public AuthResponse authenticate(AuthRequest request) {
        Authentication authentication = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsernameOrEmail(),
                        request.getPassword()
                )
        );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String token = jwtService.generateToken(userDetails);
        return new AuthResponse(token);
    }
}
