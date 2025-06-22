package Controller;

import Model.Materi;
import Model.Soal;
import Service.MateriService;
import Service.SoalService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.util.StringConverter;

import java.time.LocalDateTime;
import java.util.List;

public class KuisController {

    @FXML private ComboBox<Materi> materiComboBox;
    @FXML private TableView<Soal> tabelSoal;
    @FXML private TableColumn<Soal, String> colPertanyaan;
    @FXML private TableColumn<Soal, String> colJawaban;
    @FXML private TableColumn<Soal, Void> colAksi;

    @FXML private TextField pertanyaanField, aField, bField, cField, dField, jawabanField;
    @FXML private Button tambahButton;

    private ObservableList<Soal> soalList = FXCollections.observableArrayList();
    private Soal soalYangDiedit = null;

    @FXML
    public void initialize() {
        
        List<Materi> daftarMateri = MateriService.getAllMateri();
        materiComboBox.setItems(FXCollections.observableArrayList(daftarMateri));

       
        materiComboBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(Materi materi) {
                return materi != null ? materi.getTitle() : "";
            }

            @Override
            public Materi fromString(String string) {
                return null;
            }
        });

       
        materiComboBox.setOnAction(e -> loadSoal());

        colPertanyaan.setCellValueFactory(new PropertyValueFactory<>("question"));
        colJawaban.setCellValueFactory(new PropertyValueFactory<>("correctOption"));

        colAksi.setCellFactory(param -> new TableCell<>() {
            private final Button btnEdit = new Button("Edit");
            private final Button btnHapus = new Button("Hapus");

            {
                btnEdit.setOnAction(e -> {
                    Soal soal = getTableView().getItems().get(getIndex());
                    loadSoalToForm(soal);
                });

                btnHapus.setOnAction(e -> {
                    Soal soal = getTableView().getItems().get(getIndex());
                    SoalService.hapusSoal(soal.getId());
                    loadSoal();
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox box = new HBox(5, btnEdit, btnHapus);
                    setGraphic(box);
                }
            }
        });

        tabelSoal.setItems(soalList);
    }

    private void loadSoal() {
        Materi materi = materiComboBox.getValue();
        if (materi != null) {
            List<Soal> data = SoalService.getSoalByMateri(materi.getId());
            soalList.setAll(data);
            tabelSoal.refresh();
        }
    }

    private void loadSoalToForm(Soal soal) {
        soalYangDiedit = soal;

        pertanyaanField.setText(soal.getQuestion());
        aField.setText(soal.getOptionA());
        bField.setText(soal.getOptionB());
        cField.setText(soal.getOptionC());
        dField.setText(soal.getOptionD());
        jawabanField.setText(soal.getCorrectOption());

        tambahButton.setText("Simpan Perubahan");

       
    }

    @FXML
    private void tambahSoal() {
        try {
            
            int materiId;
            if (soalYangDiedit == null) {
                Materi materi = materiComboBox.getValue();
                if (materi == null) {
                    showAlert(Alert.AlertType.ERROR, "Pilih materi terlebih dahulu.");
                    return;
                }
                materiId = materi.getId();
            } else {
                
                materiId = soalYangDiedit.getMateriId();
            }

            String question = pertanyaanField.getText().trim();
            String a = aField.getText().trim();
            String b = bField.getText().trim();
            String c = cField.getText().trim();
            String d = dField.getText().trim();
            String correct = jawabanField.getText().trim().toUpperCase();

            if (question.isEmpty() || a.isEmpty() || b.isEmpty() || c.isEmpty() || d.isEmpty() || !correct.matches("[A-D]")) {
                showAlert(Alert.AlertType.WARNING, "Lengkapi semua field dan jawaban harus A-D.");
                return;
            }

            if (soalYangDiedit == null) {
                Soal soal = new Soal(0, materiId, question, a, b, c, d, correct, LocalDateTime.now(), LocalDateTime.now());
                SoalService.tambahSoal(soal);
                showAlert(Alert.AlertType.INFORMATION, "Soal berhasil ditambahkan!");
            } else {
                soalYangDiedit.setQuestion(question);
                soalYangDiedit.setOptionA(a);
                soalYangDiedit.setOptionB(b);
                soalYangDiedit.setOptionC(c);
                soalYangDiedit.setOptionD(d);
                soalYangDiedit.setCorrectOption(correct);
                soalYangDiedit.setUpdatedAt(LocalDateTime.now());

                SoalService.updateSoal(soalYangDiedit);
                showAlert(Alert.AlertType.INFORMATION, "Soal berhasil diperbarui!");
                soalYangDiedit = null;
            }

            clearForm();
            loadSoal();

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Terjadi kesalahan: " + e.getMessage());
        }
    }

    private void clearForm() {
        pertanyaanField.clear();
        aField.clear();
        bField.clear();
        cField.clear();
        dField.clear();
        jawabanField.clear();

        soalYangDiedit = null;
        tambahButton.setText("Tambah Soal");
    }

    private void showAlert(Alert.AlertType type, String message) {
        new Alert(type, message).showAndWait();
    }

    public void setMateriId(int id) {
        for (Materi m : materiComboBox.getItems()) {
            if (m.getId() == id) {
                materiComboBox.setValue(m);
                break;
            }
        }
        loadSoal();
    }
}