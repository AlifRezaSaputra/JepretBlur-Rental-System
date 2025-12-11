package com.jepretblur.controller;

import com.jepretblur.dao.TransaksiDAO;
import com.jepretblur.model.Transaksi;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class TransaksiKembaliController {

    @FXML private TableView<Transaksi> tableSewa;
    @FXML private TableColumn<Transaksi, Integer> colId;
    @FXML private TableColumn<Transaksi, String> colPelanggan;
    @FXML private TableColumn<Transaksi, String> colKamera;
    @FXML private TableColumn<Transaksi, String> colTglSewa;
    @FXML private TableColumn<Transaksi, String> colTglKembali;
    @FXML private TableColumn<Transaksi, Double> colTotal;
    @FXML private TableColumn<Transaksi, String> colStatus;

    private TransaksiDAO dao;

    @FXML
    public void initialize() {
        dao = new TransaksiDAO();
        setupTable();
        loadData();
    }

    private void setupTable() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        
        // Custom cell value karena Pelanggan & Kamera itu Object
        colPelanggan.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPelanggan().getNama()));
        colKamera.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getKamera().getMerk() + " " + cellData.getValue().getKamera().getTipe()));
        
        colTglSewa.setCellValueFactory(new PropertyValueFactory<>("tglSewa"));
        colTglKembali.setCellValueFactory(new PropertyValueFactory<>("tglKembali"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("totalHarga"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
    }

    private void loadData() {
        tableSewa.setItems(dao.getActiveTransactions());
    }

    @FXML
    private void prosesKembali() {
        Transaksi selected = tableSewa.getSelectionModel().getSelectedItem();
        
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "Pilih Data", "Klik salah satu baris tabel dulu!");
            return;
        }

        // Cek Denda (Keterlambatan)
        long telat = ChronoUnit.DAYS.between(selected.getTglKembali(), LocalDate.now());
        String msg = "Konfirmasi pengembalian untuk: " + selected.getPelanggan().getNama() + "?";
        
        if (telat > 0) {
            double denda = telat * 50000; // Misal denda 50rb per hari
            msg += "\n\n⚠️ PERINGATAN: TERLAMBAT " + telat + " HARI!";
            msg += "\nDenda: Rp " + denda;
            msg += "\nTotal yang harus dibayar: Rp " + (selected.getTotalHarga() + denda);
        }

        // Kalau User Klik OK (Di sini kita anggap langsung OK aja biar simple)
        if (dao.returnKamera(selected.getId(), selected.getKamera().getId())) {
            showAlert(Alert.AlertType.INFORMATION, "Sukses", "Kamera berhasil dikembalikan! Stok bertambah.");
            loadData(); // Refresh tabel
        } else {
            showAlert(Alert.AlertType.ERROR, "Gagal", "Terjadi kesalahan database.");
        }
    }

    @FXML
    private void kembaliKeMenu() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/jepretblur/view/MenuUtama.fxml"));
            Stage stage = (Stage) tableSewa.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) { e.printStackTrace(); }
    }

    private void showAlert(Alert.AlertType type, String title, String msg) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(msg);
        alert.show();
    }
}