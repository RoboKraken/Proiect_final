package com.elearning.repository;

import com.elearning.db.DatabaseConnection;
import com.elearning.model.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EnrollmentRepository implements GenericRepository<Enrollment, Integer> {

    @Override
    public void save(Enrollment enrollment) throws SQLException {
        Connection conn = DatabaseConnection.getInstance().getConnection();
        conn.setAutoCommit(false);
        String sql = "INSERT INTO enrollments (id, student_id, curs_id, data_inscriere) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, enrollment.getId());
            pstmt.setInt(2, enrollment.getStudent().getId());
            pstmt.setInt(3, enrollment.getCurs().getId());
            pstmt.setDate(4, Date.valueOf(enrollment.getDataInscriere()));
            pstmt.executeUpdate();
            conn.commit();
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        }
    }

    @Override
    public Enrollment findById(Integer id) throws SQLException {
        Connection conn = DatabaseConnection.getInstance().getConnection();
        String sql = """
                SELECT e.id, e.data_inscriere,
                       s.id AS student_id, s.nume AS student_nume, s.email AS student_email, s.parola AS student_parola,
                       c.id AS curs_id, c.titlu, c.descriere, c.pret,
                       p.id AS prof_id, p.nume AS prof_nume, p.email AS prof_email,
                       p.parola AS prof_parola, p.specializare,
                       cat.id AS cat_id, cat.nume AS cat_nume, cat.descriere AS cat_descriere
                FROM enrollments e
                JOIN users s ON e.student_id = s.id
                JOIN courses c ON e.curs_id = c.id
                JOIN users p ON c.profesor_id = p.id
                JOIN categories cat ON c.categorie_id = cat.id
                WHERE e.id = ?
                """;
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return buildEnrollment(rs);
                }
            }
        }
        return null;
    }

    @Override
    public List<Enrollment> findAll() throws SQLException {
        Connection conn = DatabaseConnection.getInstance().getConnection();
        String sql = """
                SELECT e.id, e.data_inscriere,
                       s.id AS student_id, s.nume AS student_nume, s.email AS student_email, s.parola AS student_parola,
                       c.id AS curs_id, c.titlu, c.descriere, c.pret,
                       p.id AS prof_id, p.nume AS prof_nume, p.email AS prof_email,
                       p.parola AS prof_parola, p.specializare,
                       cat.id AS cat_id, cat.nume AS cat_nume, cat.descriere AS cat_descriere
                FROM enrollments e
                JOIN users s ON e.student_id = s.id
                JOIN courses c ON e.curs_id = c.id
                JOIN users p ON c.profesor_id = p.id
                JOIN categories cat ON c.categorie_id = cat.id
                """;
        List<Enrollment> inscrieri = new ArrayList<>();
        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                inscrieri.add(buildEnrollment(rs));
            }
        }
        return inscrieri;
    }

    @Override
    public void update(Enrollment enrollment) throws SQLException {
        Connection conn = DatabaseConnection.getInstance().getConnection();
        conn.setAutoCommit(false);
        String sql = "UPDATE enrollments SET data_inscriere = ? WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDate(1, Date.valueOf(enrollment.getDataInscriere()));
            pstmt.setInt(2, enrollment.getId());
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
        String sql = "DELETE FROM enrollments WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            conn.commit();
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        }
    }

    private Enrollment buildEnrollment(ResultSet rs) throws SQLException {
        Student student = new Student(
                rs.getInt("student_id"),
                rs.getString("student_nume"),
                rs.getString("student_email"),
                rs.getString("student_parola")
        );
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
                rs.getString("titlu"),
                rs.getString("descriere"),
                profesor,
                rs.getDouble("pret"),
                categorie
        );
        return new Enrollment(rs.getInt("id"), student, curs);
    }
}
