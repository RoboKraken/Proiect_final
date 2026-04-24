package com.elearning.model;

import java.time.LocalDate;

public class Enrollment {
    private final int id;
    private final Student student;
    private final Course curs;
    private final LocalDate dataInscriere;

    public Enrollment(int id, Student student, Course curs) {
        this.id = id;
        this.student = student;
        this.curs = curs;
        this.dataInscriere = LocalDate.now(); //data inscrierii este setata automat la momentul crearii obiectului
    }

    public int getId() {
        return id;
    }

    public Student getStudent() {
        return student;
    }

    public Course getCurs() {
        return curs;
    }

    public LocalDate getDataInscriere() {
        return dataInscriere;
    }

    @Override
    public String toString() {
        return "Enrollment{student='" + student.getNume() + "', curs='" + curs.getTitlu() + "', data=" + dataInscriere + "}";
    }
}
