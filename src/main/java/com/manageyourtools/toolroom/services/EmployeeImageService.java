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
public class EmployeeImageService {

    private final EmployeeRepository employeeRepository;

    public EmployeeImageService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Transactional
    public void uploadImage(MultipartFile file, Long employeeId) {

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee wih id=" + employeeId + " not found"));

        employee.setImage(decodeImage(file));
        employeeRepository.save(employee);
    }

    private byte[] decodeImage(MultipartFile file) {
        try {
            return file.getBytes();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Image saving ended with error");
        }
    }

    public byte[] getImage(Long employeeId) {
        return employeeRepository.findById(employeeId)
                .filter(employee -> employee.getImage() != null)
                .map(Employee::getImage)
                .orElseThrow(() -> new ResourceNotFoundException("There is no image in DB for employye with id=" + employeeId));
    }
}
