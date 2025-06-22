package Controller;

import Model.Materi;
import Service.MateriService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class PilihLaporanController {

    @FXML
    private ComboBox<Materi> materiComboBox;

    @FXML
    private Button lihatLaporanButton, kembaliButton;

    @FXML
    public void initialize() {
        List<Materi> materiList = MateriService.getAllMateri();
        materiComboBox.setItems(FXCollections.observableArrayList(materiList));

        
        materiComboBox.setConverter(new javafx.util.StringConverter<>() {
            @Override
            public String toString(Materi materi) {
                return materi != null ? materi.getTitle() : "";
            }

            @Override
            public Materi fromString(String string) {
                return null; 
            }
        });
    }

    @FXML
    private void handleLihatLaporan() {
        Materi selectedMateri = materiComboBox.getValue();

        if (selectedMateri == null) {
            showAlert("Silakan pilih materi terlebih dahulu.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/LaporanAktivitas.fxml"));
            Parent root = loader.load();

            LaporanAktivitasController controller = loader.getController();
            controller.loadData(selectedMateri);

            Stage stage = (Stage) lihatLaporanButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Laporan Aktivitas - " + selectedMateri.getTitle());
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Gagal membuka laporan: " + e.getMessage());
        }
    }

    @FXML
    private void handleKembali() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/dashboard.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) kembaliButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Dashboard");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void showAlert(String pesan) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(pesan);
        alert.showAndWait();
    }
}