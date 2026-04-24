package com.elearning.service;

import com.elearning.exception.CourseNotFoundException;
import com.elearning.exception.DuplicateEnrollmentException;
import com.elearning.exception.UnauthorizedActionException;
import com.elearning.exception.UserNotFoundException;
import com.elearning.model.*;

import java.util.*;

public class ELearningService {
    private static ELearningService instance; //singleton , sa fie doar o instanta in memorie

    private final List<User> utilizatori;
    private final Map<String, Category> categorii;
    private final TreeSet<Course> cursuri;
    private final List<Enrollment> inscrieri;
    private final List<QuizResult> rezultateQuizuri;

    private ELearningService() { //initializare la constructor pt tipurile complexe de date
        utilizatori = new ArrayList<>();
        categorii = new HashMap<>();
        cursuri = new TreeSet<>();
        inscrieri = new ArrayList<>();
        rezultateQuizuri = new ArrayList<>();
    }

    public static ELearningService getInstance() { //singleton
        if (instance == null) {
            instance = new ELearningService();
        }
        return instance;
    }

    public void inregistrareUtilizator(User user) {
        utilizatori.add(user);
    }

    public void adaugaCategorie(Category categorie) {
        categorii.put(categorie.getNume(), categorie);
    }

    public Category gasesteCategorie(String nume) {
        return categorii.get(nume);
    }

    public void creareCurs(Course curs) {
        cursuri.add(curs);
        curs.getProfesorTitular().adaugaCurs(curs);
    }

    public void inrolareStudent(Student student, Course curs) throws DuplicateEnrollmentException, CourseNotFoundException {
        if (!cursuri.contains(curs)) {
            throw new CourseNotFoundException("Cursul nu exista in sistem: " + curs.getTitlu());
        }
        for (Enrollment enrollment : inscrieri) {
            if (enrollment.getStudent().equals(student) && enrollment.getCurs().equals(curs)) {
                throw new DuplicateEnrollmentException("Studentul " + student.getNume() + " este deja inscris la " + curs.getTitlu());
            }
        }
        inscrieri.add(new Enrollment(inscrieri.size() + 1, student, curs)); //id-ul este generat pe baza dimensiunii listei; valid doar pentru date in-memory fara stergeri
        student.adaugaCurs(curs);
    }

    public List<Course> cautareCursuriDupaCategorie(String numeCategorie) {
        List<Course> rezultat = new ArrayList<>();
        for (Course curs : cursuri) {
            if (curs.getCategorie().getNume().equalsIgnoreCase(numeCategorie)) {
                rezultat.add(curs);
            }
        }
        return rezultat;
    }

    public TreeSet<Course> sortareCursuriAlfabetic() {
        return cursuri;
    }

    public List<Course> sortareCursuriDupaPret() {
        List<Course> lista = new ArrayList<>(cursuri);
        lista.sort(new CourseByPriceComparator());
        return lista;
    }

    public void adaugareLectie(Course curs, Lesson lectie) throws CourseNotFoundException {
        if (!cursuri.contains(curs)) {
            throw new CourseNotFoundException("Cursul nu exista in sistem: " + curs.getTitlu());
        }
        curs.adaugaLectie(lectie);
    }

    public void creareQuiz(Course curs, Quiz quiz) throws CourseNotFoundException {
        if (!cursuri.contains(curs)) {
            throw new CourseNotFoundException("Cursul nu exista in sistem: " + curs.getTitlu());
        }
        curs.adaugaQuiz(quiz);
    }

    public List<Student> listareStudentiPerCurs(Course curs) {
        List<Student> studenti = new ArrayList<>();
        for (Enrollment enrollment : inscrieri) {
            if (enrollment.getCurs().equals(curs)) {
                studenti.add(enrollment.getStudent());
            }
        }
        return studenti;
    }

    public void actualizareProfil(User user, String numeNou, String emailNou) throws UserNotFoundException {
        if (!utilizatori.contains(user)) {
            throw new UserNotFoundException("Utilizatorul nu exista in sistem.");
        }
        user.setNume(numeNou);
        user.setEmail(emailNou);
    }

    public double raportVenituriProfesor(Teacher teacher) {
        double total = 0;
        for (Course curs : teacher.getCursuriPredate()) {
            for (Enrollment enrollment : inscrieri) { //pentru fiecare inscriere la cursul profesorului, se aduna pretul cursului
                if (enrollment.getCurs().equals(curs)) {
                    total += curs.getPret();
                }
            }
        }
        return total;
    }

    public void inregistrareRezultatQuiz(Student student, Quiz quiz, int punctajObtinut) throws UserNotFoundException, UnauthorizedActionException {
        if (!utilizatori.contains(student)) {
            throw new UserNotFoundException("Studentul nu exista in sistem.");
        }
        boolean esteInrolat = false;
        for (Enrollment enrollment : inscrieri) {
            if (enrollment.getStudent().equals(student) && enrollment.getCurs().equals(quiz.getCurs())) {
                esteInrolat = true;
                break;
            }
        }
        if (!esteInrolat) {
            throw new UnauthorizedActionException("Studentul nu este inscris la cursul acestui quiz.");
        }
        rezultateQuizuri.add(new QuizResult(rezultateQuizuri.size() + 1, student, quiz, punctajObtinut));
    }

    public List<QuizResult> getRezultateQuizuri() {
        return rezultateQuizuri;
    }

    public List<User> getUtilizatori() {
        return utilizatori;
    }

    public List<Enrollment> getInscrieri() {
        return inscrieri;
    }
}
