package me.shinsunyung.commitnote.service;


import me.shinsunyung.commitnote.dto.WritingSuggestionRequest;
import me.shinsunyung.commitnote.dto.WritingSuggestionResponse;
import org.springframework.core.io.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class WritingAssistantService {

    private final ChatClient chatClient;
    private final PromptTemplate template;

    // LLM의 JSON 응답을 WritingSuggestions DTO로 변환하기 위한 컨버터
    private final BeanOutputConverter<WritingSuggestionResponse> outputConverter
            =
            new BeanOutputConverter<>(WritingSuggestionResponse.class);

    public WritingAssistantService(
            ChatClient.Builder chatClientBuilder,
            @Value("classpath:prompts/writing-assistant.st") Resource
            promptResource
            ) {
        this.chatClient = chatClientBuilder.build();
        this.template = new PromptTemplate(promptResource);
    }

    public WritingSuggestionResponse getWritingAssist(WritingSuggestionRequest request) {
        // 프롬프트 템플릿에 파라미터를 채워넣어 최종 프롬프트 생성
        String prompt = template.create(request.toMap(outputConverter.getFormat())).getContents();

        // LLM에 프롬프트 전송 및 수신
        String response = chatClient.prompt().user(prompt).call().content();

        // 응답을 DTO로 변환
        return outputConverter.convert(response);
    }
}
