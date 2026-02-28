package com.example.demo.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.example.demo.repository.StudentRepository;
import com.example.demo.entity.Student;

@Service
public class StudentService {

    private final StudentRepository repository;

    public StudentService(StudentRepository repository) {
        this.repository = repository;
    }

    public Student save(Student student) {
        return repository.save(student);
    }

    public List<Student> getAll() {
        return repository.findAll();
    }

    public Student update(Long id, Student updated) {
        Student s = repository.findById(id).orElseThrow();
        s.setName(updated.getName());
        s.setStandard(updated.getStandard());
        s.setSchool(updated.getSchool());
        return repository.save(s);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}