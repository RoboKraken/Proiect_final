package com.elearning.model;

import java.util.List;

public class Question {
    private final int id;
    private String enunt;
    private List<String> varianteRaspuns;
    private String raspunsCorect;

    public Question(int id, String enunt, List<String> varianteRaspuns, String raspunsCorect) {
        this.id = id;
        this.enunt = enunt;
        this.varianteRaspuns = varianteRaspuns;
        this.raspunsCorect = raspunsCorect;
    }

    public int getId() {
        return id;
    }

    public String getEnunt() {
        return enunt;
    }

    public List<String> getVarianteRaspuns() {
        return varianteRaspuns;
    }

    public String getRaspunsCorect() {
        return raspunsCorect;
    }

    public void setEnunt(String enunt) {
        this.enunt = enunt;
    }

    public void setVarianteRaspuns(List<String> varianteRaspuns) {
        this.varianteRaspuns = varianteRaspuns;
    }

    public void setRaspunsCorect(String raspunsCorect) {
        this.raspunsCorect = raspunsCorect;
    }
}
