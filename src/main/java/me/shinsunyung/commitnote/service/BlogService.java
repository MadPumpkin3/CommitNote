package me.shinsunyung.commitnote.service;

import lombok.RequiredArgsConstructor;
import me.shinsunyung.commitnote.dto.AddArticleRequest;
import me.shinsunyung.commitnote.domain.Article;
import me.shinsunyung.commitnote.dto.UpdateArticleRequest;
import me.shinsunyung.commitnote.repository.BlogRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor // 빈을 생성자로 생성하는 애너테이션, final이 붙거나 @NotNull이 붙은 필드의 생성자 추가
@Service // 빈으로 등록
public class BlogService {

    private final BlogRepository blogRepository;

    // 블로그 글 추가 메서드
    public Article save(AddArticleRequest request) {
        return blogRepository.save(request.toEntity());
    }

    // 블로그 글 전체 조회 메서드
    public List<Article> findAll() {
        return blogRepository.findAll();
    }

    public Article findById(long id) {
        return blogRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found: " + id));
    }

    public void delete(long id) {
        blogRepository.deleteById(id);
    }

    @Transactional // 트랜잭션 메서드
    public Article update(long id, UpdateArticleRequest request) {
        Article article = blogRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found: " + id));

        article.update(request.getTitle(), request.getContent());

        return article;
    }
}
