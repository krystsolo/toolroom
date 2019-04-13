package com.manageyourtools.toolroom.services;

import com.manageyourtools.toolroom.domains.Tool;
import com.manageyourtools.toolroom.exception.ResourceNotFoundException;
import com.manageyourtools.toolroom.repositories.ToolRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class ToolImageServiceImpl implements ToolImageService {

    private final ToolRepository toolRepository;

    public ToolImageServiceImpl(ToolRepository toolRepository) {
        this.toolRepository = toolRepository;
    }

    @Override
    public void uploadImage(MultipartFile file, Long toolId) {
        Tool tool = toolRepository.findById(toolId)
                .orElseThrow(() -> new ResourceNotFoundException("Tool with id=" +  toolId + " not found"));

        try {
            tool.setImage(file.getBytes());
            toolRepository.save(tool);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Image saving ended with error");
        }
    }

    @Override
    public byte[] getImage(Long toolId) {
        Tool tool = toolRepository.findById(toolId)
                .orElseThrow(() -> new ResourceNotFoundException("Tool with id=" +  toolId + " not found"));
        if(tool.getImage() == null){
            throw new ResourceNotFoundException("There is no image in DB for this tool");
        }
        return tool.getImage();
    }
}
