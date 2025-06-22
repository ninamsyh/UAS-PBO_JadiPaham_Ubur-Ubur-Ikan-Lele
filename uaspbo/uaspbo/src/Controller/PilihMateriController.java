package Controller;

import Model.Materi;
import Service.MateriService;
import Service.NilaiService;
import Service.Session;
import Util.SceneUtil;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class PilihMateriController {

    @FXML
    private VBox daftarMateriVBox;

    @FXML
    private Button kembaliButton;

    @FXML
    public void initialize() {
        List<Materi> list = MateriService.getAllMateri();

        if (list.isEmpty()) {
            Label kosong = new Label("📂 Tidak ada materi tersedia.");
            kosong.setStyle("-fx-font-style: italic;");
            daftarMateriVBox.getChildren().add(kosong);
            return;
        }

        for (Materi materi : list) {
            Button btn = new Button();
            btn.setPrefWidth(400);
            btn.setStyle("-fx-font-size: 14px;");
            
            boolean sudahDikerjakan = NilaiService.sudahSelesaiKerjakanMateri(Session.userId, materi.getId());

            btn.setText(materi.getTitle() + (sudahDikerjakan ? " ✅" : ""));
            btn.setDisable(sudahDikerjakan);  

            btn.setOnAction(e -> {
                System.out.println("📥 Materi dipilih: " + materi.getTitle());
                bukaKuis(materi);
            });

            daftarMateriVBox.getChildren().add(btn);
        }
    }

   @FXML
private void kembaliKeDashboard() {
    try {
        String dashboardFxml = Session.currentRole.equalsIgnoreCase("admin")
                ? "/View/dashboard.fxml"
                : "/View/dashboard_siswa.fxml";

        FXMLLoader loader = new FXMLLoader(getClass().getResource(dashboardFxml));
        Parent root = loader.load();

        if (Session.currentRole.equalsIgnoreCase("admin")) {
            DashboardController controller = loader.getController();
            controller.setUserRole("admin");
        } else {
            DashboardSiswaController controller = loader.getController();
            controller.setUserRole("student");
        }

        Stage stage = (Stage) kembaliButton.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Dashboard");
        stage.show();
    } catch (IOException e) {
        e.printStackTrace();
    }
}



    private void bukaKuis(Materi materi) {
        try {
            System.out.println("? Materi dipilih: " + materi.getTitle());

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/jawab.fxml")); 
            Parent root = loader.load();

            JawabController controller = loader.getController();
            controller.loadSoalMateri(materi.getId(), Session.userId);

            Stage stage = (Stage) daftarMateriVBox.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Kuis: " + materi.getTitle());
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "❌ Gagal membuka kuis: " + e.getMessage());
        }
    }


    private void showAlert(Alert.AlertType type, String msg) {
        Alert alert = new Alert(type);
        alert.setContentText(msg);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}