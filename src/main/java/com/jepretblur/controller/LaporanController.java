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
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;

public class LaporanController {

    @FXML private TableView<Transaksi> tableLaporan;
    @FXML private TableColumn<Transaksi, Integer> colId;
    @FXML private TableColumn<Transaksi, String> colPelanggan;
    @FXML private TableColumn<Transaksi, String> colKamera;
    @FXML private TableColumn<Transaksi, String> colSewa;
    @FXML private TableColumn<Transaksi, String> colKembali;
    @FXML private TableColumn<Transaksi, String> colStatus;
    
    @FXML private TextArea txtStruk;

    private TransaksiDAO dao;

    @FXML
    public void initialize() {
        dao = new TransaksiDAO();
        setupTable();
        refreshData();

        // Listener saat baris tabel diklik
        tableLaporan.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                generateStruk(newVal);
            }
        });
    }

    private void setupTable() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colPelanggan.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getPelanggan().getNama()));
        colKamera.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getKamera().getMerk() + " " + cell.getValue().getKamera().getTipe()));
        colSewa.setCellValueFactory(new PropertyValueFactory<>("tglSewa"));
        colKembali.setCellValueFactory(new PropertyValueFactory<>("tglKembali"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
    }

    @FXML
    private void refreshData() {
        tableLaporan.setItems(dao.getAllHistory());
    }

    // Fungsi bikin teks struk manual
    private void generateStruk(Transaksi t) {
        StringBuilder sb = new StringBuilder();
        sb.append("================================\n");
        sb.append("      JEPRET BLUR RENTAL\n");
        sb.append("================================\n");
        sb.append("ID Transaksi : ").append(t.getId()).append("\n");
        sb.append("Tgl Sewa     : ").append(t.getTglSewa()).append("\n");
        sb.append("Tgl Kembali  : ").append(t.getTglKembali()).append("\n");
        sb.append("--------------------------------\n");
        sb.append("PELANGGAN:\n");
        sb.append(t.getPelanggan().getNama()).append("\n");
        sb.append("(").append(t.getPelanggan().getNoHp()).append(")\n");
        sb.append("--------------------------------\n");
        sb.append("UNIT KAMERA:\n");
        sb.append(t.getKamera().getMerk()).append(" ").append(t.getKamera().getTipe()).append("\n");
        sb.append("@ Rp").append((int)t.getKamera().getHargaSewa()).append("/hari\n");
        sb.append("--------------------------------\n");
        sb.append("TOTAL BAYAR  : Rp ").append((int)t.getTotalHarga()).append("\n");
        sb.append("STATUS       : ").append(t.getStatus().toUpperCase()).append("\n");
        sb.append("================================\n");
        sb.append("   Terima Kasih Telah Sewa\n");
        sb.append("================================\n");

        txtStruk.setText(sb.toString());
    }

    @FXML
    private void cetakStruk() {
        if (txtStruk.getText().isEmpty()) {
            showAlert("Pilih data dulu di tabel!");
            return;
        }
        // Di dunia nyata, ini connect ke printer thermal.
        // Di sini kita cuma munculin pop-up info.
        showAlert("Struk berhasil dikirim ke Printer (Simulasi).");
    }

    @FXML
    private void kembaliKeMenu() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/jepretblur/view/MenuUtama.fxml"));
            Stage stage = (Stage) tableLaporan.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) { e.printStackTrace(); }
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(msg);
        alert.show();
    }
}