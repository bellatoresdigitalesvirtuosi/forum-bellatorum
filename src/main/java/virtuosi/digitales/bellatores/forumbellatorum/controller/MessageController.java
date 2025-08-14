package virtuosi.digitales.bellatores.forumbellatorum.controller;

import virtuosi.digitales.bellatores.forumbellatorum.dto.MessageRequest;
import virtuosi.digitales.bellatores.forumbellatorum.dto.MessageResponse;
import virtuosi.digitales.bellatores.forumbellatorum.entity.Message;
import virtuosi.digitales.bellatores.forumbellatorum.services.MessageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    // Crear mensaje (permiso MESSAGE_CREATE)
    @PostMapping
    @PreAuthorize("hasAuthority('MESSAGE_CREATE')")
    public ResponseEntity<MessageResponse> create(@Valid @RequestBody MessageRequest request) {
        Message message = messageService.createMessage(request.getContent(), request.getTopicId());
        return ResponseEntity.ok(new MessageResponse(message));
    }

    // Listar mensajes por tópico
    @GetMapping("/topic/{topicId}")
    public ResponseEntity<List<MessageResponse>> listByTopic(@PathVariable Long topicId) {
        List<Message> messages = messageService.listMessagesByTopic(topicId);
        return ResponseEntity.ok(messages.stream().map(MessageResponse::new).toList());
    }

    // Eliminar mensaje (permiso MESSAGE_DELETE)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('MESSAGE_DELETE')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        messageService.deleteMessage(id);
        return ResponseEntity.noContent().build();
    }
}
