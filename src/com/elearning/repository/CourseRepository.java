package com.elearning.repository;

import com.elearning.db.DatabaseConnection;
import com.elearning.model.Category;
import com.elearning.model.Course;
import com.elearning.model.Teacher;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CourseRepository implements GenericRepository<Course, Integer> {

    @Override
    public void save(Course course) throws SQLException {
        Connection conn = DatabaseConnection.getInstance().getConnection();
        conn.setAutoCommit(false);
        String sql = "INSERT INTO courses (id, titlu, descriere, profesor_id, pret, categorie_id) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, course.getId());
            pstmt.setString(2, course.getTitlu());
            pstmt.setString(3, course.getDescriere());
            pstmt.setInt(4, course.getProfesorTitular().getId());
            pstmt.setDouble(5, course.getPret());
            pstmt.setInt(6, course.getCategorie().getId());
            pstmt.executeUpdate();
            conn.commit();
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        }
    }

    @Override
    public Course findById(Integer id) throws SQLException {
        Connection conn = DatabaseConnection.getInstance().getConnection();
        String sql = """
                SELECT c.id, c.titlu, c.descriere, c.pret,
                       u.id AS prof_id, u.nume AS prof_nume, u.email AS prof_email,
                       u.parola AS prof_parola, u.specializare,
                       cat.id AS cat_id, cat.nume AS cat_nume, cat.descriere AS cat_descriere
                FROM courses c
                JOIN users u ON c.profesor_id = u.id
                JOIN categories cat ON c.categorie_id = cat.id
                WHERE c.id = ?
                """;
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return buildCourse(rs);
                }
            }
        }
        return null;
    }

    @Override
    public List<Course> findAll() throws SQLException {
        Connection conn = DatabaseConnection.getInstance().getConnection();
        String sql = """
                SELECT c.id, c.titlu, c.descriere, c.pret,
                       u.id AS prof_id, u.nume AS prof_nume, u.email AS prof_email,
                       u.parola AS prof_parola, u.specializare,
                       cat.id AS cat_id, cat.nume AS cat_nume, cat.descriere AS cat_descriere
                FROM courses c
                JOIN users u ON c.profesor_id = u.id
                JOIN categories cat ON c.categorie_id = cat.id
                """;
        List<Course> cursuri = new ArrayList<>();
        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                cursuri.add(buildCourse(rs));
            }
        }
        return cursuri;
    }

    @Override
    public void update(Course course) throws SQLException {
        Connection conn = DatabaseConnection.getInstance().getConnection();
        conn.setAutoCommit(false);
        String sql = "UPDATE courses SET titlu = ?, descriere = ?, pret = ? WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, course.getTitlu());
            pstmt.setString(2, course.getDescriere());
            pstmt.setDouble(3, course.getPret());
            pstmt.setInt(4, course.getId());
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
        String sql = "DELETE FROM courses WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            conn.commit();
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        }
    }

    private Course buildCourse(ResultSet rs) throws SQLException {
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
        return new Course(
                rs.getInt("id"),
                rs.getString("titlu"),
                rs.getString("descriere"),
                profesor,
                rs.getDouble("pret"),
                categorie
        );
    }
}
