package virtuosi.digitales.bellatores.forumbellatorum.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import virtuosi.digitales.bellatores.forumbellatorum.entity.Message;
import virtuosi.digitales.bellatores.forumbellatorum.entity.Topic;
import virtuosi.digitales.bellatores.forumbellatorum.entity.User;
import virtuosi.digitales.bellatores.forumbellatorum.repository.MessageRepository;
import virtuosi.digitales.bellatores.forumbellatorum.repository.TopicRepository;
import virtuosi.digitales.bellatores.forumbellatorum.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final TopicRepository topicRepository;
    private final UserRepository userRepository;

    // 🔹 Crear mensaje
    public Message createMessage(String content, Long topicId) {
        // Obtener usuario actual
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Obtener topic
        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new RuntimeException("Tópico no encontrado"));

        // Crear y guardar mensaje
        Message message = new Message();
        message.setContent(content);
        message.setCreatedAt(LocalDateTime.now());
        message.setUser(user);
        message.setTopic(topic);

        return messageRepository.save(message);
    }

    // 🔹 Listar mensajes por topic
    public List<Message> listMessagesByTopic(Long topicId) {
        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new RuntimeException("Tópico no encontrado"));
        return messageRepository.findByTopic(topic);
    }

    // 🔹 Eliminar mensaje
    public void deleteMessage(Long messageId) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("Mensaje no encontrado"));
        messageRepository.delete(message);
    }

    // 🔹 Verificar si un usuario puede moderar un mensaje (opcional)
    public boolean canModerateMessage(Message message, String username) {
        // Se puede delegar a un método de CategoryService
        return message.getTopic().getCategory().getModerators()
                .stream()
                .anyMatch(u -> u.getUsername().equals(username));
    }
}
