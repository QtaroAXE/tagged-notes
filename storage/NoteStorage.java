package storage;

import model.Note;
import java.io.*;
import java.util.*;

public class NoteStorage {
    private static final String SAVE_FILE = "notes.dat";
    
    @SuppressWarnings("unchecked")
    public List<Note> loadNotes() {
        File file = new File(SAVE_FILE);
        if (!file.exists()) {
            return new ArrayList<>();
        }
        
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            List<Note> notes = (List<Note>) ois.readObject();
            if (!notes.isEmpty()) {
                int maxId = notes.stream().mapToInt(Note::getId).max().getAsInt();
                Note.setNextId(maxId + 1);
            }
            System.out.println("Данные восстановлены. Загружено " + notes.size() + " заметок.");
            return notes;
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Ошибка загрузки данных: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    public void saveNotes(List<Note> notes) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(SAVE_FILE))) {
            oos.writeObject(notes);
            System.out.println("✓ Автосохранение выполнено");
        } catch (IOException e) {
            System.out.println("Ошибка сохранения: " + e.getMessage());
        }
    }
    
    public void exportToFile(List<Note> notes, String filename) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            for (Note note : notes) {
                writer.write(note.toExportString());
            }
            System.out.println("✓ Экспортировано " + notes.size() + " заметок в файл " + filename);
        } catch (IOException e) {
            System.out.println("Ошибка экспорта: " + e.getMessage());
        }
    }
    
    public List<Note> importFromFile(String filename) {
        List<Note> importedNotes = new ArrayList<>();
        File file = new File(filename);
        
        if (!file.exists()) {
            System.out.println("Файл " + filename + " не найден");
            return importedNotes;
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            String title = null;
            String text = null;
            List<String> tags = null;
            
            while ((line = reader.readLine()) != null) {
                if (line.equals("=== ЗАМЕТКА ===")) {
                    title = null;
                    text = null;
                    tags = new ArrayList<>();
                } else if (line.startsWith("Заголовок: ")) {
                    title = line.substring(11);
                } else if (line.startsWith("Текст: ")) {
                    text = line.substring(7);
                } else if (line.startsWith("Теги: ")) {
                    String tagsStr = line.substring(6);
                    if (!tagsStr.isEmpty()) {
                        String[] tagArray = tagsStr.split(", ");
                        tags = new ArrayList<>(Arrays.asList(tagArray));
                    }
                } else if (line.equals("================") && title != null && text != null && tags != null) {
                    Note note = new Note(title, text, tags);
                    importedNotes.add(note);
                }
            }
            System.out.println("✓ Импортировано " + importedNotes.size() + " заметок из файла " + filename);
        } catch (IOException e) {
            System.out.println("Ошибка импорта: " + e.getMessage());
        }
        return importedNotes;
    }
}