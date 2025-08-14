package virtuosi.digitales.bellatores.forumbellatorum.dto;

import java.util.List;

public record CategoryResponse(
        Long id,                  // ID de la categoría
        String name,              // Nombre de la categoría
        Long parentId,            // ID del padre (null si es raíz)
        boolean approved,         // Estado de aprobación
        List<CategoryResponse> subcategories // Subcategorías (vacío si no hay)
) {}
