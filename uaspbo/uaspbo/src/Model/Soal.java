package Model;

import java.time.LocalDateTime;

public class Soal {
    private int id;
    private int materiId;
    private String question;
    private String optionA;
    private String optionB;
    private String optionC;
    private String optionD;
    private String correctOption;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Soal(int id, int materiId, String question, String optionA, String optionB, String optionC, String optionD,
                String correctOption, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.materiId = materiId;
        this.question = question;
        this.optionA = optionA;
        this.optionB = optionB;
        this.optionC = optionC;
        this.optionD = optionD;
        this.correctOption = correctOption;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    
    public int getId() { return id; }
    public int getMateriId() { return materiId; }
    public String getQuestion() { return question; }
    public String getOptionA() { return optionA; }
    public String getOptionB() { return optionB; }
    public String getOptionC() { return optionC; }
    public String getOptionD() { return optionD; }
    public String getCorrectOption() { return correctOption; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    
    public void setId(int id) { this.id = id; }
    public void setMateriId(int materiId) { this.materiId = materiId; }
    public void setQuestion(String question) { this.question = question; }
    public void setOptionA(String optionA) { this.optionA = optionA; }
    public void setOptionB(String optionB) { this.optionB = optionB; }
    public void setOptionC(String optionC) { this.optionC = optionC; }
    public void setOptionD(String optionD) { this.optionD = optionD; }
    public void setCorrectOption(String correctOption) { this.correctOption = correctOption; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}