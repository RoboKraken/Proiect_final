package com.elearning.model;

public abstract class User {
    private final int id;
    private String nume;
    private String email;
    private String parola;

    public User(int id, String nume, String email, String parola) {
        this.id = id;
        this.nume = nume;
        this.email = email;
        this.parola = parola;
    }

    public int getId() {
        return id;
    }

    public String getNume() {
        return nume;
    }

    public String getEmail() {
        return email;
    }

    public String getParola() {
        return parola;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setParola(String parola) {
        this.parola = parola;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{id=" + id + ", nume='" + nume + "', email='" + email + "'}";
    }
}
