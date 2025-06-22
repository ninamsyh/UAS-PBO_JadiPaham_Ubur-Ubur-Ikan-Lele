package Model;

import java.time.LocalDateTime;

public class Nilai {
    private int id;
    private int userId;
    private int soalId;
    private double score;
    private LocalDateTime answeredAt;

    public Nilai(int id, int userId, int soalId, double score, LocalDateTime answeredAt) {
        this.id = id;
        this.userId = userId;
        this.soalId = soalId;
        this.score = score;
        this.answeredAt = answeredAt;
    }

    
    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public int getSoalId() {
        return soalId;
    }

    public double getScore() {
        return score;
    }

    public LocalDateTime getAnsweredAt() {
        return answeredAt;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setSoalId(int soalId) {
        this.soalId = soalId;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public void setAnsweredAt(LocalDateTime answeredAt) {
        this.answeredAt = answeredAt;
    }
}
