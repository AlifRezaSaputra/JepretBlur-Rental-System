package com.jepretblur.controller;

import com.jepretblur.dao.KameraDAO;
import com.jepretblur.dao.PelangganDAO;
import com.jepretblur.dao.TransaksiDAO;
import com.jepretblur.model.Kamera;
import com.jepretblur.model.Pelanggan;
import com.jepretblur.model.Transaksi;
import com.jepretblur.util.DBConnection;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import com.jepretblur.model.Pelanggan; // Pastikan import Model
import com.jepretblur.model.Kamera;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.temporal.ChronoUnit;

public class TransaksiSewaController {

    @FXML private ComboBox<Pelanggan> comboPelanggan;
    @FXML private ComboBox<Kamera> comboKamera;
    @FXML private DatePicker dateSewa;
    @FXML private DatePicker dateKembali;
    @FXML private TextField txtTotal;

    private TransaksiDAO transaksiDAO;
    private double grandTotal = 0;

    @FXML
    public void initialize() {
        transaksiDAO = new TransaksiDAO();
        PelangganDAO pelangganDAO = new PelangganDAO();
        KameraDAO kameraDAO = new KameraDAO();

        // Isi ComboBox dengan data dari Database
        comboPelanggan.setItems(pelangganDAO.getAllPelanggan());
        comboKamera.setItems(kameraDAO.getAllKameras());
    }

    @FXML
    private void hitungTotal() {
        if (dateSewa.getValue() == null || dateKembali.getValue() == null || comboKamera.getValue() == null) {
            showAlert("Info", "Pilih kamera dan tanggal dulu!");
            return;
        }

        // Hitung selisih hari
        long days = ChronoUnit.DAYS.between(dateSewa.getValue(), dateKembali.getValue());
        
        if (days < 1) days = 1; // Minimal sewa 1 hari

        double hargaPerHari = comboKamera.getValue().getHargaSewa();
        grandTotal = days * hargaPerHari;

        txtTotal.setText("Rp " + grandTotal + " (" + days + " hari)");
    }

    @FXML
    private void simpanTransaksi() {
        hitungTotal(); // Pastikan harga terupdate
        
        if (comboPelanggan.getValue() == null || comboKamera.getValue() == null) {
            showAlert("Error", "Data belum lengkap!");
            return;
        }

        Transaksi t = new Transaksi();
        t.setPelanggan(comboPelanggan.getValue());
        t.setKamera(comboKamera.getValue());
        t.setTglSewa(dateSewa.getValue());
        t.setTglKembali(dateKembali.getValue());
        t.setTotalHarga(grandTotal);

        if (transaksiDAO.saveTransaksi(t)) {
            showAlert("Sukses", "Transaksi Berhasil! Stok Kamera berkurang.");
            kembali();
        } else {
            showAlert("Error", "Gagal menyimpan transaksi.");
        }
    }

    @FXML
    private void kembali() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/jepretblur/view/MenuUtama.fxml"));
            Stage stage = (Stage) txtTotal.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) { e.printStackTrace(); }
    }

    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(msg);
        alert.show();
    }

    public ObservableList<Transaksi> getActiveTransactions() {
        ObservableList<Transaksi> list = FXCollections.observableArrayList();
        String query = "SELECT t.*, c.nama, c.no_ktp, k.merk, k.tipe, k.harga_sewa " +
                       "FROM transactions t " +
                       "JOIN customers c ON t.customer_id = c.id " +
                       "JOIN cameras k ON t.camera_id = k.id " +
                       "WHERE t.status = 'sewa'";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                // Mapping manual karena kita join 3 tabel
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

    // 2. Proses Pengembalian (Update Status & Stok)
    public boolean returnKamera(int transaksiId, int kameraId) {
        String updateTrans = "UPDATE transactions SET status = 'kembali' WHERE id = ?";
        String updateStok = "UPDATE cameras SET stok = stok + 1 WHERE id = ?"; // Stok balik

        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false); // Transaksi Database

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
}