package com.elearning.model;

import java.time.LocalDate;

public class QuizResult {
    private final int id;
    private final Student student;
    private final Quiz quiz;
    private final int punctajObtinut;
    private final LocalDate dataSustinerii;

    public QuizResult(int id, Student student, Quiz quiz, int punctajObtinut) {
        this.id = id;
        this.student = student;
        this.quiz = quiz;
        this.punctajObtinut = punctajObtinut;
        this.dataSustinerii = LocalDate.now();
    }

    public int getId() {
        return id;
    }

    public Student getStudent() {
        return student;
    }

    public Quiz getQuiz() {
        return quiz;
    }

    public int getPunctajObtinut() {
        return punctajObtinut;
    }

    public LocalDate getDataSustinerii() {
        return dataSustinerii;
    }

    @Override
    public String toString() {
        return "QuizResult{student='" + student.getNume() + "', quiz='" + quiz.getTitlu() + "', punctaj=" + punctajObtinut + "}";
    }
}
