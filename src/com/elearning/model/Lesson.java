package com.elearning.model;

public class Lesson {
    private final int id;
    private String titlu;
    private String continutText;
    private int durataMinute;

    public Lesson(int id, String titlu, String continutText, int durataMinute) {
        this.id = id;
        this.titlu = titlu;
        this.continutText = continutText;
        this.durataMinute = durataMinute;
    }

    public int getId() {
        return id;
    }

    public String getTitlu() {
        return titlu;
    }

    public String getContinutText() {
        return continutText;
    }

    public int getDurataMinute() {
        return durataMinute;
    }

    public void setTitlu(String titlu) {
        this.titlu = titlu;
    }

    public void setContinutText(String continutText) {
        this.continutText = continutText;
    }

    public void setDurataMinute(int durataMinute) {
        this.durataMinute = durataMinute;
    }

    @Override
    public String toString() {
        return "Lesson{'" + titlu + "', " + durataMinute + " min}";
    }
}
