package com.manageyourtools.toolroom.services;

import com.manageyourtools.toolroom.domains.Tool;
import com.manageyourtools.toolroom.exception.ResourceNotFoundException;
import com.manageyourtools.toolroom.repositories.ToolRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class ToolImageService {

    private final ToolRepository toolRepository;

    public ToolImageService(ToolRepository toolRepository) {
        this.toolRepository = toolRepository;
    }

    public void uploadImage(MultipartFile file, Long toolId) {
        Tool tool = toolRepository.findById(toolId)
                .orElseThrow(() -> new ResourceNotFoundException("Tool with id=" +  toolId + " not found"));

        tool.setImage(decodeImage(file));
        toolRepository.save(tool);
    }

    private byte[] decodeImage(MultipartFile file) {
        try {
            return file.getBytes();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Image saving ended with error");
        }
    }

    public byte[] getImage(Long toolId) {
        return toolRepository.findById(toolId)
                .filter(t -> t.getImage() != null)
                .map(Tool::getImage)
                .orElseThrow(() -> new ResourceNotFoundException("There is no image in DB for tool with id=" + toolId));
    }
}
