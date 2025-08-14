package virtuosi.digitales.bellatores.forumbellatorum.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import virtuosi.digitales.bellatores.forumbellatorum.entity.Role;
import virtuosi.digitales.bellatores.forumbellatorum.entity.RoleName;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleName name);
    boolean existsByName(RoleName name);
}
