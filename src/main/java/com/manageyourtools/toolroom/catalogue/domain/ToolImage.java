package com.manageyourtools.toolroom.catalogue.domain;

import com.manageyourtools.toolroom.exception.ResourceNotFoundException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.io.IOException;

@Entity(name = "tool_image")
@NoArgsConstructor
@Builder
class ToolImage {

    ToolImage(Long toolId) {
        this.toolId = toolId;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    private byte[] image;

    private Long toolId;

    void changeImage(MultipartFile file) {
        try {
            this.image = file.getBytes();
        } catch (IOException e) {
            throw new RuntimeException("Image saving ended with error");
        }
    }

    byte[] retrieveImage() {
        if (image == null) {
            throw new ResourceNotFoundException("Image not found for tool" + id);
        }
        return image;
    }

    Long getToolId() {
        return toolId;
    }
}
