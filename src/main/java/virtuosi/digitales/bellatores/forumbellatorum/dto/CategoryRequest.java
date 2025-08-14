package virtuosi.digitales.bellatores.forumbellatorum.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CategoryRequest(
        @NotBlank(message = "El nombre de la categoría es obligatorio")
        @Size(max = 100, message = "El nombre no puede superar 100 caracteres")
        String name,

        // parentId puede ser null si es categoría raíz
        Long parentId
) {}

