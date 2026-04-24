package com.elearning.model;

public class Category {
    private final int id;
    private String nume;
    private String descriere;

    public Category(int id, String nume, String descriere) {
        this.id = id;
        this.nume = nume;
        this.descriere = descriere;
    }

    public int getId() {
        return id;
    }

    public String getNume() {
        return nume;
    }

    public String getDescriere() {
        return descriere;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public void setDescriere(String descriere) {
        this.descriere = descriere;
    }

    @Override
    public String toString() {
        return "Category{'" + nume + "'}";
    }
}
