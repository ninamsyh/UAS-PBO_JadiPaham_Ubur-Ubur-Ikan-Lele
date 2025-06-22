package Model;

public class Siswa {
    private int id;
    private String username;
    private String password;
    private String role;
    private String profilePicture;

    public Siswa(int id, String username, String password, String role, String profilePicture) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
        this.profilePicture = profilePicture;
    }

    
    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    // Optional: toString() if needed for debugging
    @Override
    public String toString() {
        return "Siswa{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", role='" + role + '\'' +
                ", profilePicture='" + profilePicture + '\'' +
                '}';
    }
}