package me.shinsunyung.commitnote.dto;

import java.util.List;

// 블로그 작성 도움 기능을 사용할 때, AI가 생성한 문자 및 표현 제안을 구조화된 형태로 전달하기 위한 레코드
// LLM 응답을 suggestions 리스트에 매핑하여 일관된 포맷으로 처리하기 위해 사용
public record WritingSuggestionResponse(List<String> suggestions) {}
