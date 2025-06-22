package Service;

import Model.Siswa;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SiswaService {

    public static List<Siswa> getAllSiswa() {
        List<Siswa> list = new ArrayList<>();
        String sql = "SELECT id, username, password, role, profile_picture FROM users WHERE role = 'student' ORDER BY id";

        try (Connection conn = DatabaseService.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Siswa siswa = new Siswa(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("role"),  
                        rs.getString("profile_picture")
                );
                list.add(siswa);
            }

        } catch (SQLException e) {
            System.err.println("❌ Gagal mengambil data siswa: " + e.getMessage());
        }

        return list;
    }

    public static void tambahSiswa(Siswa siswa) {
        String sql = "INSERT INTO users (username, password, role, profile_picture) VALUES (?, ?, 'student', ?)";

        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, siswa.getUsername());
            stmt.setString(2, siswa.getPassword());
            stmt.setString(3, siswa.getProfilePicture());

            stmt.executeUpdate();
            System.out.println("✅ Siswa berhasil ditambahkan.");

        } catch (SQLException e) {
            System.err.println("❌ Gagal menambah siswa: " + e.getMessage());
        }
    }

    public static void updateSiswa(Siswa siswa) {
        boolean updatePassword = siswa.getPassword() != null && !siswa.getPassword().isEmpty();
        String sql;

        if (updatePassword) {
            sql = "UPDATE users SET username = ?, password = ?, profile_picture = ? WHERE id = ?";
        } else {
            sql = "UPDATE users SET username = ?, profile_picture = ? WHERE id = ?";
        }

        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, siswa.getUsername());

            if (updatePassword) {
                stmt.setString(2, siswa.getPassword());
                stmt.setString(3, siswa.getProfilePicture());
                stmt.setInt(4, siswa.getId());
            } else {
                stmt.setString(2, siswa.getProfilePicture());
                stmt.setInt(3, siswa.getId());
            }

            stmt.executeUpdate();
            System.out.println("✅ Siswa berhasil diperbarui.");

        } catch (SQLException e) {
            System.err.println("❌ Gagal mengupdate siswa: " + e.getMessage());
        }
    }

    public static void hapusSiswa(int id) {
        String sql = "DELETE FROM users WHERE id = ?";

        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
            System.out.println("🗑 Siswa berhasil dihapus.");

        } catch (SQLException e) {
            System.err.println("❌ Gagal menghapus siswa: " + e.getMessage());
        }
    }
}