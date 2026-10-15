package me.shinsunyung.commitnote.controller;

import lombok.RequiredArgsConstructor;
import me.shinsunyung.commitnote.dto.*;
import me.shinsunyung.commitnote.domain.Article;
import me.shinsunyung.commitnote.service.BlogService;
import me.shinsunyung.commitnote.service.ThumbnailGeneratorService;
import me.shinsunyung.commitnote.service.WritingAssistantService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController // HTTP Response Body에 객체 데이터를 JSON 형식으로 반환하는 컨트롤러
public class BlogApiController {

    private final BlogService blogService;
    private final WritingAssistantService writingAssistantService;
    private final ThumbnailGeneratorService thumbnailGeneratorService;

    // HTTP 메서드가 POST일 때 전달받은 URL과 동일하면 메서드로 매핑
    @PostMapping("/api/articles")
    // @RequestBody로 요청 본분 값 매핑
    public ResponseEntity<Article> addArticle(@RequestBody AddArticleRequest request) {
        Article savedArticle = blogService.save(request);

        // 요창한 자원이 성공적으로 생성되었으며 저장된 블로그 글 정보를 응답 객체에 담아 전송
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(savedArticle); // @RestController 애너테이션으로 인해 프론트엔드에서 읽을 수 있는 JSON으로 반환됨.
    }

    // 글 목록 조회 메서드
    @GetMapping("/api/articles")
    public ResponseEntity<List<ArticleResponse>> findAllArticles() {
        List<ArticleResponse> articles = blogService.findAll()
                .stream()
                .map(ArticleResponse::new)
                .toList();

        return ResponseEntity.ok()
                .body(articles);
    }

    @GetMapping("/api/articles/{id}")
    // URL 경로에서 값 추출
    public ResponseEntity<ArticleResponse> findArticle(@PathVariable long id) {
        Article article = blogService.findById(id);

        return ResponseEntity.ok()
                .body(new ArticleResponse(article));
    }

    @DeleteMapping("/api/articles/{id}")
    public ResponseEntity<Void> deleteArticle(@PathVariable long id) {

        blogService.delete(id);

        return ResponseEntity.ok()
                .build();
    }

    @PutMapping("/api/articles/{id}")
    public ResponseEntity<Article> updateArticle(@PathVariable long id,
                                                 @RequestBody UpdateArticleRequest request) {
        Article updateArticle = blogService.update(id, request);

        return ResponseEntity.ok()
                .body(updateArticle);
    }

    @PostMapping("/api/ai-suggestions")
    public ResponseEntity<WritingSuggestionResponse> writingAssist(@RequestBody WritingSuggestionRequest request) {
        WritingSuggestionResponse response = writingAssistantService.getWritingAssist(request);

        return ResponseEntity.ok()
                .body(response);
    }

    @PostMapping("/api/ai-thumbnails")
    public ResponseEntity<GeneratorThumbnailResponse> thumbnailGenerator(@RequestBody GeneratorThumbnailRequest request) {
        GeneratorThumbnailResponse response = thumbnailGeneratorService.generatorThumbnail(request);

        return ResponseEntity.ok()
                .body(response);
    }
}
