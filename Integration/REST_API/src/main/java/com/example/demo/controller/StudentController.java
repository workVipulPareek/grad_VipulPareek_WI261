package com.example.demo.controller;

import org.springframework.web.bind.annotation.*;
import com.example.demo.service.*;
import com.example.demo.enitity.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService service;

    public StudentController(StudentService service) {
        this.service = service;
    }

    // CREATE STUDENT
    @PostMapping
    public Student addStudent(@RequestBody Student student) {
        return service.saveStudent(student);
    }

    // GET ALL STUDENTS
    @GetMapping
    public List<Student> getAllStudents() {
        return service.getAllStudents();
    }

    // GET BY REGISTRATION NUMBER
    @GetMapping("/{regNo}")
    public Optional<Student> getByRegNo(@PathVariable String regNo) {
        return service.getByRegNo(regNo);
    }

    // DELETE STUDENT
    @DeleteMapping("/{id}")
    public String deleteStudent(@PathVariable Long id) {
        service.deleteStudent(id);
        return "Student Deleted Successfully";
    }
}