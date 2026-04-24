package com.elearning.model;

import java.util.ArrayList;
import java.util.List;

public class Quiz {
    private final int id;
    private String titlu;
    private final List<Question> listaIntrebari;
    private int punctajMinim;
    private final Course curs;

    public Quiz(int id, String titlu, int punctajMinim, Course curs) {
        this.id = id;
        this.titlu = titlu;
        this.punctajMinim = punctajMinim;
        this.curs = curs;
        this.listaIntrebari = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public String getTitlu() {
        return titlu;
    }

    public List<Question> getListaIntrebari() {
        return listaIntrebari;
    }

    public int getPunctajMinim() {
        return punctajMinim;
    }

    public Course getCurs() {
        return curs;
    }

    public void setTitlu(String titlu) {
        this.titlu = titlu;
    }

    public void setPunctajMinim(int punctajMinim) {
        this.punctajMinim = punctajMinim;
    }

    public void adaugaIntrebare(Question intrebare) {
        listaIntrebari.add(intrebare);
    }

    @Override
    public String toString() {
        return "Quiz{'" + titlu + "', punctajMinim=" + punctajMinim + "}";
    }
}
