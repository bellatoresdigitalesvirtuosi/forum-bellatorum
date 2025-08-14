package virtuosi.digitales.bellatores.forumbellatorum.dto;

import lombok.Getter;
import lombok.Setter;
import virtuosi.digitales.bellatores.forumbellatorum.entity.Message;

import java.time.LocalDateTime;

@Getter
@Setter
public class MessageResponse {

    private Long id;
    private String content;
    private String username;
    private Long topicId;
    private LocalDateTime createdAt;

    // Constructor que convierte la entidad Message a DTO
    public MessageResponse(Message message) {
        this.id = message.getId();
        this.content = message.getContent();
        this.username = message.getUser().getUsername();
        this.topicId = message.getTopic().getId();
        this.createdAt = message.getCreatedAt();
    }
}
