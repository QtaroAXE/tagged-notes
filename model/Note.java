package model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Note implements Serializable {
    private static final long serialVersionUID = 1L;
    private static int nextId = 0;
    
    private int id;
    private String title;
    private String text;
    private LocalDateTime createdAt;
    private List<String> tags;
    
    public Note(String title, String text, List<String> tags) {
        this.id = nextId++;
        this.title = title;
        this.text = text;
        this.createdAt = LocalDateTime.now();
        this.tags = new ArrayList<>(tags);
    }
    
    public static void setNextId(int id) {
        nextId = id;
    }
    
    public static int getNextId() {
        return nextId;
    }
    
    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getText() { return text; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public List<String> getTags() { return tags; }
    
    public void setTitle(String title) { this.title = title; }
    public void setText(String text) { this.text = text; }
    public void setTags(List<String> tags) { this.tags = new ArrayList<>(tags); }
    
    public String getFormattedDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        return createdAt.format(formatter);
    }
    
    @Override
    public String toString() {
        return String.format("[%d] %s (создано: %s)", id, title, getFormattedDate());
    }
    
    public String toExportString() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== ЗАМЕТКА ===\n");
        sb.append("ID: ").append(id).append("\n");
        sb.append("Заголовок: ").append(title).append("\n");
        sb.append("Текст: ").append(text).append("\n");
        sb.append("Дата: ").append(getFormattedDate()).append("\n");
        sb.append("Теги: ").append(String.join(", ", tags)).append("\n");
        sb.append("================\n");
        return sb.toString();
    }
}