package console_interface;

import service.NoteService;
import java.util.Scanner;

public class ConsoleInterface {
    private NoteService service;
    private Scanner scanner;
    
    public ConsoleInterface() {
        this.service = new NoteService();
        this.scanner = new Scanner(System.in);
    }
    
    public void start() {
        System.out.println("Добро пожаловать в Менеджер заметок!");
        
        while (true) {
            printMenu();
            String choice = scanner.nextLine();
            
            switch (choice) {
                case "1": createNote(); break;
                case "2": editNote(); break;
                case "3": deleteNote(); break;
                case "4": service.listAllNotes(); break;
                case "5": searchByText(); break;
                case "6": filterByTag(); break;
                case "7": service.showAllTags(); break;
                case "8": findById(); break;
                case "9": findByTitle(); break;
                case "10": exportNotes(); break;
                case "11": importNotes(); break;
                case "0":
                    System.out.println("До свидания!");
                    return;
                default:
                    System.out.println("Неверный выбор. Попробуйте снова.");
            }
        }
    }
    
    private void printMenu() {
        System.out.println("\n========== МЕНЮ ==========");
        System.out.println("1. Создать заметку");
        System.out.println("2. Редактировать заметку");
        System.out.println("3. Удалить заметку");
        System.out.println("4. Список всех заметок");
        System.out.println("5. Поиск по тексту");
        System.out.println("6. Фильтр по тегу");
        System.out.println("7. Показать все теги");
        System.out.println("8. Найти заметку по ID");
        System.out.println("9. Найти заметку по точному заголовку");
        System.out.println("10. Экспорт всех заметок в файл");
        System.out.println("11. Импорт заметок из файла");
        System.out.println("0. Выход");
        System.out.print("Ваш выбор: ");
    }
    
    private void createNote() {
        System.out.print("Заголовок: ");
        String title = scanner.nextLine();
        System.out.print("Текст: ");
        String text = scanner.nextLine();
        System.out.print("Теги (через запятую, например: работа,важное,идеи): ");
        String tags = scanner.nextLine();
        
        service.createNote(title, text, tags);
    }
    
    private void editNote() {
        System.out.print("ID заметки для редактирования: ");
        int id = Integer.parseInt(scanner.nextLine());
        System.out.print("Новый заголовок (оставьте пустым, чтобы не менять): ");
        String title = scanner.nextLine();
        System.out.print("Новый текст (оставьте пустым, чтобы не менять): ");
        String text = scanner.nextLine();
        System.out.print("Новые теги (оставьте пустым, чтобы не менять): ");
        String tags = scanner.nextLine();
        
        service.editNote(id, title.isEmpty() ? null : title,
                         text.isEmpty() ? null : text,
                         tags.isEmpty() ? null : tags);
    }
    
    private void deleteNote() {
        System.out.print("ID заметки для удаления: ");
        int id = Integer.parseInt(scanner.nextLine());
        service.deleteNote(id);
    }
    
    private void searchByText() {
        System.out.print("Введите текст для поиска: ");
        String text = scanner.nextLine();
        service.searchByText(text);
    }
    
    private void filterByTag() {
        System.out.print("Введите тег: ");
        String tag = scanner.nextLine();
        service.filterByTag(tag);
    }
    
    private void findById() {
        System.out.print("Введите ID: ");
        int id = Integer.parseInt(scanner.nextLine());
        service.findNoteById(id);
    }
    
    private void findByTitle() {
        System.out.print("Введите точный заголовок: ");
        String title = scanner.nextLine();
        service.findByExactTitle(title);
    }
    
    private void exportNotes() {
        System.out.print("Имя файла для экспорта (например: notes.txt): ");
        String filename = scanner.nextLine();
        service.exportAll(filename);
    }
    
    private void importNotes() {
        System.out.print("Имя файла для импорта: ");
        String filename = scanner.nextLine();
        service.importFromFile(filename);
    }
}