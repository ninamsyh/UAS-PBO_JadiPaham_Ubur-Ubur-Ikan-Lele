package Service;

import Model.Materi;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MateriService {

    public static List<Materi> getAllMateri() {
        List<Materi> list = new ArrayList<>();
        String sql = "SELECT id, title, description, file_path FROM materi ORDER BY id";

        try (Connection conn = DatabaseService.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Materi materi = new Materi(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getString("file_path")
                );
                list.add(materi);
            }

        } catch (SQLException e) {
            System.err.println("❌ Gagal mengambil daftar materi: " + e.getMessage());
        }

        return list;
    }

    public static boolean insertMateri(Materi materi) {
        String sql = "INSERT INTO materi (title, description, file_path) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, materi.getTitle());
            stmt.setString(2, materi.getDescription());
            stmt.setString(3, materi.getFilePath());
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean updateMateri(Materi materi) {
        String sql = "UPDATE materi SET title=?, description=?, file_path=? WHERE id=?";

        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, materi.getTitle());
            stmt.setString(2, materi.getDescription());
            stmt.setString(3, materi.getFilePath());
            stmt.setInt(4, materi.getId());
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean deleteMateri(int id) {
        String sql = "DELETE FROM materi WHERE id=?";

        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
