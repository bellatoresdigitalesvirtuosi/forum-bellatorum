package virtuosi.digitales.bellatores.forumbellatorum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import virtuosi.digitales.bellatores.forumbellatorum.entity.Message;
import virtuosi.digitales.bellatores.forumbellatorum.entity.Topic;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findByApprovedTrue();

    List<Message> findByApprovedFalse();

    List<Message> findByTopicIdAndApprovedTrue(Long topicId);

    List<Message> findByParentIsNullAndTopicId(Long topicId);

    List<Message> findByTopic(Topic topic);
}
