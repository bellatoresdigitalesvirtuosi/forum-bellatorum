package virtuosi.digitales.bellatores.forumbellatorum.controller;

import virtuosi.digitales.bellatores.forumbellatorum.dto.CategoryRequest;
import virtuosi.digitales.bellatores.forumbellatorum.dto.CategoryResponse;
import virtuosi.digitales.bellatores.forumbellatorum.entity.Category;
import virtuosi.digitales.bellatores.forumbellatorum.services.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    // Crear categoría (permiso CATEGORY_CREATE)
    @PostMapping
    @PreAuthorize("hasAuthority('CATEGORY_CREATE')")
    public ResponseEntity<CategoryResponse> create(@Valid @RequestBody CategoryRequest request) {
        Category category = categoryService.createCategory(request.name(), request.parentId());
        return ResponseEntity.ok(toResponse(category));
    }

    // Aprobar categoría (permiso CATEGORY_APPROVE)
    @PatchMapping("/{id}/approve")
    @PreAuthorize("hasAuthority('CATEGORY_APPROVE')")
    public ResponseEntity<CategoryResponse> approve(@PathVariable Long id) {
        Category category = categoryService.approveCategory(id);
        return ResponseEntity.ok(toResponse(category));
    }

    // Listar solo categorías aprobadas (público)
    @GetMapping
    public ResponseEntity<List<CategoryResponse>> listApproved() {
        List<Category> categories = categoryService.listApprovedCategories();
        return ResponseEntity.ok(toResponseList(categories));
    }

    // Obtener categoría individual
    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> getCategory(@PathVariable Long id) {
        Category category = categoryService.getCategoryById(id);
        return ResponseEntity.ok(toResponse(category));
    }

    // Actualizar categoría (permiso CATEGORY_CREATE)
    @PatchMapping("/{id}")
    @PreAuthorize("hasAuthority('CATEGORY_CREATE')")
    public ResponseEntity<CategoryResponse> update(@PathVariable Long id,
                                                   @Valid @RequestBody CategoryRequest request) {
        Category category = categoryService.updateCategory(id, request.name(), request.parentId());
        return ResponseEntity.ok(toResponse(category));
    }

    // Eliminar categoría (permiso CATEGORY_APPROVE)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('CATEGORY_APPROVE')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }

    // Listar categorías como árbol jerárquico
    @GetMapping("/tree")
    public ResponseEntity<List<CategoryResponse>> listTree() {
        List<Category> categories = categoryService.listCategoryTree();
        return ResponseEntity.ok(toResponseList(categories));
    }

    // Buscar categorías por nombre
    @GetMapping("/search")
    public ResponseEntity<List<CategoryResponse>> search(@RequestParam String query) {
        List<Category> categories = categoryService.searchCategories(query);
        return ResponseEntity.ok(toResponseList(categories));
    }

    // Listar categorías pendientes (permiso CATEGORY_APPROVE)
    @GetMapping("/pending")
    @PreAuthorize("hasAuthority('CATEGORY_APPROVE')")
    public ResponseEntity<List<CategoryResponse>> listPending() {
        List<Category> categories = categoryService.listPendingCategories();
        return ResponseEntity.ok(toResponseList(categories));
    }

    // Métodos privados de conversión DTO
    private CategoryResponse toResponse(Category category) {
        return new CategoryResponse(
                category.getId(),
                category.getName(),
                category.getParent() != null ? category.getParent().getId() : null,
                category.isApproved(),
                toResponseList(category.getSubcategories()) // ya devuelve List<CategoryResponse>
        );
    }


    private List<CategoryResponse> toResponseList(Collection<Category> categories) {
        if (categories == null || categories.isEmpty()) return Collections.emptyList();
        return categories.stream()
                .map(this::toResponse) // <-- mapear a CategoryResponse
                .toList();
    }

}
