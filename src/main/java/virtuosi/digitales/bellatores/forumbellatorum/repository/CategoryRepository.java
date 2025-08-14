package virtuosi.digitales.bellatores.forumbellatorum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import virtuosi.digitales.bellatores.forumbellatorum.entity.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    // Buscar categoría por nombre exacto
    Optional<Category> findByName(String name);

    // Listar categorías aprobadas
    List<Category> findByApprovedTrue();

    // Listar categorías pendientes
    List<Category> findByApprovedFalse();

    // Buscar categorías cuyo nombre contenga el texto (insensible a mayúsculas/minúsculas)
    List<Category> findByNameContainingIgnoreCase(String name);

    // Listar categorías raíz (sin padre) para construir árbol
    List<Category> findByParentIsNull();
}