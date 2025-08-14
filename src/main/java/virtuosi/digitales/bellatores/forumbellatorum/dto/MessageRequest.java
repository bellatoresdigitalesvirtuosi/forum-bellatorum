package virtuosi.digitales.bellatores.forumbellatorum.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageRequest {

        @NotBlank
        private String content;

        @NotNull
        private Long topicId;
}
