package com.jepretblur.controller;

import com.jepretblur.dao.PelangganDAO;
import com.jepretblur.model.Pelanggan;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;

public class MasterPelangganController {

    @FXML private TableView<Pelanggan> pelangganTable;
    @FXML private TableColumn<Pelanggan, Integer> colId;
    @FXML private TableColumn<Pelanggan, String> colKtp;
    @FXML private TableColumn<Pelanggan, String> colNama;
    @FXML private TableColumn<Pelanggan, String> colAlamat;
    @FXML private TableColumn<Pelanggan, String> colHp;

    @FXML private TextField txtKtp;
    @FXML private TextField txtNama;
    @FXML private TextField txtHp;
    @FXML private TextArea txtAlamat;

    private PelangganDAO dao;
    private Pelanggan selected;

    @FXML
    public void initialize() {
        dao = new PelangganDAO();
        
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colKtp.setCellValueFactory(new PropertyValueFactory<>("noKtp"));
        colNama.setCellValueFactory(new PropertyValueFactory<>("nama"));
        colAlamat.setCellValueFactory(new PropertyValueFactory<>("alamat"));
        colHp.setCellValueFactory(new PropertyValueFactory<>("noHp"));

        loadData();

        pelangganTable.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                selected = newVal;
                txtKtp.setText(newVal.getNoKtp());
                txtNama.setText(newVal.getNama());
                txtHp.setText(newVal.getNoHp());
                txtAlamat.setText(newVal.getAlamat());
            }
        });
    }

    private void loadData() {
        pelangganTable.setItems(dao.getAllPelanggan());
    }

    @FXML
    private void simpan() {
        Pelanggan p = new Pelanggan(0, txtKtp.getText(), txtNama.getText(), txtAlamat.getText(), txtHp.getText());
        if (dao.addPelanggan(p)) {
            showAlert("Sukses", "Pelanggan berhasil disimpan");
            loadData();
            clear();
        } else {
            showAlert("Error", "Gagal simpan (Cek No KTP mungkin duplikat)");
        }
    }

    @FXML
    private void update() {
        if (selected == null) return;
        selected.setNoKtp(txtKtp.getText());
        selected.setNama(txtNama.getText());
        selected.setAlamat(txtAlamat.getText());
        selected.setNoHp(txtHp.getText());
        
        if (dao.updatePelanggan(selected)) {
            showAlert("Sukses", "Data diupdate");
            loadData();
            clear();
        }
    }

    @FXML
    private void hapus() {
        if (selected != null && dao.deletePelanggan(selected.getId())) {
            showAlert("Sukses", "Data dihapus");
            loadData();
            clear();
        }
    }

    @FXML
    private void clear() {
        txtKtp.clear(); txtNama.clear(); txtHp.clear(); txtAlamat.clear();
        selected = null;
    }

    @FXML
    private void kembali() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/jepretblur/view/MenuUtama.fxml"));
            Stage stage = (Stage) txtKtp.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) { e.printStackTrace(); }
    }

    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(msg);
        alert.show();
    }
}