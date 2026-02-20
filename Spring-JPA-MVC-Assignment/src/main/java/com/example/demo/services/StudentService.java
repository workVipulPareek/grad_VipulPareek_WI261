package com.example.demo.services;

import com.example.demo.entity.Student;
import com.example.demo.primary.repository.StudentPrimaryRepo;
import com.example.demo.secondary.repository.StudentSecondaryRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {

    @Autowired
    private StudentPrimaryRepo primaryRepo;

    @Autowired
    private StudentSecondaryRepo secondaryRepo;

    public void saveStudent(Student student) {

        // Save in primary
        Student savedPrimary = primaryRepo.save(student);

        // Create new object for secondary
        Student backupStudent = new Student();
        backupStudent.setRollNo(savedPrimary.getRollNo());
        backupStudent.setName(savedPrimary.getName());
        backupStudent.setStandard(savedPrimary.getStandard());
        backupStudent.setSchool(savedPrimary.getSchool());

        // DO NOT SET ID
        secondaryRepo.save(backupStudent);
    }


    public List<Student> getAllStudents() {
        return primaryRepo.findAll();
    }
}
