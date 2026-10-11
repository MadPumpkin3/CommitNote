package me.shinsunyung.commitnote.service;

import me.shinsunyung.commitnote.dto.UploadResponse;

public interface FileStorageService {
    UploadResponse store(byte[] bytes, String filename);
}
