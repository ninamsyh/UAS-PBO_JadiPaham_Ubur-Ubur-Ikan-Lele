package Service;

import Model.Soal;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SoalService {

    public static void tambahSoal(Soal soal) {
        String sql = """
            INSERT INTO soal (
                materi_id, question, option_a, option_b, option_c, option_d,
                correct_option, created_at, updated_at
            ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, soal.getMateriId());
            stmt.setString(2, soal.getQuestion());
            stmt.setString(3, soal.getOptionA());
            stmt.setString(4, soal.getOptionB());
            stmt.setString(5, soal.getOptionC());
            stmt.setString(6, soal.getOptionD());
            stmt.setString(7, soal.getCorrectOption());
            stmt.setTimestamp(8, Timestamp.valueOf(soal.getCreatedAt()));
            stmt.setTimestamp(9, Timestamp.valueOf(soal.getUpdatedAt()));

            stmt.executeUpdate();
            System.out.println("✅ Soal berhasil ditambahkan ke database.");

        } catch (SQLException e) {
            System.err.println("❌ Gagal menambahkan soal: " + e.getMessage());
        }
    }

    public static List<Soal> getSoalByMateri(int materiId) {
        List<Soal> list = new ArrayList<>();
        String sql = "SELECT * FROM soal WHERE materi_id = ? ORDER BY id DESC";

        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, materiId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Soal soal = new Soal(
                        rs.getInt("id"),
                        rs.getInt("materi_id"),
                        rs.getString("question"),
                        rs.getString("option_a"),
                        rs.getString("option_b"),
                        rs.getString("option_c"),
                        rs.getString("option_d"),
                        rs.getString("correct_option"),
                        rs.getTimestamp("created_at").toLocalDateTime(),
                        rs.getTimestamp("updated_at").toLocalDateTime()
                );
                list.add(soal);
            }

        } catch (SQLException e) {
            System.err.println("❌ Gagal mengambil data soal: " + e.getMessage());
        }

        return list;
    }

    public static void updateSoal(Soal soal) {
        String sql = """
            UPDATE soal SET
                question = ?, option_a = ?, option_b = ?, option_c = ?, option_d = ?,
                correct_option = ?, updated_at = ?
            WHERE id = ?
        """;

        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, soal.getQuestion());
            stmt.setString(2, soal.getOptionA());
            stmt.setString(3, soal.getOptionB());
            stmt.setString(4, soal.getOptionC());
            stmt.setString(5, soal.getOptionD());
            stmt.setString(6, soal.getCorrectOption());
            stmt.setTimestamp(7, Timestamp.valueOf(soal.getUpdatedAt()));
            stmt.setInt(8, soal.getId());

            stmt.executeUpdate();
            System.out.println("✅ Soal berhasil diupdate.");

        } catch (SQLException e) {
            System.err.println("❌ Gagal mengupdate soal: " + e.getMessage());
        }
    }

    public static void hapusSoal(int id) {
        String sql = "DELETE FROM soal WHERE id = ?";

        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
            System.out.println("🗑 Soal berhasil dihapus.");

        } catch (SQLException e) {
            System.err.println("❌ Gagal menghapus soal: " + e.getMessage());
        }
    }
}