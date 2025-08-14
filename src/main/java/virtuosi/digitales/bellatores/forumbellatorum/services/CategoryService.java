package virtuosi.digitales.bellatores.forumbellatorum.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import virtuosi.digitales.bellatores.forumbellatorum.entity.Category;
import virtuosi.digitales.bellatores.forumbellatorum.repository.CategoryRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    // 🔹 Crear categoría (pendiente por defecto)
    public Category createCategory(String name, Long parentId) {
        Category category = new Category();
        category.setName(name);
        category.setApproved(false);
        if (parentId != null) {
            category.setParent(categoryRepository.findById(parentId)
                    .orElseThrow(() -> new RuntimeException("Categoría padre no encontrada")));
        }
        return categoryRepository.save(category);
    }

    // 🔹 Aprobar categoría
    public Category approveCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
        category.setApproved(true);
        return categoryRepository.save(category);
    }

    // 🔹 Listar solo categorías aprobadas
    public List<Category> listApprovedCategories() {
        return categoryRepository.findByApprovedTrue();
    }

    // 🔹 Obtener categoría por ID
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
    }

    // 🔹 Actualizar categoría (solo nombre y padre)
    public Category updateCategory(Long id, String name, Long parentId) {
        Category category = getCategoryById(id);
        category.setName(name);
        if (parentId != null) {
            Category parent = getCategoryById(parentId);
            // Evitar ciclos: no permitir que la categoría sea padre de sí misma o descendiente
            if (parent.getId().equals(id) || isDescendant(id, parent)) {
                throw new RuntimeException("No se puede asignar esta categoría como padre");
            }
            category.setParent(parent);
        } else {
            category.setParent(null); // categoría raíz
        }
        return categoryRepository.save(category);
    }

    // 🔹 Eliminar categoría
    public void deleteCategory(Long id) {
        Category category = getCategoryById(id);
        categoryRepository.delete(category);
    }

    // 🔹 Listar categorías como árbol jerárquico
    public List<Category> listCategoryTree() {
        return categoryRepository.findByParentIsNull();
    }

    // 🔹 Buscar categorías por nombre
    public List<Category> searchCategories(String query) {
        return categoryRepository.findByNameContainingIgnoreCase(query);
    }

    // 🔹 Listar categorías pendientes (solo ADMIN)
    public List<Category> listPendingCategories() {
        return categoryRepository.findByApprovedFalse();
    }

    // 🔹 Método auxiliar para evitar ciclos al actualizar padre
    private boolean isDescendant(Long categoryId, Category potentialParent) {
        if (potentialParent.getParent() == null) return false;
        if (potentialParent.getParent().getId().equals(categoryId)) return true;
        return isDescendant(categoryId, potentialParent.getParent());
    }
}
