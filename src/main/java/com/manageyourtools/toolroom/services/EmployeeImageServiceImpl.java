package com.manageyourtools.toolroom.services;

import com.manageyourtools.toolroom.domains.Employee;
import com.manageyourtools.toolroom.exception.ResourceNotFoundException;
import com.manageyourtools.toolroom.repositories.EmployeeRepository;
import javassist.bytecode.CodeAttribute;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

@Service
public class EmployeeImageServiceImpl implements EmployeeImageService {

    private final EmployeeRepository employeeRepository;

    public EmployeeImageServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    @Transactional
    public void uploadImage(MultipartFile file, Long employeeId) {

        Employee employee = employeeRepository.findById(employeeId).orElseThrow(ResourceNotFoundException::new);

        try {
            employee.setImage(file.getBytes());
            employeeRepository.save(employee);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Image saving ended with error");
        }
    }

    @Override
    public byte[] getImage(Long id) {
        Employee employee = employeeRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
        return employee.getImage();
    }
}
