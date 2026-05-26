package com.elearning.repository;

import com.elearning.db.DatabaseConnection;
import com.elearning.model.Category;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryRepository implements GenericRepository<Category, Integer> {

    @Override
    public void save(Category category) throws SQLException {
        Connection conn = DatabaseConnection.getInstance().getConnection();
        conn.setAutoCommit(false);
        String sql = "INSERT INTO categories (id, nume, descriere) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, category.getId());
            pstmt.setString(2, category.getNume());
            pstmt.setString(3, category.getDescriere());
            pstmt.executeUpdate();
            conn.commit();
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        }
    }

    @Override
    public Category findById(Integer id) throws SQLException {
        Connection conn = DatabaseConnection.getInstance().getConnection();
        String sql = "SELECT id, nume, descriere FROM categories WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Category(rs.getInt("id"), rs.getString("nume"), rs.getString("descriere"));
                }
            }
        }
        return null;
    }

    @Override
    public List<Category> findAll() throws SQLException {
        Connection conn = DatabaseConnection.getInstance().getConnection();
        String sql = "SELECT id, nume, descriere FROM categories";
        List<Category> categorii = new ArrayList<>();
        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                categorii.add(new Category(rs.getInt("id"), rs.getString("nume"), rs.getString("descriere")));
            }
        }
        return categorii;
    }

    @Override
    public void update(Category category) throws SQLException {
        Connection conn = DatabaseConnection.getInstance().getConnection();
        conn.setAutoCommit(false);
        String sql = "UPDATE categories SET nume = ?, descriere = ? WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, category.getNume());
            pstmt.setString(2, category.getDescriere());
            pstmt.setInt(3, category.getId());
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
        String sql = "DELETE FROM categories WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            conn.commit();
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        }
    }
}
