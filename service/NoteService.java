package service;

import model.Note;
import storage.NoteStorage;
import java.util.*;
import java.util.stream.Collectors;

public class NoteService {
    private List<Note> notes;
    private NoteStorage storage;
    
    public NoteService() {
        this.storage = new NoteStorage();
        this.notes = storage.loadNotes();
    }
    
    private void autoSave() {
        storage.saveNotes(notes);
    }
    
    public void createNote(String title, String text, String tagsLine) {
        List<String> tags = new ArrayList<>();
        if (tagsLine != null && !tagsLine.trim().isEmpty()) {
            tags = Arrays.stream(tagsLine.split(","))
                         .map(String::trim)
                         .filter(t -> !t.isEmpty())
                         .collect(Collectors.toList());
        }
        
        Note note = new Note(title, text, tags);
        notes.add(note);
        autoSave();
        System.out.println("✓ Заметка создана! ID: " + note.getId());
    }
    
    public void editNote(int id, String newTitle, String newText, String newTagsLine) {
        Note note = findNoteById(id);
        if (note == null) {
            System.out.println("Заметка с ID " + id + " не найдена");
            return;
        }
        
        if (newTitle != null && !newTitle.trim().isEmpty()) {
            note.setTitle(newTitle);
        }
        if (newText != null && !newText.trim().isEmpty()) {
            note.setText(newText);
        }
        if (newTagsLine != null && !newTagsLine.trim().isEmpty()) {
            List<String> tags = Arrays.stream(newTagsLine.split(","))
                                      .map(String::trim)
                                      .filter(t -> !t.isEmpty())
                                      .collect(Collectors.toList());
            note.setTags(tags);
        }
        
        autoSave();
        System.out.println("✓ Заметка " + id + " отредактирована");
    }
    
    public void deleteNote(int id) {
        Note note = findNoteById(id);
        if (note == null) {
            System.out.println("Заметка с ID " + id + " не найдена");
            return;
        }
        
        notes.remove(note);
        autoSave();
        System.out.println("✓ Заметка " + id + " удалена");
    }
    
    public void listAllNotes() {
        if (notes.isEmpty()) {
            System.out.println("Заметок пока нет");
            return;
        }
        
        System.out.println("\n=== ВСЕ ЗАМЕТКИ ===");
        for (Note note : notes) {
            System.out.println(note);
        }
        System.out.println("==================\n");
    }
    
    public void searchByText(String searchText) {
        if (searchText == null || searchText.trim().isEmpty()) {
            System.out.println("Введите текст для поиска");
            return;
        }
        
        String lowerSearch = searchText.toLowerCase();
        List<Note> results = notes.stream()
            .filter(n -> n.getTitle().toLowerCase().contains(lowerSearch) ||
                         n.getText().toLowerCase().contains(lowerSearch))
            .collect(Collectors.toList());
        
        if (results.isEmpty()) {
            System.out.println("Заметки, содержащие \"" + searchText + "\", не найдены");
        } else {
            System.out.println("\n=== РЕЗУЛЬТАТЫ ПОИСКА (\"" + searchText + "\") ===");
            results.forEach(System.out::println);
            System.out.println("==========================================\n");
        }
    }
    
    public void filterByTag(String tag) {
        if (tag == null || tag.trim().isEmpty()) {
            System.out.println("Введите тег для фильтрации");
            return;
        }
        
        List<Note> results = notes.stream()
            .filter(n -> n.getTags().stream().anyMatch(t -> t.equalsIgnoreCase(tag)))
            .collect(Collectors.toList());
        
        if (results.isEmpty()) {
            System.out.println("Заметки с тегом \"" + tag + "\" не найдены");
        } else {
            System.out.println("\n=== ЗАМЕТКИ С ТЕГОМ \"" + tag + "\" ===");
            results.forEach(System.out::println);
            System.out.println("==================================\n");
        }
    }
    
    public void showAllTags() {
        Set<String> allTags = new TreeSet<>();
        for (Note note : notes) {
            allTags.addAll(note.getTags());
        }
        
        if (allTags.isEmpty()) {
            System.out.println("Тегов пока нет");
        } else {
            System.out.println("\n=== ВСЕ ТЕГИ В СИСТЕМЕ ===");
            allTags.forEach(tag -> System.out.println("• " + tag));
            System.out.println("==========================\n");
        }
    }
    
    public void findNoteById(int id) {
        Note note = findNoteById(id);
        if (note == null) {
            System.out.println("Заметка с ID " + id + " не найдена");
            return;
        }
        
        System.out.println("\n=== ЗАМЕТКА ===");
        System.out.println("ID: " + note.getId());
        System.out.println("Заголовок: " + note.getTitle());
        System.out.println("Текст: " + note.getText());
        System.out.println("Дата: " + note.getFormattedDate());
        System.out.println("Теги: " + String.join(", ", note.getTags()));
        System.out.println("===============\n");
    }
    
    public void findByExactTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            System.out.println("Введите заголовок для поиска");
            return;
        }
        
        Note found = notes.stream()
            .filter(n -> n.getTitle().equalsIgnoreCase(title))
            .findFirst()
            .orElse(null);
        
        if (found == null) {
            System.out.println("Заметка с заголовком \"" + title + "\" не найдена");
        } else {
            findNoteById(found.getId());
        }
    }
    
    private Note findNoteById(int id) {
        return notes.stream().filter(n -> n.getId() == id).findFirst().orElse(null);
    }
    
    public void exportAll(String filename) {
        storage.exportToFile(notes, filename);
    }
    
    public void importFromFile(String filename) {
        List<Note> imported = storage.importFromFile(filename);
        if (!imported.isEmpty()) {
            notes.addAll(imported);
            autoSave();
        }
    }
}