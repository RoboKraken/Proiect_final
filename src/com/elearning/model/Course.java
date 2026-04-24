package com.elearning.model;

import java.util.ArrayList;
import java.util.List;

public class Course implements Comparable<Course> {
    private final int id;
    private final String titlu;
    private String descriere;
    private final Teacher profesorTitular;
    private double pret;
    private final Category categorie;
    private final List<Lesson> lectii;
    private final List<Quiz> quizuri;

    public Course(int id, String titlu, String descriere, Teacher profesorTitular, double pret, Category categorie) {
        this.id = id;
        this.titlu = titlu;
        this.descriere = descriere;
        this.profesorTitular = profesorTitular;
        this.pret = pret;
        this.categorie = categorie;
        this.lectii = new ArrayList<>();
        this.quizuri = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public String getTitlu() {
        return titlu;
    }

    public String getDescriere() {
        return descriere;
    }

    public Teacher getProfesorTitular() {
        return profesorTitular;
    }

    public double getPret() {
        return pret;
    }

    public Category getCategorie() {
        return categorie;
    }

    public List<Lesson> getLectii() {
        return lectii;
    }

    public List<Quiz> getQuizuri() {
        return quizuri;
    }

    public void setDescriere(String descriere) {
        this.descriere = descriere;
    }

    public void setPret(double pret) {
        this.pret = pret;
    }

    public void adaugaLectie(Lesson lectie) {
        lectii.add(lectie);
    }

    public void adaugaQuiz(Quiz quiz) {
        quizuri.add(quiz);
    }

    //cursurile sunt ordonate alfabetic dupa titlu; id-ul este folosit ca tiebreaker pentru titluri identice
    @Override
    public int compareTo(Course other) {
        int cmp = this.titlu.compareTo(other.titlu);
        if (cmp != 0) {
            return cmp;
        }
        return Integer.compare(this.id, other.id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Course other)) return false;
        return this.id == other.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }

    @Override
    public String toString() {
        return "Course{id=" + id + ", titlu='" + titlu + "', pret=" + pret + ", profesor='" + profesorTitular.getNume() + "', categorie=" + categorie.getNume() + "}";
    }
}
