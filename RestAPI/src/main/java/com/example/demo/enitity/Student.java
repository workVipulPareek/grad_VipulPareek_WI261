package com.example.demo.enitity;

import jakarta.persistence.*;

@Entity
@Table(name = "students")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String univRegNo;

    private String name;
    private String standard;
    private String school;
    private String gender;
    private Double percentage;

    public Student() {}

    public Student(String univRegNo, String name, String standard,
                   String school, String gender, Double percentage) {
        this.univRegNo = univRegNo;
        this.name = name;
        this.standard = standard;
        this.school = school;
        this.gender = gender;
        this.percentage = percentage;
    }

    public Long getId() { return id; }

    public String getUnivRegNo() { return univRegNo; }
    public void setUnivRegNo(String univRegNo) { this.univRegNo = univRegNo; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getStandard() { return standard; }
    public void setStandard(String standard) { this.standard = standard; }

    public String getSchool() { return school; }
    public void setSchool(String school) { this.school = school; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public Double getPercentage() { return percentage; }
    public void setPercentage(Double percentage) { this.percentage = percentage; }
}