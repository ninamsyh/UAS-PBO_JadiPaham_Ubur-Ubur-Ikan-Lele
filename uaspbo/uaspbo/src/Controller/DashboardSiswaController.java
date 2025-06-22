package Controller;

import Model.Materi;
import Service.MateriService;
import Service.NilaiService;
import Service.Session;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class DashboardSiswaController extends DashboardController {

    @FXML private Label lblMateriDiselesaikan;
    @FXML private Label lblKuisDikerjakan;
    @FXML private Label lblNotifikasi;
    @FXML private PieChart chartProgresMateri;

    @Override
    public void setUserRole(String role) {
        super.setUserRole(role);
        loadStatistikSiswa();
        loadStatistikChart();
    }

    private void loadStatistikSiswa() {
        int selesai = NilaiService.hitungJumlahMateriDiselesaikan(Session.userId);
        int total = MateriService.getAllMateri().size();
        int totalKuis = NilaiService.hitungJumlahSoalDikerjakan(Session.userId);

        if (lblMateriDiselesaikan != null)
            lblMateriDiselesaikan.setText("Materi selesai: " + selesai + " dari " + total);
        if (lblKuisDikerjakan != null)
            lblKuisDikerjakan.setText("Kuis dikerjakan: " + totalKuis);
        if (lblNotifikasi != null)
            lblNotifikasi.setText("📢 Terus semangat belajar!");
    }

    private void loadStatistikChart() {
        int totalMateri = MateriService.getAllMateri().size();
        int selesai = NilaiService.hitungJumlahMateriDiselesaikan(Session.userId);

        if (chartProgresMateri != null) {
            chartProgresMateri.setData(FXCollections.observableArrayList(
                new PieChart.Data("Selesai", selesai),
                new PieChart.Data("Belum", totalMateri - selesai)
            ));
        }
    }

    private void laporanNilai() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/laporan.fxml"));
            Parent root = loader.load();

            LaporanController controller = loader.getController();
            controller.loadData();

            Stage stage = new Stage();
            stage.setTitle("Laporan Nilai Saya");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void initialize() {
        super.initialize();
    }
}






