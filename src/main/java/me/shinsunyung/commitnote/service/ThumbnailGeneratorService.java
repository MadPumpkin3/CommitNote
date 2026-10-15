package me.shinsunyung.commitnote.service;


import me.shinsunyung.commitnote.dto.GeneratorThumbnailRequest;
import me.shinsunyung.commitnote.dto.GeneratorThumbnailResponse;
import me.shinsunyung.commitnote.dto.UploadResponse;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.image.Image;
import org.springframework.ai.image.ImageModel;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.image.ImageResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.net.URI;
import java.util.Base64;

@Service
public class ThumbnailGeneratorService {

    private final ImageModel imageModel;
    private final PromptTemplate template;
    private final FileStorageService fileStorageService;
    private final RestClient restClient = RestClient.create();

    public ThumbnailGeneratorService(
            ImageModel imageModel,
            FileStorageService fileStorageService,
            @Value("classpath:prompts/thumbnail-generator.st") Resource
            promptResource // 프롬프트 템플릿 리소스
            ) {
        this.imageModel = imageModel;
        this.fileStorageService = fileStorageService;
        this.template = new PromptTemplate(promptResource);
    }

    public GeneratorThumbnailResponse generatorThumbnail(GeneratorThumbnailRequest request) {
        String prompt = template.create(request.toMap()).getContents();

        ImageResponse response = imageModel.call(new ImagePrompt(prompt));
        Image image = response.getResult().getOutput(); // 생성된 이미지 정보 가져오기

        // 문제발생 : ai로 생성한 이미지의 데이터 구조가 다름 (예상: url / 실제: B64Json)
        /* 삭제
        byte[] bytes = restClient.get() // 이미지 URL로부터 바이트 배열 가져오기
                .uri(URI.create(image.getUrl()))
                .retrieve()
                .body(byte[].class);
        */

        byte[] bytes;

        if (image.getB64Json() != null) {
            bytes = Base64.getDecoder().decode(image.getB64Json());
        } else if (image.getUrl() != null) {
            bytes = restClient.get() // 이미지 URL로부터 바이트 배열 가져오기
                    .uri(URI.create(image.getUrl()))
                    .retrieve()
                    .body(byte[].class);
        } else {
            throw new IllegalStateException("이미지 응답에 URL과 Base64 데이터가 없습니다.");
        }

        // 로컬에 이미지 저장
        UploadResponse saved = fileStorageService.store(bytes, "thumbnail.png");

        return new GeneratorThumbnailResponse(saved.imageUrl());
    }
}
