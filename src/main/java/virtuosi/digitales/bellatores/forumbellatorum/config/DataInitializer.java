package virtuosi.digitales.bellatores.forumbellatorum.config;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import virtuosi.digitales.bellatores.forumbellatorum.entity.Role;
import virtuosi.digitales.bellatores.forumbellatorum.repository.RoleRepository;
import virtuosi.digitales.bellatores.forumbellatorum.entity.RoleName;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        for (RoleName roleName : RoleName.values()) {
            if (!roleRepository.existsByName(roleName)) {
                roleRepository.save(Role.builder().name(roleName).build());
                System.out.println("Rol creado: " + roleName);
            }
        }
    }
}

