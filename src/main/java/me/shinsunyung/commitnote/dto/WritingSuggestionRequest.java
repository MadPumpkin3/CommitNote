package me.shinsunyung.commitnote.dto;

import java.util.Map;

// 블로그 작성 도움 기능 사용 시, 필요한 요청을 담을 요청용 레코드
public record WritingSuggestionRequest(
        String title,
        String content,
        String question
) {
    public Map<String, Object> toMap(String format) {
        return Map.of(
                "title", title,
                "content", content,
                "question", question,
                "format", format
        );
    }
}
