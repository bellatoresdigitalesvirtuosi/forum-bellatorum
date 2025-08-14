package virtuosi.digitales.bellatores.forumbellatorum.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import virtuosi.digitales.bellatores.forumbellatorum.entity.Category;
import virtuosi.digitales.bellatores.forumbellatorum.entity.Topic;
import virtuosi.digitales.bellatores.forumbellatorum.repository.CategoryRepository;
import virtuosi.digitales.bellatores.forumbellatorum.repository.TopicRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TopicService {

    private final TopicRepository topicRepository;
    private final CategoryRepository categoryRepository;

    public Topic createTopic(String title, String content, Long categoryId, Long parentId) {
        Topic topic = new Topic();
        topic.setTitle(title);
        topic.setContent(content);
        topic.setApproved(false);

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
        topic.setCategory(category);

        if (parentId != null) {
            Topic parent = getTopicById(parentId);
            if (parent.getId().equals(parentId)) { // evitar ciclos simples
                throw new RuntimeException("No se puede asignar este subtópico como padre");
            }
            topic.setParent(parent);
        }

        return topicRepository.save(topic);
    }

    public Topic approveTopic(Long id) {
        Topic topic = getTopicById(id);
        topic.setApproved(true);
        return topicRepository.save(topic);
    }

    public Topic getTopicById(Long id) {
        return topicRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tópico no encontrado"));
    }

    public Topic updateTopic(Long id, String title, String content, Long categoryId, Long parentId) {
        Topic topic = getTopicById(id);
        topic.setTitle(title);
        topic.setContent(content);

        if (categoryId != null) {
            Category category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
            topic.setCategory(category);
        }

        if (parentId != null) {
            Topic parent = getTopicById(parentId);
            topic.setParent(parent);
        } else {
            topic.setParent(null);
        }

        return topicRepository.save(topic);
    }

    public void deleteTopic(Long id) {
        topicRepository.delete(getTopicById(id));
    }

    public List<Topic> listApprovedTopics() {
        return topicRepository.findByApprovedTrue();
    }

    public List<Topic> listPendingTopics() {
        return topicRepository.findByApprovedFalse();
    }

    public List<Topic> listTopicsByCategory(Long categoryId) {
        return topicRepository.findByCategoryIdAndApprovedTrue(categoryId);
    }

    public List<Topic> searchTopics(String query) {
        return topicRepository.findByTitleContainingIgnoreCase(query);
    }

    public List<Topic> listTopicTree(Long categoryId) {
        return topicRepository.findByParentIsNullAndCategoryId(categoryId);
    }
}
