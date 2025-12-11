package com.jepretblur.dao;

import com.jepretblur.model.User;
import com.jepretblur.util.DBConnection;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {

    // Method untuk cek login
    public User validateLogin(String username, String inputPassword) {
        String query = "SELECT * FROM users WHERE username = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            if (conn == null) return null; // Safety check

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            // Kalau username ketemu
            if (rs.next()) {
                String dbHash = rs.getString("password");
                
                // Cek password input user vs Hash di Database
                if (BCrypt.checkpw(inputPassword, dbHash)) {
                    // Password cocok! Balikin object User
                    return new User(
                        rs.getInt("id"),
                        rs.getString("username"),
                        dbHash,
                        rs.getString("role")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Login gagal (username gak ada / password salah)
    }

    // Method u/ register
    public boolean registerUser(User user) {
        String query = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            // 1. Hash Password dulu sebelum simpan!
            String hashed = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
            
            stmt.setString(1, user.getUsername());
            stmt.setString(2, hashed); // Simpan yang udah di-hash
            stmt.setString(3, user.getRole());
            
            int result = stmt.executeUpdate();
            return result > 0; // Return true kalau berhasil simpan
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public ObservableList<User> getAllStaff() {
        ObservableList<User> list = FXCollections.observableArrayList();
        String query = "SELECT * FROM users WHERE role IN ('admin', 'manager', 'karyawan')";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                list.add(new User(
                    rs.getInt("id"),
                    rs.getString("username"),
                    rs.getString("password"), // Hash, gak apa-apa ditampilin stringnya
                    rs.getString("role")
                ));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    // Hapus Staff
    public boolean deleteUser(int id) {
        String unlinkKamera = "UPDATE cameras SET owner_id = NULL WHERE owner_id = ?";
        String deleteUser = "DELETE FROM users WHERE id=?";
        
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false); // Pake transaksi biar aman

            // 1. Lepasin dulu kepemilikan kamera (Set owner jadi NULL/Kosong)
            try (PreparedStatement stmt1 = conn.prepareStatement(unlinkKamera)) {
                stmt1.setInt(1, id);
                stmt1.executeUpdate();
            }

            // 2. Baru hapus usernya
            int result;
            try (PreparedStatement stmt2 = conn.prepareStatement(deleteUser)) {
                stmt2.setInt(1, id);
                result = stmt2.executeUpdate();
            }

            conn.commit(); // Simpan perubahan
            return result > 0;

        } catch (SQLException e) {
            try { if (conn != null) conn.rollback(); } catch (SQLException ex) {}
            e.printStackTrace();
            return false;
        } finally {
            try { if (conn != null) conn.setAutoCommit(true); } catch (SQLException ex) {}
        }
    }
}