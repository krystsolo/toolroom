package com.manageyourtools.toolroom.services;

import org.springframework.web.multipart.MultipartFile;

public interface ToolImageService {

    void uploadImage(MultipartFile file, Long toolId);

    byte[] getImage(Long toolId);
}
