package com.jepretblur.dao;

import com.jepretblur.model.Pelanggan;
import com.jepretblur.util.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PelangganDAO {

    public ObservableList<Pelanggan> getAllPelanggan() {
        ObservableList<Pelanggan> list = FXCollections.observableArrayList();
        String query = "SELECT * FROM customers";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                list.add(new Pelanggan(
                    rs.getInt("id"),
                    rs.getString("no_ktp"),
                    rs.getString("nama"),
                    rs.getString("alamat"),
                    rs.getString("no_hp")
                ));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    public boolean addPelanggan(Pelanggan p) {
        String query = "INSERT INTO customers (no_ktp, nama, alamat, no_hp) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, p.getNoKtp());
            stmt.setString(2, p.getNama());
            stmt.setString(3, p.getAlamat());
            stmt.setString(4, p.getNoHp());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) { return false; }
    }

    public boolean updatePelanggan(Pelanggan p) {
        String query = "UPDATE customers SET no_ktp=?, nama=?, alamat=?, no_hp=? WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, p.getNoKtp());
            stmt.setString(2, p.getNama());
            stmt.setString(3, p.getAlamat());
            stmt.setString(4, p.getNoHp());
            stmt.setInt(5, p.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) { return false; }
    }

    public boolean deletePelanggan(int id) {
        String query = "DELETE FROM customers WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) { return false; }
    }
}