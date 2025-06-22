package Service;

import Model.Admin;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AuthService {

    public static List<Admin> getAllSiswa() {
        List<Admin> list = new ArrayList<>();
        String sql = "SELECT id, username FROM users WHERE role = 'student'";

        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Admin user = new Admin(
                        rs.getInt("id"),
                        rs.getString("username")
                );
                list.add(user);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }
}