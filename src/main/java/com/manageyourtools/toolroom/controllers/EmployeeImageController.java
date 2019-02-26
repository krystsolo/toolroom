package com.manageyourtools.toolroom.controllers;

import com.manageyourtools.toolroom.api.model.EmployeeDTO;
import com.manageyourtools.toolroom.services.EmployeeImageService;
import com.manageyourtools.toolroom.services.EmployeeService;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sun.nio.ch.IOUtil;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;


@RestController
@RequestMapping(EmployeeController.BASE_URL)
public class EmployeeImageController {

    private final EmployeeImageService imageService;

    public EmployeeImageController(EmployeeImageService imageService, EmployeeService employeeService) {
        this.imageService = imageService;
    }

    @PostMapping("/{employeeId}/image")
    @ResponseStatus(HttpStatus.CREATED)
    public void uploadImage(@RequestParam("file") MultipartFile file, @PathVariable String employeeId){
        this.imageService.uploadImage(file, Long.valueOf(employeeId));
    }

    @GetMapping("/{employeeId}/image")
    @ResponseStatus(HttpStatus.OK)
    public void getImage(@PathVariable String employeeId, HttpServletResponse response) throws IOException {

        response.setContentType("image/jpeg");
        InputStream in = new ByteArrayInputStream(this.imageService.getImage(Long.valueOf(employeeId)));
        IOUtils.copy(in, response.getOutputStream());
    }

}
