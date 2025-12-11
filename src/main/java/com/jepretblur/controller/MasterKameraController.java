package com.jepretblur.controller;

import com.jepretblur.dao.KameraDAO;
import com.jepretblur.model.Kamera;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import com.jepretblur.util.UserSession;

import java.io.IOException;

public class MasterKameraController {

    // UI Komponen
    @FXML private TableView<Kamera> kameraTable;
    @FXML private TableColumn<Kamera, Integer> colId;
    @FXML private TableColumn<Kamera, String> colMerk;
    @FXML private TableColumn<Kamera, String> colTipe;
    @FXML private TableColumn<Kamera, Double> colHarga;
    @FXML private TableColumn<Kamera, Integer> colStok;
    @FXML private TableColumn<Kamera, String> colOwner;

    @FXML private TextField txtMerk;
    @FXML private TextField txtTipe;
    @FXML private TextField txtHarga;
    @FXML private TextField txtStok;

    private KameraDAO kameraDAO;
    private Kamera selectedKamera; // Nyimpen kamera yang lagi diklik di tabel

    @FXML
    public void initialize() {
        kameraDAO = new KameraDAO();
        setupTable();
        loadData();
        
        // Listener: Kalau baris tabel diklik, isi form otomatis
        kameraTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectedKamera = newSelection;
                txtMerk.setText(newSelection.getMerk());
                txtTipe.setText(newSelection.getTipe());
                txtHarga.setText(String.valueOf(newSelection.getHargaSewa()));
                txtStok.setText(String.valueOf(newSelection.getStok()));
            }
        });
    }

    private void setupTable() {
        // Hubungkan kolom tabel dengan atribut di Class Kamera
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colMerk.setCellValueFactory(new PropertyValueFactory<>("merk"));
        colTipe.setCellValueFactory(new PropertyValueFactory<>("tipe"));
        colHarga.setCellValueFactory(new PropertyValueFactory<>("hargaSewa"));
        colStok.setCellValueFactory(new PropertyValueFactory<>("stok"));
        colOwner.setCellValueFactory(new PropertyValueFactory<>("ownerName"));
    }

    private void loadData() {
        ObservableList<Kamera> list = kameraDAO.getAllKameras();
        kameraTable.setItems(list);
    }

    @FXML
    private void tambahKamera() {
        try {
            Kamera k = new Kamera();
            k.setMerk(txtMerk.getText());
            k.setTipe(txtTipe.getText());
            k.setHargaSewa(Double.parseDouble(txtHarga.getText()));
            k.setStok(Integer.parseInt(txtStok.getText()));

            // FIX: Set Owner ID pake ID Admin yang lagi login
            int adminId = UserSession.getSession().getUser().getId();
            String adminName = UserSession.getSession().getUser().getUsername();
            
            k.setOwnerId(adminId);
            k.setOwnerName(adminName);

            if (kameraDAO.addKamera(k)) {
                showAlert("Sukses", "Data kamera berhasil disimpan!");
                loadData();
                clearForm();
            }
        } catch (Exception e) {
            showAlert("Error", "Input tidak valid (pastikan harga/stok angka)!");
        }
    }

    @FXML
    private void updateKamera() {
        if (selectedKamera == null) {
            showAlert("Peringatan", "Pilih data di tabel dulu!");
            return;
        }
        try {
            selectedKamera.setMerk(txtMerk.getText());
            selectedKamera.setTipe(txtTipe.getText());
            selectedKamera.setHargaSewa(Double.parseDouble(txtHarga.getText()));
            selectedKamera.setStok(Integer.parseInt(txtStok.getText()));

            if (kameraDAO.updateKamera(selectedKamera)) {
                showAlert("Sukses", "Data berhasil diupdate!");
                loadData();
                clearForm();
            }
        } catch (Exception e) {
            showAlert("Error", "Gagal update data!");
        }
    }

    @FXML
    private void hapusKamera() {
        if (selectedKamera == null) {
            showAlert("Peringatan", "Pilih data yang mau dihapus!");
            return;
        }
        if (kameraDAO.deleteKamera(selectedKamera.getId())) {
            showAlert("Sukses", "Data berhasil dihapus!");
            loadData();
            clearForm();
        }
    }

    @FXML
    private void clearForm() {
        txtMerk.clear();
        txtTipe.clear();
        txtHarga.clear();
        txtStok.clear();
        selectedKamera = null;
        kameraTable.getSelectionModel().clearSelection();
    }

    @FXML
    private void kembaliKeMenu() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/jepretblur/view/MenuUtama.fxml"));
            Stage stage = (Stage) txtMerk.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.show();
    }
}