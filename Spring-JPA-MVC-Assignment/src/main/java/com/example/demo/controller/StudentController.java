package com.example.demo.controller;

import com.example.demo.entity.Student;
import com.example.demo.services.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class StudentController {

    @Autowired
    private StudentService service;

    @GetMapping("/")
    public String showForm(Model model) {
        model.addAttribute("student", new Student());
        model.addAttribute("students", service.getAllStudents());
        return "student";
    }

    @PostMapping("/save")
    public String saveStudent(@ModelAttribute Student student) {
        service.saveStudent(student);
        return "redirect:/";
    }
}
