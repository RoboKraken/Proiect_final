package com.elearning.model;

import java.util.ArrayList;
import java.util.List;

public class Teacher extends User {
    private String specializare;
    private final List<Course> cursuriPredate;

    public Teacher(int id, String nume, String email, String parola, String specializare) {
        super(id, nume, email, parola);
        this.specializare = specializare;
        this.cursuriPredate = new ArrayList<>();
    }

    public String getSpecializare() {
        return specializare;
    }

    public List<Course> getCursuriPredate() {
        return cursuriPredate;
    }

    public void setSpecializare(String specializare) {
        this.specializare = specializare;
    }

    public void adaugaCurs(Course curs) {
        cursuriPredate.add(curs);
    }
}
