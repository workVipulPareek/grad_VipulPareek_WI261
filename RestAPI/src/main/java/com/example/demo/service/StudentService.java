package com.example.demo.service;

import org.springframework.stereotype.Service;
import com.example.demo.repository.*;
import com.example.demo.enitity.*;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    private final StudentRepository repository;

    public StudentService(StudentRepository repository) {
        this.repository = repository;
    }

    public Student saveStudent(Student student) {
        return repository.save(student);
    }

    public List<Student> getAllStudents() {
        return repository.findAll();
    }

    public Optional<Student> getByRegNo(String regNo) {
        return repository.findByUnivRegNo(regNo);
    }

    public void deleteStudent(Long id) {
        repository.deleteById(id);
    }
}