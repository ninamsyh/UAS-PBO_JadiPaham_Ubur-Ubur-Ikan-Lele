package Controller;

import Model.Materi;
import Service.MateriService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.List;

public class MateriController {

    @FXML
    private TableView<Materi> materiTable;
    @FXML
    private TableColumn<Materi, Integer> colId;
    @FXML
    private TableColumn<Materi, String> colTitle;
    @FXML
    private TableColumn<Materi, String> colDescription;
    @FXML
    private TableColumn<Materi, String> colFilePath;

    @FXML
    private TextField titleField;
    @FXML
    private TextField descriptionField;
    @FXML
    private TextField filePathField;
    @FXML
    private Label statusLabel;
    @FXML
    private Button btnBack;

    private ObservableList<Materi> materiList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colId.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getId()).asObject());
        colTitle.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getTitle()));
        colDescription.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getDescription()));
        colFilePath.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getFilePath()));

        materiTable.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                titleField.setText(newVal.getTitle());
                descriptionField.setText(newVal.getDescription());
                filePathField.setText(newVal.getFilePath());
            }
        });

        loadMateri();
    }

    private void loadMateri() {
        List<Materi> data = MateriService.getAllMateri();
        materiList.setAll(data);
        materiTable.setItems(materiList);
    }

    @FXML
    private void handleBrowseFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Pilih File Materi");
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("PDF", "*.pdf"),
            new FileChooser.ExtensionFilter("Semua File", "*.*")
        );
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            filePathField.setText(file.getAbsolutePath());
        }
    }

    @FXML
    private void handleAdd() {
        String title = titleField.getText().trim();
        String desc = descriptionField.getText().trim();
        String path = filePathField.getText().trim();

        if (title.isEmpty()) {
            statusLabel.setText("Judul tidak boleh kosong!");
            return;
        }

        Materi newMateri = new Materi(0, title, desc, path);
        boolean success = MateriService.insertMateri(newMateri);
        statusLabel.setText(success ? "Data ditambahkan." : "Gagal tambah data.");
        if (success) {
            clearFields();
            loadMateri();
        }
    }

    @FXML
    private void handleEdit() {
        Materi selected = materiTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            statusLabel.setText("Pilih data dulu.");
            return;
        }

        selected = new Materi(
            selected.getId(),
            titleField.getText().trim(),
            descriptionField.getText().trim(),
            filePathField.getText().trim()
        );

        boolean success = MateriService.updateMateri(selected);
        statusLabel.setText(success ? "Data diupdate." : "Gagal update.");
        if (success) {
            clearFields();
            loadMateri();
        }
    }

    @FXML
    private void handleDelete() {
        Materi selected = materiTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            statusLabel.setText("Pilih data dulu.");
            return;
        }

        boolean success = MateriService.deleteMateri(selected.getId());
        statusLabel.setText(success ? "Data dihapus." : "Gagal hapus.");
        if (success) {
            clearFields();
            loadMateri();
        }
    }

    private void clearFields() {
        titleField.clear();
        descriptionField.clear();
        filePathField.clear();
        materiTable.getSelectionModel().clearSelection();
    }

    @FXML
    private void handleBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/dashboard.fxml"));
            Parent root = loader.load();

            DashboardController controller = loader.getController();
            controller.setUserRole(Service.Session.currentRole);

            Stage stage = (Stage) btnBack.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Dashboard - " + Service.Session.currentRole);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
