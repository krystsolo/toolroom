package com.manageyourtools.toolroom.controllers;

import com.manageyourtools.toolroom.services.ToolImageService;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping(ToolController.BASE_URL)
public class ToolImageController {

    private final ToolImageService imageService;

    public ToolImageController(ToolImageService imageService) {
        this.imageService = imageService;
    }

    @PostMapping("/{toolId}/image")
    @ResponseStatus(HttpStatus.CREATED)
    public void uploadImage(@RequestParam("file") MultipartFile file, @PathVariable String toolId){
        this.imageService.uploadImage(file, Long.valueOf(toolId));
    }

    @GetMapping("/{toolId}/image")
    @ResponseStatus(HttpStatus.OK)
    public void getImage(@PathVariable String toolId, HttpServletResponse response) throws IOException {

        response.setContentType("image/jpeg");
        InputStream in = new ByteArrayInputStream(this.imageService.getImage(Long.valueOf(toolId)));
        IOUtils.copy(in, response.getOutputStream());
    }

}
