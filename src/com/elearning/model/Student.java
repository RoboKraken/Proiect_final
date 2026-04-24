package com.elearning.model;

import java.util.ArrayList;
import java.util.List;

public class Student extends User {
    private final List<Course> listaCursuriInscrise;

    public Student(int id, String nume, String email, String parola) {
        super(id, nume, email, parola);
        this.listaCursuriInscrise = new ArrayList<>();
    }

    public List<Course> getListaCursuriInscrise() {
        return listaCursuriInscrise;
    }

    public void adaugaCurs(Course curs) {
        listaCursuriInscrise.add(curs);
    }
}
