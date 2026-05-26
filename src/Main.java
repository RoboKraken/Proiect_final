import com.elearning.exception.CourseNotFoundException;
import com.elearning.exception.DuplicateEnrollmentException;
import com.elearning.exception.UnauthorizedActionException;
import com.elearning.exception.UserNotFoundException;
import com.elearning.model.*;
import com.elearning.repository.CategoryRepository;
import com.elearning.repository.CourseRepository;
import com.elearning.repository.EnrollmentRepository;
import com.elearning.repository.QuizRepository;
import com.elearning.repository.UserRepository;
import com.elearning.service.ELearningService;

import java.sql.SQLException;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        ELearningService service = ELearningService.getInstance();

        Category programare = new Category(1, "Programare", "Cursuri de programare si algoritmica");
        Category design = new Category(2, "Design", "Cursuri de UI/UX si design grafic");
        service.adaugaCategorie(programare);
        service.adaugaCategorie(design);

        Teacher profesorIon = new Teacher(1, "Ion Popescu", "ion@unibuc.ro", "parola123", "Informatica");
        Teacher profesorMaria = new Teacher(2, "Maria Ionescu", "maria@unibuc.ro", "parola456", "Design");
        Admin admin = new Admin(3, "Admin Principal", "admin@unibuc.ro", "admin123");
        Student studentAlex = new Student(4, "Alexandru Radu", "alex@student.ro", "student123");
        Student studentElena = new Student(5, "Elena Muresan", "elena@student.ro", "student456");

        service.inregistrareUtilizator(profesorIon);
        service.inregistrareUtilizator(profesorMaria);
        service.inregistrareUtilizator(admin);
        service.inregistrareUtilizator(studentAlex);
        service.inregistrareUtilizator(studentElena);

        System.out.println("1. Inregistrare utilizatori");
        service.getUtilizatori().forEach(System.out::println);

        Course cursJava = new Course(1, "Java Avansat", "Programare orientata pe obiecte in Java", profesorIon, 299.99, programare);
        Course cursAlgoritmi = new Course(2, "Algoritmi si Structuri de Date", "Tehnici de baza in algoritmica", profesorIon, 199.99, programare);
        Course cursUI = new Course(3, "Figma pentru Incepatori", "Introducere in design UI/UX cu Figma", profesorMaria, 149.99, design);

        service.creareCurs(cursJava);
        service.creareCurs(cursAlgoritmi);
        service.creareCurs(cursUI);

        System.out.println("\n2. Cursuri create (sortate alfabetic - TreeSet)");
        service.sortareCursuriAlfabetic().forEach(System.out::println);

        try {
            service.inrolareStudent(studentAlex, cursJava);
            service.inrolareStudent(studentAlex, cursAlgoritmi);
            service.inrolareStudent(studentElena, cursJava);
            service.inrolareStudent(studentElena, cursUI);
            System.out.println("\n3. Inrolare studenti");
            service.getInscrieri().forEach(System.out::println);
        } catch (DuplicateEnrollmentException | CourseNotFoundException e) {
            System.out.println("Eroare inrolare: " + e.getMessage());
        }

        try {
            service.inrolareStudent(studentAlex, cursJava);
        } catch (DuplicateEnrollmentException e) {
            System.out.println("\n3b. Duplicate enrollment blocat");
            System.out.println(e.getMessage());
        } catch (CourseNotFoundException e) {
            System.out.println("Eroare: " + e.getMessage());
        }

        System.out.println("\n4. Cautare cursuri dupa categorie 'Programare'");
        List<Course> cursuriProgramare = service.cautareCursuriDupaCategorie("Programare");
        cursuriProgramare.forEach(System.out::println);

        System.out.println("\n5. Sortare cursuri dupa pret");
        service.sortareCursuriDupaPret().forEach(System.out::println);

        try {
            service.adaugareLectie(cursJava, new Lesson(1, "Introducere in OOP", "Concepte fundamentale ale POO", 45));
            service.adaugareLectie(cursJava, new Lesson(2, "Mostenire si Polimorfism", "Ierarhii de clase in Java", 60));
            System.out.println("\n6. Lectii adaugate la cursul Java");
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
            System.out.println("\n7. Quiz creat pentru cursul Java");
            System.out.println(quizJava + " cu " + quizJava.getListaIntrebari().size() + " intrebari");
        } catch (CourseNotFoundException e) {
            System.out.println("Eroare: " + e.getMessage());
        }

        System.out.println("\n8. Studenti inscrisi la cursul Java");
        service.listareStudentiPerCurs(cursJava).forEach(System.out::println);

        try {
            service.actualizareProfil(studentAlex, "Alexandru Radu-Ionescu", "alex.ionescu@student.ro");
            System.out.println("\n9. Profil actualizat");
            System.out.println(studentAlex);
        } catch (UserNotFoundException e) {
            System.out.println("Eroare: " + e.getMessage());
        }

        System.out.println("\n10. Raport venituri profesor Ion Popescu");
        double venituri = service.raportVenituriProfesor(profesorIon);
        System.out.println("Venituri totale: " + venituri + " RON");

        try {
            service.inregistrareRezultatQuiz(studentAlex, cursJava.getQuizuri().get(0), 85);
            service.inregistrareRezultatQuiz(studentElena, cursJava.getQuizuri().get(0), 72);
            System.out.println("\n11. Rezultate quiz inregistrate");
            service.getRezultateQuizuri().forEach(System.out::println);
        } catch (UserNotFoundException | UnauthorizedActionException e) {
            System.out.println("Eroare: " + e.getMessage());
        }

        System.out.println("\n--- JDBC ---");

        UserRepository userRepo = new UserRepository();
        CategoryRepository categoryRepo = new CategoryRepository();
        CourseRepository courseRepo = new CourseRepository();
        EnrollmentRepository enrollmentRepo = new EnrollmentRepository();
        QuizRepository quizRepo = new QuizRepository();

        try {
            System.out.println("\n12. Salvare utilizatori in baza de date");
            userRepo.save(profesorIon);
            userRepo.save(profesorMaria);
            userRepo.save(admin);
            userRepo.save(studentAlex);
            userRepo.save(studentElena);
            System.out.println("Utilizatori salvati cu succes.");

            System.out.println("\n13. Citire toti utilizatorii din baza de date");
            List<User> utilizatoriDB = userRepo.findAll();
            utilizatoriDB.forEach(System.out::println);

            System.out.println("\n14. Cautare utilizator cu id=4 din baza de date");
            User userGasit = userRepo.findById(4);
            System.out.println(userGasit);

            System.out.println("\n15. Actualizare utilizator cu id=4");
            studentAlex.setNume("Alexandru Radu-Ionescu");
            studentAlex.setEmail("alex.nou@student.ro");
            userRepo.update(studentAlex);
            System.out.println("Dupa actualizare: " + userRepo.findById(4));

            System.out.println("\n16. Salvare categorii in baza de date");
            categoryRepo.save(programare);
            categoryRepo.save(design);
            System.out.println("Categorii salvate cu succes.");

            System.out.println("\n17. Salvare cursuri in baza de date");
            courseRepo.save(cursJava);
            courseRepo.save(cursAlgoritmi);
            courseRepo.save(cursUI);
            System.out.println("Cursuri salvate cu succes.");

            System.out.println("\n18. Citire toate cursurile din baza de date (JOIN cu profesor si categorie)");
            List<Course> cursuriDB = courseRepo.findAll();
            cursuriDB.forEach(System.out::println);

            System.out.println("\n18. Salvare inscrieri in baza de date");
            List<Enrollment> inscrieri = service.getInscrieri();
            for (Enrollment enrollment : inscrieri) {
                enrollmentRepo.save(enrollment);
            }
            System.out.println("Inscrieri salvate cu succes.");

            System.out.println("\n19. Citire toate inscrierile din baza de date");
            List<Enrollment> inscrieriDB = enrollmentRepo.findAll();
            inscrieriDB.forEach(System.out::println);

            System.out.println("\n20. Salvare quiz in baza de date");
            Quiz quizJavaDB = cursJava.getQuizuri().get(0);
            quizRepo.save(quizJavaDB);
            System.out.println("Quiz salvat cu succes.");

            System.out.println("\n21. Citire toate quizurile din baza de date");
            List<Quiz> quizuriDB = quizRepo.findAll();
            quizuriDB.forEach(System.out::println);

            System.out.println("\n22. Stergere utilizator cu id=3 (admin) din baza de date");
            userRepo.delete(3);
            System.out.println("Utilizatori dupa stergere admin:");
            userRepo.findAll().forEach(System.out::println);

        } catch (SQLException e) {
            System.out.println("Eroare baza de date: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
