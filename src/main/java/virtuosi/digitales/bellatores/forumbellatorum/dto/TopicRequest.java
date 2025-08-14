package virtuosi.digitales.bellatores.forumbellatorum.dto;

import jakarta.validation.constraints.NotBlank;

public record TopicRequest(
        @NotBlank(message = "El título es obligatorio")
        String title,

        @NotBlank(message = "El contenido es obligatorio")
        String content,

        Long categoryId, // categoría a la que pertenece
        Long parentId    // subtópico opcional
) {}
