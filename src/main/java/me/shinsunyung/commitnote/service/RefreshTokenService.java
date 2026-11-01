package me.shinsunyung.commitnote.service;

import lombok.RequiredArgsConstructor;
import me.shinsunyung.commitnote.domain.RefreshToken;
import me.shinsunyung.commitnote.repository.RefreshTokenRepository;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshToken findByRefreshToken(String refreshToken) {
        return refreshTokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new IllegalArgumentException("Unexpected token"));
    }
}
