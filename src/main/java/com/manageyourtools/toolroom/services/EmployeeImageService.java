package com.manageyourtools.toolroom.services;

import org.springframework.web.multipart.MultipartFile;

public interface EmployeeImageService {

    void uploadImage(MultipartFile file, Long employeeId);
    byte[] getImage(Long id);
}
