package com.jepretblur.dao;

import com.jepretblur.model.Kamera;
import com.jepretblur.model.Pelanggan;
import com.jepretblur.model.Transaksi;
import com.jepretblur.util.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class TransaksiDAO {

    // --- BAGIAN 1: FITUR SEWA (Simpan Transaksi Baru) ---

    public boolean saveTransaksi(Transaksi t) {
        String query = "INSERT INTO transactions (customer_id, camera_id, tgl_sewa, tgl_kembali, total_harga, status) VALUES (?, ?, ?, ?, ?, 'sewa')";
        String updateStok = "UPDATE cameras SET stok = stok - 1 WHERE id = ?"; 

        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false); // Mulai Transaksi biar aman

            // 1. Simpan data sewa
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setInt(1, t.getPelanggan().getId());
                stmt.setInt(2, t.getKamera().getId());
                stmt.setDate(3, Date.valueOf(t.getTglSewa()));
                stmt.setDate(4, Date.valueOf(t.getTglKembali()));
                stmt.setDouble(5, t.getTotalHarga());
                stmt.executeUpdate();
            }

            // 2. Kurangi stok kamera
            try (PreparedStatement stmt2 = conn.prepareStatement(updateStok)) {
                stmt2.setInt(1, t.getKamera().getId());
                stmt2.executeUpdate();
            }

            conn.commit(); // Simpan permanen
            return true;

        } catch (SQLException e) {
            try { if (conn != null) conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            e.printStackTrace();
            return false;
        } finally {
            try { if (conn != null) conn.setAutoCommit(true); } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    // --- BAGIAN 2: FITUR PENGEMBALIAN (Lihat Sewa Aktif & Proses Kembali) ---
    // (Ini yang tadi bikin ERROR karena method-nya belum ada)

    public ObservableList<Transaksi> getActiveTransactions() {
        ObservableList<Transaksi> list = FXCollections.observableArrayList();
        // Join 3 tabel biar dapet nama pelanggan & merk kamera
        String query = "SELECT t.*, c.nama, c.no_ktp, k.merk, k.tipe, k.harga_sewa " +
                       "FROM transactions t " +
                       "JOIN customers c ON t.customer_id = c.id " +
                       "JOIN cameras k ON t.camera_id = k.id " +
                       "WHERE t.status = 'sewa'";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Pelanggan p = new Pelanggan();
                p.setId(rs.getInt("customer_id"));
                p.setNama(rs.getString("nama"));
                p.setNoKtp(rs.getString("no_ktp"));

                Kamera k = new Kamera();
                k.setId(rs.getInt("camera_id"));
                k.setMerk(rs.getString("merk"));
                k.setTipe(rs.getString("tipe"));
                k.setHargaSewa(rs.getDouble("harga_sewa"));

                Transaksi t = new Transaksi(
                    rs.getInt("id"),
                    p, k,
                    rs.getDate("tgl_sewa").toLocalDate(),
                    rs.getDate("tgl_kembali").toLocalDate(),
                    rs.getDouble("total_harga"),
                    rs.getString("status")
                );
                list.add(t);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    public boolean returnKamera(int transaksiId, int kameraId) {
        String updateTrans = "UPDATE transactions SET status = 'kembali' WHERE id = ?";
        String updateStok = "UPDATE cameras SET stok = stok + 1 WHERE id = ?"; // Balikin stok

        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false); 

            try (PreparedStatement stmt1 = conn.prepareStatement(updateTrans)) {
                stmt1.setInt(1, transaksiId);
                stmt1.executeUpdate();
            }

            try (PreparedStatement stmt2 = conn.prepareStatement(updateStok)) {
                stmt2.setInt(1, kameraId);
                stmt2.executeUpdate();
            }

            conn.commit();
            return true;
        } catch (SQLException e) {
            try { if (conn != null) conn.rollback(); } catch (SQLException ex) {}
            e.printStackTrace();
            return false;
        } finally {
            try { if (conn != null) conn.setAutoCommit(true); } catch (SQLException e) {}
        }
    }

    public ObservableList<Transaksi> getAllHistory() {
        ObservableList<Transaksi> list = FXCollections.observableArrayList();
        // Query mirip active transaction, tapi TANPA filter WHERE status='sewa'
        // Kita urutkan dari yang terbaru (ORDER BY id DESC)
        String query = "SELECT t.*, c.nama, c.no_ktp, k.merk, k.tipe, k.harga_sewa " +
                       "FROM transactions t " +
                       "JOIN customers c ON t.customer_id = c.id " +
                       "JOIN cameras k ON t.camera_id = k.id " +
                       "ORDER BY t.id DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Pelanggan p = new Pelanggan();
                p.setId(rs.getInt("customer_id"));
                p.setNama(rs.getString("nama"));
                p.setNoKtp(rs.getString("no_ktp"));

                Kamera k = new Kamera();
                k.setId(rs.getInt("camera_id"));
                k.setMerk(rs.getString("merk"));
                k.setTipe(rs.getString("tipe"));
                k.setHargaSewa(rs.getDouble("harga_sewa"));

                Transaksi t = new Transaksi(
                    rs.getInt("id"),
                    p, k,
                    rs.getDate("tgl_sewa").toLocalDate(),
                    rs.getDate("tgl_kembali").toLocalDate(),
                    rs.getDouble("total_harga"),
                    rs.getString("status")
                );
                list.add(t);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }
}