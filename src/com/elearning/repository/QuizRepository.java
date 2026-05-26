package com.elearning.repository;

import com.elearning.db.DatabaseConnection;
import com.elearning.model.Category;
import com.elearning.model.Course;
import com.elearning.model.Quiz;
import com.elearning.model.Teacher;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class QuizRepository implements GenericRepository<Quiz, Integer> {

    @Override
    public void save(Quiz quiz) throws SQLException {
        Connection conn = DatabaseConnection.getInstance().getConnection();
        conn.setAutoCommit(false);
        String sql = "INSERT INTO quizzes (id, titlu, punctaj_minim, curs_id) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, quiz.getId());
            pstmt.setString(2, quiz.getTitlu());
            pstmt.setInt(3, quiz.getPunctajMinim());
            pstmt.setInt(4, quiz.getCurs().getId());
            pstmt.executeUpdate();
            conn.commit();
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        }
    }

    @Override
    public Quiz findById(Integer id) throws SQLException {
        Connection conn = DatabaseConnection.getInstance().getConnection();
        String sql = """
                SELECT q.id, q.titlu, q.punctaj_minim,
                       c.id AS curs_id, c.titlu AS curs_titlu, c.descriere AS curs_descriere, c.pret,
                       p.id AS prof_id, p.nume AS prof_nume, p.email AS prof_email,
                       p.parola AS prof_parola, p.specializare,
                       cat.id AS cat_id, cat.nume AS cat_nume, cat.descriere AS cat_descriere
                FROM quizzes q
                JOIN courses c ON q.curs_id = c.id
                JOIN users p ON c.profesor_id = p.id
                JOIN categories cat ON c.categorie_id = cat.id
                WHERE q.id = ?
                """;
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return buildQuiz(rs);
                }
            }
        }
        return null;
    }

    @Override
    public List<Quiz> findAll() throws SQLException {
        Connection conn = DatabaseConnection.getInstance().getConnection();
        String sql = """
                SELECT q.id, q.titlu, q.punctaj_minim,
                       c.id AS curs_id, c.titlu AS curs_titlu, c.descriere AS curs_descriere, c.pret,
                       p.id AS prof_id, p.nume AS prof_nume, p.email AS prof_email,
                       p.parola AS prof_parola, p.specializare,
                       cat.id AS cat_id, cat.nume AS cat_nume, cat.descriere AS cat_descriere
                FROM quizzes q
                JOIN courses c ON q.curs_id = c.id
                JOIN users p ON c.profesor_id = p.id
                JOIN categories cat ON c.categorie_id = cat.id
                """;
        List<Quiz> quizuri = new ArrayList<>();
        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                quizuri.add(buildQuiz(rs));
            }
        }
        return quizuri;
    }

    @Override
    public void update(Quiz quiz) throws SQLException {
        Connection conn = DatabaseConnection.getInstance().getConnection();
        conn.setAutoCommit(false);
        String sql = "UPDATE quizzes SET titlu = ?, punctaj_minim = ? WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, quiz.getTitlu());
            pstmt.setInt(2, quiz.getPunctajMinim());
            pstmt.setInt(3, quiz.getId());
            pstmt.executeUpdate();
            conn.commit();
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        }
    }

    @Override
    public void delete(Integer id) throws SQLException {
        Connection conn = DatabaseConnection.getInstance().getConnection();
        conn.setAutoCommit(false);
        String sql = "DELETE FROM quizzes WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            conn.commit();
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        }
    }

    private Quiz buildQuiz(ResultSet rs) throws SQLException {
        Teacher profesor = new Teacher(
                rs.getInt("prof_id"),
                rs.getString("prof_nume"),
                rs.getString("prof_email"),
                rs.getString("prof_parola"),
                rs.getString("specializare")
        );
        Category categorie = new Category(
                rs.getInt("cat_id"),
                rs.getString("cat_nume"),
                rs.getString("cat_descriere")
        );
        Course curs = new Course(
                rs.getInt("curs_id"),
                rs.getString("curs_titlu"),
                rs.getString("curs_descriere"),
                profesor,
                rs.getDouble("pret"),
                categorie
        );
        return new Quiz(
                rs.getInt("id"),
                rs.getString("titlu"),
                rs.getInt("punctaj_minim"),
                curs
        );
    }
}
