package me.shinsunyung.commitnote.service;

import lombok.RequiredArgsConstructor;
import me.shinsunyung.commitnote.domain.User;
import me.shinsunyung.commitnote.dto.AddUserRequest;
import me.shinsunyung.commitnote.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public Long save(AddUserRequest dto) {
        return userRepository.save(User.builder()
                .email(dto.getEmail())
                // [1] 패스워드 암호화
                .password(passwordEncoder.encode(dto.getPassword()))
                .build()).getId();
    }
}
