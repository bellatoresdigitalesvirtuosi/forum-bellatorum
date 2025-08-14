package virtuosi.digitales.bellatores.forumbellatorum.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import virtuosi.digitales.bellatores.forumbellatorum.entity.Category;
import virtuosi.digitales.bellatores.forumbellatorum.entity.Message;
import virtuosi.digitales.bellatores.forumbellatorum.entity.Topic;
import virtuosi.digitales.bellatores.forumbellatorum.entity.User;
import virtuosi.digitales.bellatores.forumbellatorum.repository.CategoryRepository;
import virtuosi.digitales.bellatores.forumbellatorum.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class ModerationService {

    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    // Verifica si el usuario es moderador de una categoría
    public boolean canModerateCategory(Long categoryId, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

        return category.getModerators().contains(user);
    }

    // Verifica si el moderador puede moderar un tópico
    public boolean canModerateTopic(Topic topic, String username) {
        return canModerateCategory(topic.getCategory().getId(), username);
    }

    // Verifica si el moderador puede moderar un mensaje
    public boolean canModerateMessage(Message message, String username) {
        return canModerateCategory(message.getTopic().getCategory().getId(), username);
    }
}
