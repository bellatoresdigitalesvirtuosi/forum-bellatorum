package virtuosi.digitales.bellatores.forumbellatorum.controller;

import virtuosi.digitales.bellatores.forumbellatorum.dto.TopicRequest;
import virtuosi.digitales.bellatores.forumbellatorum.dto.TopicResponse;
import virtuosi.digitales.bellatores.forumbellatorum.entity.Topic;
import virtuosi.digitales.bellatores.forumbellatorum.services.TopicService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/topics")
@RequiredArgsConstructor
public class TopicController {

    private final TopicService topicService;

    // Crear tópico (permiso TOPIC_CREATE)
    @PostMapping
    @PreAuthorize("hasAuthority('TOPIC_CREATE')")
    public ResponseEntity<TopicResponse> create(@Valid @RequestBody TopicRequest request) {
        Topic topic = topicService.createTopic(request.getTitle(), request.getContent(), request.getCategoryId());
        return ResponseEntity.ok(new TopicResponse(topic));
    }

    // Obtener un tópico
    @GetMapping("/{id}")
    public ResponseEntity<TopicResponse> getTopic(@PathVariable Long id) {
        Topic topic = topicService.getTopicById(id);
        return ResponseEntity.ok(new TopicResponse(topic));
    }

    // Listar tópicos por categoría
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<TopicResponse>> listByCategory(@PathVariable Long categoryId) {
        List<Topic> topics = topicService.listTopicsByCategory(categoryId);
        return ResponseEntity.ok(topics.stream().map(TopicResponse::new).toList());
    }

    // Eliminar tópico (permiso TOPIC_DELETE)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('TOPIC_DELETE')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        topicService.deleteTopic(id);
        return ResponseEntity.noContent().build();
    }
}
