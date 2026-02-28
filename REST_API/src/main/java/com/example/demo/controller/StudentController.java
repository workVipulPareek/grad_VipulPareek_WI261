package com.example.demo.controller;

import org.springframework.web.bind.annotation.*;
import com.example.demo.service.*;
import com.example.demo.enitity.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService service;

    public StudentController(StudentService service) {
        this.service = service;
    }

    @PostMapping
    public Student createStudent(@RequestBody Student student) {
        return service.saveStudent(student);
    }

    @GetMapping
    public List<Student> getAllStudents() {
        return service.getAllStudents();
    }

    @GetMapping("/{regNo}")
    public Optional<Student> getByRegNo(@PathVariable String regNo) {
        return service.getByRegNo(regNo);
    }

    @DeleteMapping("/{id}")
    public String deleteStudent(@PathVariable Long id) {
        service.deleteStudent(id);
        return "Student Deleted Successfully";
    }
}