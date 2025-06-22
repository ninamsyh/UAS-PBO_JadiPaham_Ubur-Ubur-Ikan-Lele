package Model;

public class Materi {
    private int id;
    private String title;
    private String description;
    private String filePath; 
    
    public Materi(int id, String title, String description, String filePath) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.filePath = filePath;
    }

   
    public Materi(int id, String title, String description) {
        this(id, title, description, null);
    }

    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getFilePath() { return filePath; } // 🔹 Getter baru
}
