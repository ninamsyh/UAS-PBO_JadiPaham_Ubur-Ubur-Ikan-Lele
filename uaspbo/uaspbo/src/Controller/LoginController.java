package Controller;

import Service.DatabaseService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LoginController {
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label statusLabel;

    @FXML
    public void handleLogin() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            statusLabel.setText("Username dan Password wajib diisi!");
            return;
        }

        try (Connection conn = DatabaseService.getConnection()) {
            String sql = "SELECT * FROM users WHERE username = ? AND password = MD5(?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String role = rs.getString("role");
            System.out.println("Login berhasil sebagai: " + role);
            Service.Session.username = username;
            Service.Session.currentRole = role; // simpan role ke Session
            Service.Session.userId = rs.getInt("id"); 
            switchToDashboard(role);

            } else {
                statusLabel.setText("Username atau password salah!");
            }

        } catch (Exception e) {
            e.printStackTrace();
            statusLabel.setText("Koneksi database gagal.");
        }
    }

    private void switchToDashboard(String role) {
    try {
        String fxml = role.equalsIgnoreCase("admin") ? "/View/dashboard.fxml" : "/View/dashboard_siswa.fxml";
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
        Parent dashboardRoot = loader.load();

        if (role.equalsIgnoreCase("admin")) {
            DashboardController controller = loader.getController();
            controller.setUserRole(role);
        } else {
            DashboardSiswaController controller = loader.getController();
            controller.setUserRole(role);
        }

        Stage window = (Stage) usernameField.getScene().getWindow();
        window.setScene(new Scene(dashboardRoot));
        window.setTitle("Dashboard - " + role);
    } catch (Exception e) {
        e.printStackTrace();
        statusLabel.setText("Gagal membuka Dashboard.");
    }
}

}
