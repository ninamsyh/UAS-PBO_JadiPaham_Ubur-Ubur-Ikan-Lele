package Controller;

import Model.Nilai;
import Model.Soal;
import Service.NilaiService;
import Service.SoalService;
import Service.Session;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public class JawabController {

    @FXML private RadioButton radioA, radioB, radioC, radioD;
    @FXML private Label labelPertanyaan;
    @FXML private Button kirimButton;

    private ToggleGroup groupJawaban;

    private int userId;
    private int materiId;
    private List<Soal> soalList;
    private int indexSoal = 0;

    @FXML
    public void initialize() {
        groupJawaban = new ToggleGroup();
        radioA.setToggleGroup(groupJawaban);
        radioB.setToggleGroup(groupJawaban);
        radioC.setToggleGroup(groupJawaban);
        radioD.setToggleGroup(groupJawaban);
    }

    public void loadSoalMateri(int materiId, int userId) {
        this.materiId = materiId;
        this.userId = userId;
        this.indexSoal = 0;

        if (NilaiService.sudahSelesaiKerjakanMateri(userId, materiId)) {
            double skor = NilaiService.hitungSkorAkhir(userId, materiId);
            showAlert(AlertType.INFORMATION, "✅ Anda sudah menyelesaikan kuis ini.\nSkor: " + (int) skor);
            kembaliKeDashboard();
            return;
        }

        this.soalList = SoalService.getSoalByMateri(materiId);
        if (soalList.isEmpty()) {
            showAlert(AlertType.INFORMATION, "📂 Tidak ada soal dalam materi ini.");
            kembaliKeDashboard();
            return;
        }

        tampilkanSoal();
    }

    private void tampilkanSoal() {
        if (indexSoal >= soalList.size()) {
            double skor = NilaiService.hitungSkorAkhir(userId, materiId);
            showAlert(AlertType.INFORMATION, "🎉 Kuis selesai!\nSkor akhir: " + (int) skor);
            kembaliKeDashboard();
            return;
        }

        Soal soal = soalList.get(indexSoal);

        if (NilaiService.sudahMenjawab(userId, soal.getId())) {
            indexSoal++;
            tampilkanSoal();
            return;
        }

        labelPertanyaan.setText("Soal " + (indexSoal + 1) + ": " + soal.getQuestion());
        radioA.setText(soal.getOptionA());
        radioB.setText(soal.getOptionB());
        radioC.setText(soal.getOptionC());
        radioD.setText(soal.getOptionD());
        groupJawaban.selectToggle(null);
    }

    @FXML
    private void kirimJawaban() {
        if (indexSoal >= soalList.size()) return;

        RadioButton selected = (RadioButton) groupJawaban.getSelectedToggle();
        if (selected == null) {
            showAlert(AlertType.WARNING, "⚠️ Pilih salah satu jawaban sebelum melanjutkan.");
            return;
        }

        Soal soal = soalList.get(indexSoal);
        String jawabanUser = selected.getText();
        boolean benar = jawabanUser.equalsIgnoreCase(getOptionText(soal.getCorrectOption(), soal));

        Nilai nilai = new Nilai(0, userId, soal.getId(), benar ? 1.0 : 0.0, LocalDateTime.now());
        NilaiService.simpanJawaban(nilai);

        showAlert(AlertType.INFORMATION, benar
                ? "✅ Jawaban Anda BENAR!"
                : "❌ Jawaban Anda SALAH.\nJawaban yang benar: " + getOptionText(soal.getCorrectOption(), soal));

        indexSoal++;
        tampilkanSoal();
    }

    private String getOptionText(String correctOption, Soal soal) {
        return switch (correctOption.toUpperCase()) {
            case "A" -> soal.getOptionA();
            case "B" -> soal.getOptionB();
            case "C" -> soal.getOptionC();
            case "D" -> soal.getOptionD();
            default -> "";
        };
    }

    private void showAlert(AlertType type, String message) {
        Alert alert = new Alert(type);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void kembaliKeDashboard() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    Session.currentRole.equals("admin") ? "/View/dashboard.fxml" : "/View/dashboard_siswa.fxml"));
            Parent root = loader.load();

            if (Session.currentRole.equals("admin")) {
                DashboardController controller = loader.getController();
                controller.setUserRole("admin");
            } else {
                DashboardSiswaController controller = loader.getController();
                controller.setUserRole("student");
            }

            Stage stage = (Stage) kirimButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Dashboard");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "❌ Gagal kembali ke dashboard.");
        }
    }
}
