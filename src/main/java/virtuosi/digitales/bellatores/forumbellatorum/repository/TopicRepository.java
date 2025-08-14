package virtuosi.digitales.bellatores.forumbellatorum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import virtuosi.digitales.bellatores.forumbellatorum.entity.Topic;

import java.util.List;

public interface TopicRepository extends JpaRepository<Topic, Long> {

    List<Topic> findByApprovedTrue();

    List<Topic> findByApprovedFalse();

    List<Topic> findByCategoryIdAndApprovedTrue(Long categoryId);

    List<Topic> findByTitleContainingIgnoreCase(String title);

    List<Topic> findByParentIsNullAndCategoryId(Long categoryId);
}
