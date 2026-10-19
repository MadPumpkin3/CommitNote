package me.shinsunyung.commitnote.repository;

import me.shinsunyung.commitnote.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email); // [1] email로 사용자 정보를 가져옴
}
