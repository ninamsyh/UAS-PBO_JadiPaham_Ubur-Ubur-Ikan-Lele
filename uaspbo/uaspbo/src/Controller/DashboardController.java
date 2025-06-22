package Controller;

import Service.DatabaseService;
import Service.Session;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.Cursor;

public class DashboardController {

    @FXML private Button materiButton;
    @FXML private Button kuisButton;
    @FXML private Button laporanButton;
    @FXML private Button logoutButton;
    @FXML private Button crudMateriButton;
    @FXML private Button crudKuisButton;
    @FXML private Button crudSiswaButton;
    @FXML private Button laporanAktivitasButton; 
    @FXML private ImageView profileImage;
    @FXML private Button backupButton;
    @FXML private Button restoreButton;

    @FXML private VBox siswaStatsBox;
    @FXML private Label lblMateriDiselesaikan;
    @FXML private Label lblKuisDikerjakan;
    @FXML private Label lblNotifikasi;

    protected String userRole;

    public void setUserRole(String role) {
        this.userRole = role;
        logoutButton.setOnAction(e -> handleLogout());
        loadProfileImage();

        if ("admin".equalsIgnoreCase(role)) {
            if (crudMateriButton != null) {
                crudMateriButton.setVisible(true);
                crudMateriButton.setOnAction(e -> goToCrudMateri());
            }

            if (crudKuisButton != null) {
                crudKuisButton.setVisible(true);
                crudKuisButton.setOnAction(e -> goToCrudKuis());
            }

            if (crudSiswaButton != null) {
                crudSiswaButton.setVisible(true);
                crudSiswaButton.setOnAction(e -> goToCrudSiswa());
            }

            if (laporanAktivitasButton != null) {
                laporanAktivitasButton.setVisible(true);
                laporanAktivitasButton.setOnAction(e -> goToLaporanAktivitas());
            }

            if (materiButton != null) materiButton.setVisible(false);
            if (kuisButton != null) kuisButton.setVisible(false);
            if (laporanButton != null) laporanButton.setVisible(false);

            if (siswaStatsBox != null) {
                siswaStatsBox.setVisible(false);
            }
        } else {
            if (crudMateriButton != null) crudMateriButton.setVisible(false);
            if (crudKuisButton != null) crudKuisButton.setVisible(false);
            if (crudSiswaButton != null) crudSiswaButton.setVisible(false);
            if (laporanAktivitasButton != null) laporanAktivitasButton.setVisible(false);

            if (materiButton != null) materiButton.setOnAction(e -> goToMateriSiswa());
            if (kuisButton != null) kuisButton.setOnAction(e -> goToKuisSiswa());
            if (laporanButton != null) laporanButton.setOnAction(e -> goToLaporanNilai());

            if (siswaStatsBox != null) {
                siswaStatsBox.setVisible(true);
                lblMateriDiselesaikan.setText("Materi selesai: 3");
                lblKuisDikerjakan.setText("Kuis dikerjakan: 2");
                lblNotifikasi.setText("📢 Ada kuis baru minggu ini.");
            }
        }
    }

    private void loadProfileImage() {
        try (Connection conn = DatabaseService.getConnection()) {
            String sql = "SELECT profile_picture FROM users WHERE username = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, Session.username);
            ResultSet rs = stmt.executeQuery();

            String defaultPath = "/images/user.png";

            if (rs.next()) {
                String path = rs.getString("profile_picture");
                if (path != null && !path.isEmpty()) {
                    File file = new File(path);
                    if (file.exists()) {
                        profileImage.setImage(new Image(file.toURI().toString()));
                        return;
                    }
                }
            }

            profileImage.setImage(new Image(getClass().getResourceAsStream(defaultPath)));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleProfileImageClick() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Pilih Foto Profil");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.jpeg", "*.png"));
        File selectedFile = chooser.showOpenDialog(null);

        if (selectedFile != null) {
            try {
                profileImage.setImage(new Image(selectedFile.toURI().toString()));
                try (Connection conn = DatabaseService.getConnection()) {
                    String sql = "UPDATE users SET profile_picture = ? WHERE username = ?";
                    PreparedStatement stmt = conn.prepareStatement(sql);
                    stmt.setString(1, selectedFile.getAbsolutePath());
                    stmt.setString(2, Session.username);
                    stmt.executeUpdate();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private void goToCrudMateri() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/View/materi.fxml"));
            Stage stage = (Stage) materiButton.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void goToMateriSiswa() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/View/materi_siswa.fxml"));
            Stage stage = (Stage) materiButton.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void goToKuisSiswa() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/pilihmateri.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) kuisButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Pilih Materi Kuis");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleLogout() {
        try {
            Parent loginRoot = FXMLLoader.load(getClass().getResource("/View/login.fxml"));
            Stage stage = (Stage) logoutButton.getScene().getWindow();
            stage.setScene(new Scene(loginRoot));
            stage.setTitle("Login - uaspbo");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void goToCrudKuis(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/kuis.fxml"));
            Parent root = loader.load();
            KuisController controller = loader.getController();
            controller.setMateriId(1);
            Stage stage = new Stage();
            stage.setTitle("Manajemen Soal Kuis");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void goToCrudSiswa(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/admin.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Manajemen Siswa");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void goToLaporanAktivitas() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/View/pilihlaporan.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Laporan Aktivitas");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void goToLaporanNilai(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/Laporan.fxml"));
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

    private void backupDatabase() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Simpan Backup Database");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("SQL File", "*.sql"));
        File file = chooser.showSaveDialog(null);

        if (file != null) {
            try {
                ProcessBuilder pb = new ProcessBuilder(
                    "mysqldump", "-u", "root", "uaspbo"
                );
                pb.redirectOutput(file);
                Process process = pb.start();
                int exitCode = process.waitFor();

                if (exitCode == 0) {
                    System.out.println("✅ Backup berhasil disimpan di: " + file.getAbsolutePath());
                } else {
                    System.err.println("❌ Backup gagal dengan kode: " + exitCode);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void restoreDatabase() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Pilih File Backup (.sql)");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("SQL File", "*.sql"));
        File file = chooser.showOpenDialog(null);

        if (file != null) {
            try {
                ProcessBuilder pb = new ProcessBuilder(
                    "mysql", "-u", "root", "uaspbo", "-e", "source " + file.getAbsolutePath()
                );

                Process process = pb.start();
                int exitCode = process.waitFor();

                if (exitCode == 0) {
                    System.out.println("✅ Restore berhasil dari file: " + file.getAbsolutePath());
                } else {
                    System.err.println("❌ Restore gagal dengan kode: " + exitCode);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void initialize() {
        if (profileImage != null) {
            profileImage.setCursor(Cursor.HAND);
            profileImage.setOnMouseClicked(e -> handleProfileImageClick());
        }

        if (backupButton != null) {
            backupButton.setOnAction(e -> backupDatabase());
        }

        if (restoreButton != null) {
            restoreButton.setOnAction(e -> restoreDatabase());
        }
    }
}
