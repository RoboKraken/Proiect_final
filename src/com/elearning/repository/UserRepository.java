package com.elearning.repository;

import com.elearning.db.DatabaseConnection;
import com.elearning.model.Admin;
import com.elearning.model.Student;
import com.elearning.model.Teacher;
import com.elearning.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserRepository implements GenericRepository<User, Integer> {

    @Override
    public void save(User user) throws SQLException {
        Connection conn = DatabaseConnection.getInstance().getConnection();
        conn.setAutoCommit(false);
        String sql = "INSERT INTO users (id, nume, email, parola, rol, specializare) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, user.getId());
            pstmt.setString(2, user.getNume());
            pstmt.setString(3, user.getEmail());
            pstmt.setString(4, user.getParola());
            pstmt.setString(5, user.getClass().getSimpleName().toUpperCase());
            if (user instanceof Teacher teacher) {
                pstmt.setString(6, teacher.getSpecializare());
            } else {
                pstmt.setNull(6, Types.VARCHAR);
            }
            pstmt.executeUpdate();
            conn.commit();
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        }
    }

    @Override
    public User findById(Integer id) throws SQLException {
        Connection conn = DatabaseConnection.getInstance().getConnection();
        String sql = "SELECT id, nume, email, parola, rol, specializare FROM users WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return buildUser(rs);
                }
            }
        }
        return null;
    }

    @Override
    public List<User> findAll() throws SQLException {
        Connection conn = DatabaseConnection.getInstance().getConnection();
        String sql = "SELECT id, nume, email, parola, rol, specializare FROM users";
        List<User> utilizatori = new ArrayList<>();
        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                utilizatori.add(buildUser(rs));
            }
        }
        return utilizatori;
    }

    @Override
    public void update(User user) throws SQLException {
        Connection conn = DatabaseConnection.getInstance().getConnection();
        conn.setAutoCommit(false);
        String sql = "UPDATE users SET nume = ?, email = ?, parola = ?, specializare = ? WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, user.getNume());
            pstmt.setString(2, user.getEmail());
            pstmt.setString(3, user.getParola());
            if (user instanceof Teacher teacher) {
                pstmt.setString(4, teacher.getSpecializare());
            } else {
                pstmt.setNull(4, Types.VARCHAR);
            }
            pstmt.setInt(5, user.getId());
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
        String sql = "DELETE FROM users WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            conn.commit();
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        }
    }

    private User buildUser(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String nume = rs.getString("nume");
        String email = rs.getString("email");
        String parola = rs.getString("parola");
        String rol = rs.getString("rol");
        String specializare = rs.getString("specializare");
        return switch (rol) {
            case "TEACHER" -> new Teacher(id, nume, email, parola, specializare);
            case "ADMIN"   -> new Admin(id, nume, email, parola);
            default        -> new Student(id, nume, email, parola);
        };
    }
}
