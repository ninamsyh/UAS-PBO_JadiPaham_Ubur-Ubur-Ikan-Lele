package Controller;

import Model.Materi;
import Model.Nilai;
import Service.MateriService;
import Service.NilaiService;
import Service.Session;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.List;

public class LaporanController {

    @FXML
    private TableView<LaporanRow> tableView;

    @FXML
    private TableColumn<LaporanRow, String> materiColumn;

    @FXML
    private TableColumn<LaporanRow, String> nilaiColumn;

    private final ObservableList<LaporanRow> dataLaporan = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        materiColumn.setCellValueFactory(cellData -> cellData.getValue().materiProperty());
        nilaiColumn.setCellValueFactory(cellData -> cellData.getValue().nilaiProperty());
        tableView.setItems(dataLaporan);

        loadData();
    }

    public void loadData() {
        int userId = Session.getCurrentUserId();
        dataLaporan.clear();

        List<Materi> materiList = MateriService.getAllMateri();
        for (Materi materi : materiList) {
            double skor = NilaiService.hitungSkorAkhir(userId, materi.getId());
            if (skor > 0) {
                String persentase = String.format("%.0f%%", skor);
                dataLaporan.add(new LaporanRow(materi.getTitle(), persentase));
            }
        }
    }

    public static class LaporanRow {
        private final SimpleStringProperty materi;
        private final SimpleStringProperty nilai;

        public LaporanRow(String materi, String nilai) {
            this.materi = new SimpleStringProperty(materi);
            this.nilai = new SimpleStringProperty(nilai);
        }

        public String getMateri() {
            return materi.get();
        }

        public SimpleStringProperty materiProperty() {
            return materi;
        }

        public String getNilai() {
            return nilai.get();
        }

        public SimpleStringProperty nilaiProperty() {
            return nilai;
        }
    }
}