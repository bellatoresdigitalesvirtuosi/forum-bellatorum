package virtuosi.digitales.bellatores.forumbellatorum.dto;

import java.util.List;

public record TopicResponse(
        Long id,
        String title,
        String content,
        Long categoryId,
        Long parentId,
        boolean approved,
        List<TopicResponse> subtopics
) {}
