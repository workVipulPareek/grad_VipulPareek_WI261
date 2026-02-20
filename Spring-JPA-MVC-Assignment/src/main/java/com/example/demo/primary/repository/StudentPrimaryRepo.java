package com.example.demo.primary.repository;

import com.example.demo.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentPrimaryRepo extends JpaRepository<Student, Long> {
}
