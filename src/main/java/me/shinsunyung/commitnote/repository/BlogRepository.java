package me.shinsunyung.commitnote.repository;

import me.shinsunyung.commitnote.domain.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlogRepository extends JpaRepository<Article, Long> {
}
