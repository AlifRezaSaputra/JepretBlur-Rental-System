package com.jepretblur.dao;

import com.jepretblur.model.Kamera;
import com.jepretblur.util.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.*;

public class KameraDAO {

    public ObservableList<Kamera> getAllKameras() {
        ObservableList<Kamera> list = FXCollections.observableArrayList();
        // Join ke users buat ambil username pemilik
        String query = "SELECT k.*, u.username FROM cameras k LEFT JOIN users u ON k.owner_id = u.id";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                list.add(new Kamera(
                    rs.getInt("id"),
                    rs.getString("merk"),
                    rs.getString("tipe"),
                    rs.getDouble("harga_sewa"),
                    rs.getInt("stok"),
                    rs.getInt("owner_id"),
                    rs.getString("username") // Simpan nama pemilik
                ));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    public boolean addKamera(Kamera k) {
        // Harga Sewa default 0 kalau pemilik yang input
        String query = "INSERT INTO cameras (merk, tipe, harga_sewa, stok, owner_id) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, k.getMerk());
            stmt.setString(2, k.getTipe());
            stmt.setDouble(3, k.getHargaSewa());
            stmt.setInt(4, k.getStok());
            stmt.setInt(5, k.getOwnerId());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public boolean updateKamera(Kamera k) {
        // Admin update harga & stok, owner gak berubah
        String query = "UPDATE cameras SET merk=?, tipe=?, harga_sewa=?, stok=? WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, k.getMerk());
            stmt.setString(2, k.getTipe());
            stmt.setDouble(3, k.getHargaSewa());
            stmt.setInt(4, k.getStok());
            stmt.setInt(5, k.getId());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public boolean deleteKamera(int id) {
        String query = "DELETE FROM cameras WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) { return false; }
    }
}