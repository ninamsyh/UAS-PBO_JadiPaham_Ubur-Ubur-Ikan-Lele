package Controller;

import Model.Materi;
import Model.Nilai;
import Model.Admin;
import Service.NilaiService;
import Service.AuthService;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;

import java.io.FileOutputStream;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class LaporanAktivitasController {

    @FXML private TableView<LaporanRow> tableView;
    @FXML private TableColumn<LaporanRow, String> usernameColumn;
    @FXML private TableColumn<LaporanRow, String> nilaiColumn;
    @FXML private Button downloadButton;

    private final ObservableList<LaporanRow> dataLaporan = FXCollections.observableArrayList();
    private String namaMateri = "Materi Tidak Diketahui";

    @FXML
    public void initialize() {
        usernameColumn.setCellValueFactory(cellData -> cellData.getValue().usernameProperty());
        nilaiColumn.setCellValueFactory(cellData -> cellData.getValue().nilaiProperty());

        tableView.setItems(dataLaporan);
    }

   
    public void loadData(Materi materi) {
        if (materi == null) return;
        loadData(materi.getId(), materi.getTitle());
    }

    
    public void loadData(int materiId, String namaMateri) {
        this.namaMateri = namaMateri;

        List<Nilai> semuaNilai = NilaiService.getNilaiByMateri(materiId);
        Map<Integer, List<Nilai>> nilaiPerUser = semuaNilai.stream()
                .collect(Collectors.groupingBy(Nilai::getUserId));

        List<Admin> semuaSiswa = AuthService.getAllSiswa();

        dataLaporan.clear();

        for (Admin siswa : semuaSiswa) {
            List<Nilai> nilaiSiswa = nilaiPerUser.get(siswa.getId());
            if (nilaiSiswa != null && !nilaiSiswa.isEmpty()) {
                double total = nilaiSiswa.stream().mapToDouble(Nilai::getScore).sum();
                double persen = (total / nilaiSiswa.size()) * 100;
                String hasil = String.format("%.0f%%", persen);
                dataLaporan.add(new LaporanRow(siswa.getUsername(), hasil));
            }
        }
    }

    @FXML
    private void handleDownload() {
        if (dataLaporan.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Tidak ada data untuk diunduh.");
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Simpan Laporan");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        fileChooser.setInitialFileName("laporan_aktivitas_siswa.pdf");

        java.io.File file = fileChooser.showSaveDialog(tableView.getScene().getWindow());
        if (file != null) {
            generatePDF(file.getAbsolutePath());
        }
    }

    private void generatePDF(String path) {
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream(path));
            document.open();

            Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD, BaseColor.BLUE);
            Paragraph title = new Paragraph("Laporan Aktivitas Siswa\n\n" + namaMateri + "\n\n", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            PdfPTable table = new PdfPTable(2);
            table.setWidthPercentage(100);
            table.addCell("Username");
            table.addCell("Nilai (%)");

            for (LaporanRow row : dataLaporan) {
                table.addCell(row.getUsername());
                table.addCell(row.getNilai());
            }

            document.add(table);
            showAlert(Alert.AlertType.INFORMATION, "PDF berhasil disimpan.");

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Gagal menyimpan PDF: " + e.getMessage());
        } finally {
            document.close();
        }
    }

    private void showAlert(Alert.AlertType type, String pesan) {
        Alert alert = new Alert(type);
        alert.setTitle("Laporan");
        alert.setHeaderText(null);
        alert.setContentText(pesan);
        alert.showAndWait();
    }

    
    public static class LaporanRow {
        private final javafx.beans.property.SimpleStringProperty username;
        private final javafx.beans.property.SimpleStringProperty nilai;

        public LaporanRow(String username, String nilai) {
            this.username = new javafx.beans.property.SimpleStringProperty(username);
            this.nilai = new javafx.beans.property.SimpleStringProperty(nilai);
        }

        public String getUsername() {
            return username.get();
        }

        public javafx.beans.property.StringProperty usernameProperty() {
            return username;
        }

        public String getNilai() {
            return nilai.get();
        }

        public javafx.beans.property.StringProperty nilaiProperty() {
            return nilai;
        }
    }
}