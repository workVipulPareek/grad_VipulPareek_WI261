package com.example.demo.secondary.repository;

import com.example.demo.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentSecondaryRepo extends JpaRepository<Student, Long> {
}
