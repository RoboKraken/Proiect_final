import com.elearning.exception.CourseNotFoundException;
import com.elearning.exception.DuplicateEnrollmentException;
import com.elearning.exception.UnauthorizedActionException;
import com.elearning.exception.UserNotFoundException;
import com.elearning.model.*;
import com.elearning.service.ELearningService;

import java.util.List;

public class Main {
    static void main(String[] args) {
        ELearningService service = ELearningService.getInstance();

        Category programare = new Category(1, "Programare", "Cursuri de programare si algoritmică");
        Category design = new Category(2, "Design", "Cursuri de UI/UX si design grafic");
        service.adaugaCategorie(programare);
        service.adaugaCategorie(design);

        Teacher profesorIon = new Teacher(1, "Ion Popescu", "ion@unibuc.ro", "parola123", "Informatică");
        Teacher profesorMaria = new Teacher(2, "Maria Ionescu", "maria@unibuc.ro", "parola456", "Design");
        Admin admin = new Admin(3, "Admin Principal", "admin@unibuc.ro", "admin123");
        Student studentAlex = new Student(4, "Alexandru Radu", "alex@student.ro", "student123");
        Student studentElena = new Student(5, "Elena Muresan", "elena@student.ro", "student456");

        service.inregistrareUtilizator(profesorIon);
        service.inregistrareUtilizator(profesorMaria);
        service.inregistrareUtilizator(admin);
        service.inregistrareUtilizator(studentAlex);
        service.inregistrareUtilizator(studentElena);

        System.out.println("=== 1. Inregistrare utilizatori ===");
        service.getUtilizatori().forEach(System.out::println);

        Course cursJava = new Course(1, "Java Avansat", "Programare orientata pe obiecte in Java", profesorIon, 299.99, programare);
        Course cursAlgoritmi = new Course(2, "Algoritmi si Structuri de Date", "Tehnici de baza in algoritmică", profesorIon, 199.99, programare);
        Course cursUI = new Course(3, "Figma pentru Incepatori", "Introducere in design UI/UX cu Figma", profesorMaria, 149.99, design);

        service.creareCurs(cursJava);
        service.creareCurs(cursAlgoritmi);
        service.creareCurs(cursUI);

        System.out.println("\n=== 2. Cursuri create (sortate alfabetic - TreeSet) ===");
        service.sortareCursuriAlfabetic().forEach(System.out::println);

        try {
            service.inrolareStudent(studentAlex, cursJava);
            service.inrolareStudent(studentAlex, cursAlgoritmi);
            service.inrolareStudent(studentElena, cursJava);
            service.inrolareStudent(studentElena, cursUI);
            System.out.println("\n=== 3. Inrolare studenti ===");
            service.getInscrieri().forEach(System.out::println);
        } catch (DuplicateEnrollmentException | CourseNotFoundException e) {
            System.out.println("Eroare inrolare: " + e.getMessage());
        }

        try {
            service.inrolareStudent(studentAlex, cursJava);
        } catch (DuplicateEnrollmentException e) {
            System.out.println("\n=== 3b. Duplicate enrollment blocat ===");
            System.out.println(e.getMessage());
        } catch (CourseNotFoundException e) {
            System.out.println("Eroare: " + e.getMessage());
        }

        System.out.println("\n=== 4. Cautare cursuri dupa categorie 'Programare' ===");
        List<Course> cursuriProgramare = service.cautareCursuriDupaCategorie("Programare");
        cursuriProgramare.forEach(System.out::println);

        System.out.println("\n=== 5. Sortare cursuri dupa pret ===");
        service.sortareCursuriDupaPret().forEach(System.out::println);

        try {
            service.adaugareLectie(cursJava, new Lesson(1, "Introducere in OOP", "Concepte fundamentale ale POO", 45));
            service.adaugareLectie(cursJava, new Lesson(2, "Mostenire si Polimorfism", "Ierarhii de clase in Java", 60));
            System.out.println("\n=== 6. Lectii adaugate la cursul Java ===");
            cursJava.getLectii().forEach(System.out::println);
        } catch (CourseNotFoundException e) {
            System.out.println("Eroare: " + e.getMessage());
        }

        try {
            Quiz quizJava = new Quiz(1, "Test Final Java", 60, cursJava);
            quizJava.adaugaIntrebare(new Question(1, "Ce este mostenirea?",
                    List.of("Un concept OOP", "Un tip de date", "O exceptie", "Un framework"),
                    "Un concept OOP"));
            quizJava.adaugaIntrebare(new Question(2, "Ce face cuvantul cheie 'super'?",
                    List.of("Apeleaza constructorul parintelui", "Creaza un obiect nou", "Sterge un obiect", "None"),
                    "Apeleaza constructorul parintelui"));
            service.creareQuiz(cursJava, quizJava);
            System.out.println("\n=== 7. Quiz creat pentru cursul Java ===");
            System.out.println(quizJava + " cu " + quizJava.getListaIntrebari().size() + " intrebari");
        } catch (CourseNotFoundException e) {
            System.out.println("Eroare: " + e.getMessage());
        }

        System.out.println("\n=== 8. Studenti inscrisi la cursul Java ===");
        service.listareStudentiPerCurs(cursJava).forEach(System.out::println);

        try {
            service.actualizareProfil(studentAlex, "Alexandru Radu-Ionescu", "alex.ionescu@student.ro");
            System.out.println("\n=== 9. Profil actualizat ===");
            System.out.println(studentAlex);
        } catch (UserNotFoundException e) {
            System.out.println("Eroare: " + e.getMessage());
        }

        System.out.println("\n=== 10. Raport venituri profesor Ion Popescu ===");
        double venituri = service.raportVenituriProfesor(profesorIon);
        System.out.println("Venituri totale: " + venituri + " RON");

        try {
            service.inregistrareRezultatQuiz(studentAlex, cursJava.getQuizuri().get(0), 85);
            service.inregistrareRezultatQuiz(studentElena, cursJava.getQuizuri().get(0), 72);
            System.out.println("\n=== 11. Rezultate quiz inregistrate ===");
            service.getRezultateQuizuri().forEach(System.out::println);
        } catch (UserNotFoundException | UnauthorizedActionException e) {
            System.out.println("Eroare: " + e.getMessage());
        }
    }
}
