package Service;

import Model.Nilai;
import javafx.util.Pair;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class NilaiService {

    
    public static void simpanJawaban(Nilai nilai) {
        String sql = "INSERT INTO nilai (user_id, soal_id, score, answered_at) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, nilai.getUserId());
            stmt.setInt(2, nilai.getSoalId());
            stmt.setDouble(3, nilai.getScore());
            stmt.setTimestamp(4, Timestamp.valueOf(nilai.getAnsweredAt()));

            stmt.executeUpdate();
            System.out.println("✅ Jawaban disimpan.");

        } catch (SQLException e) {
            System.err.println("❌ Gagal menyimpan jawaban: " + e.getMessage());
        }
    }

    
    public static boolean sudahMenjawab(int userId, int soalId) {
        String sql = "SELECT COUNT(*) FROM nilai WHERE user_id = ? AND soal_id = ?";

        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            stmt.setInt(2, soalId);

            ResultSet rs = stmt.executeQuery();
            return rs.next() && rs.getInt(1) > 0;

        } catch (SQLException e) {
            System.err.println("❌ Gagal mengecek jawaban: " + e.getMessage());
            return false;
        }
    }

    
    public static int hitungJawabanBenar(int userId, int materiId) {
        String sql = """
            SELECT COUNT(*) FROM nilai n
            JOIN soal s ON n.soal_id = s.id
            WHERE n.user_id = ? AND s.materi_id = ? AND n.score = 1
        """;

        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            stmt.setInt(2, materiId);

            ResultSet rs = stmt.executeQuery();
            return rs.next() ? rs.getInt(1) : 0;

        } catch (SQLException e) {
            System.err.println("❌ Gagal menghitung jawaban benar: " + e.getMessage());
            return 0;
        }
    }

    
    public static int hitungJumlahSoalDijawab(int userId, int materiId) {
        String sql = """
            SELECT COUNT(*) FROM nilai n
            JOIN soal s ON n.soal_id = s.id
            WHERE n.user_id = ? AND s.materi_id = ?
        """;

        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            stmt.setInt(2, materiId);

            ResultSet rs = stmt.executeQuery();
            return rs.next() ? rs.getInt(1) : 0;

        } catch (SQLException e) {
            System.err.println("❌ Gagal menghitung jumlah soal dijawab: " + e.getMessage());
            return 0;
        }
    }

   
    public static double hitungSkorAkhir(int userId, int materiId) {
        int totalDijawab = hitungJumlahSoalDijawab(userId, materiId);
        if (totalDijawab == 0) return 0;

        int jumlahBenar = hitungJawabanBenar(userId, materiId);
        return (jumlahBenar * 100.0) / totalDijawab;
    }

    
    public static boolean sudahSelesaiKerjakanMateri(int userId, int materiId) {
        int totalSoal = SoalService.getSoalByMateri(materiId).size();
        int dijawab = hitungJumlahSoalDijawab(userId, materiId);
        return totalSoal > 0 && dijawab == totalSoal;
    }

    
    public static List<Nilai> getJawabanByMateri(int userId, int materiId) {
        List<Nilai> list = new ArrayList<>();
        String sql = """
            SELECT n.* FROM nilai n
            JOIN soal s ON n.soal_id = s.id
            WHERE n.user_id = ? AND s.materi_id = ?
        """;

        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            stmt.setInt(2, materiId);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Nilai nilai = new Nilai(
                        rs.getInt("id"),
                        rs.getInt("user_id"),
                        rs.getInt("soal_id"),
                        rs.getDouble("score"),
                        rs.getTimestamp("answered_at").toLocalDateTime()
                );
                list.add(nilai);
            }

        } catch (SQLException e) {
            System.err.println("❌ Gagal mengambil data nilai: " + e.getMessage());
        }

        return list;
    }

    
    public static int hitungJumlahMateriDiselesaikan(int userId) {
        String sql = """
            SELECT COUNT(DISTINCT s.materi_id) FROM nilai n
            JOIN soal s ON n.soal_id = s.id
            WHERE n.user_id = ?
        """;

        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            return rs.next() ? rs.getInt(1) : 0;

        } catch (SQLException e) {
            System.err.println("❌ Gagal menghitung materi diselesaikan: " + e.getMessage());
            return 0;
        }
    }

    
    public static int hitungJumlahSoalDikerjakan(int userId) {
        String sql = "SELECT COUNT(*) FROM nilai WHERE user_id = ?";

        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            return rs.next() ? rs.getInt(1) : 0;

        } catch (SQLException e) {
            System.err.println("❌ Gagal menghitung soal dikerjakan: " + e.getMessage());
            return 0;
        }
    }
     public static List<Pair<String, Double>> getSkorSiswaByMateri(int materiId) {
        List<Pair<String, Double>> list = new ArrayList<>();

        String sql = """
            SELECT u.username,
                   SUM(n.score) AS total_score,
                   COUNT(s.id) AS total_soal
            FROM nilai n
            JOIN soal s ON n.soal_id = s.id
            JOIN users u ON n.user_id = u.id
            WHERE s.materi_id = ?
            GROUP BY u.username
        """;

        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, materiId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String username = rs.getString("username");
                int totalScore = rs.getInt("total_score");
                int totalSoal = rs.getInt("total_soal");

                double nilai = totalSoal == 0 ? 0 : (totalScore * 100.0) / totalSoal;
                list.add(new Pair<>(username, nilai));
            }

        } catch (SQLException e) {
            System.err.println("❌ Gagal mengambil skor siswa: " + e.getMessage());
        }

        return list;
    }
    
    public static List<Nilai> getNilaiByMateri(int materiId) {
    List<Nilai> list = new ArrayList<>();
    String sql = """
        SELECT n.* FROM nilai n
        JOIN soal s ON n.soal_id = s.id
        WHERE s.materi_id = ?
    """;

    try (Connection conn = DatabaseService.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setInt(1, materiId);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            Nilai nilai = new Nilai(
                    rs.getInt("id"),
                    rs.getInt("user_id"),
                    rs.getInt("soal_id"),
                    rs.getDouble("score"),
                    rs.getTimestamp("answered_at").toLocalDateTime()
            );
            list.add(nilai);
        }

    } catch (SQLException e) {
        System.err.println("❌ Gagal mengambil nilai by materi: " + e.getMessage());
    }

        return list;

        }
       }
  



