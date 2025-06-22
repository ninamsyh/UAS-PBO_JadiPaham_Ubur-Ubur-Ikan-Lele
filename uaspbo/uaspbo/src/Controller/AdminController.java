package Controller;

import Model.Siswa;
import Service.SiswaService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public class AdminController {

    @FXML private TableView<Siswa> tabelSiswa;
    @FXML private TableColumn<Siswa, Integer> colId;
    @FXML private TableColumn<Siswa, String> colUsername;
    @FXML private TableColumn<Siswa, String> colFoto;
    @FXML private TableColumn<Siswa, Void> colAksi;

    @FXML private TextField usernameField, fotoField;
    @FXML private PasswordField passwordField;
    @FXML private ImageView previewFoto;
    @FXML private Button tambahButton, hapusButton, resetButton;

    private ObservableList<Siswa> siswaList = FXCollections.observableArrayList();
    private Siswa siswaYangDipilih = null;
    private File fotoTerpilih;

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
        colFoto.setCellValueFactory(new PropertyValueFactory<>("profilePicture"));

        colAksi.setCellFactory(param -> new TableCell<>() {
            private final Button btnEdit = new Button("Edit");

            {
                btnEdit.setOnAction(e -> {
                    Siswa siswa = getTableView().getItems().get(getIndex());
                    loadSiswaToForm(siswa);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : new HBox(btnEdit));
            }
        });

        tabelSiswa.setItems(siswaList);
        loadSiswa();
    }

    private void loadSiswa() {
        List<Siswa> data = SiswaService.getAllSiswa();
        siswaList.setAll(data);
    }

    private void loadSiswaToForm(Siswa siswa) {
        siswaYangDipilih = siswa;
        usernameField.setText(siswa.getUsername());
        passwordField.clear();
        fotoField.setText(siswa.getProfilePicture());
        if (siswa.getProfilePicture() != null) {
            previewFoto.setImage(new Image("file:" + siswa.getProfilePicture()));
        }
        tambahButton.setText("Simpan");
    }

    @FXML
    private void pilihFoto() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Pilih Foto");
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("Image Files", ".jpg", ".jpeg", "*.png")
        );
        fotoTerpilih = fileChooser.showOpenDialog(null);
        if (fotoTerpilih != null) {
            fotoField.setText(fotoTerpilih.getAbsolutePath());
            previewFoto.setImage(new Image(fotoTerpilih.toURI().toString()));
        }
    }

    @FXML
    private void tambahSiswa() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        if (username.isEmpty() || (siswaYangDipilih == null && password.isEmpty())) {
            showAlert(Alert.AlertType.WARNING, "Harap lengkapi username dan password.");
            return;
        }

        String profilePath = null;
        if (fotoTerpilih != null) {
            try {
                Path dest = Path.of("profile_pics", fotoTerpilih.getName());
                Files.createDirectories(dest.getParent());
                Files.copy(fotoTerpilih.toPath(), dest, StandardCopyOption.REPLACE_EXISTING);
                profilePath = dest.toString();
            } catch (Exception e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Gagal menyimpan foto.");
                return;
            }
        }

        if (siswaYangDipilih == null) {
            
            String hashedPassword = hashMD5(password);
            Siswa siswaBaru = new Siswa(0, username, hashedPassword, "student", profilePath);
            SiswaService.tambahSiswa(siswaBaru);
            showAlert(Alert.AlertType.INFORMATION, "Siswa berhasil ditambahkan.");
        } else {
            siswaYangDipilih.setUsername(username);
            if (!password.isEmpty()) {
                siswaYangDipilih.setPassword(hashMD5(password)); // Hash password baru jika diubah
            }
            if (profilePath != null) siswaYangDipilih.setProfilePicture(profilePath);
            SiswaService.updateSiswa(siswaYangDipilih);
            showAlert(Alert.AlertType.INFORMATION, "Data siswa diperbarui.");
        }

        resetForm();
        loadSiswa();
    }

    @FXML
    private void hapusSiswa() {
        Siswa siswa = tabelSiswa.getSelectionModel().getSelectedItem();
        if (siswa != null) {
            SiswaService.hapusSiswa(siswa.getId());
            showAlert(Alert.AlertType.INFORMATION, "Siswa berhasil dihapus.");
            loadSiswa();
            resetForm();
        } else {
            showAlert(Alert.AlertType.WARNING, "Pilih siswa yang akan dihapus.");
        }
    }

    @FXML
    private void resetForm() {
        usernameField.clear();
        passwordField.clear();
        fotoField.clear();
        previewFoto.setImage(null);
        siswaYangDipilih = null;
        fotoTerpilih = null;
        tambahButton.setText("Tambah");
    }

    private void showAlert(Alert.AlertType type, String message) {
        new Alert(type, message).showAndWait();
    }

    
    private String hashMD5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : messageDigest) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5 algorithm not found.", e);
        }
    }
}