import java.io.*;
import java.util.*;

class Book implements Serializable {
    private final int id;
    private final String title;
    private final String author;
    private boolean isAvailable;

    public Book(int id, String title, String author) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.isAvailable = true;
    }

    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public boolean isAvailable() { return isAvailable; }

    public void borrowBook() { this.isAvailable = false; }
    public void returnBook() { this.isAvailable = true; }

    @Override
    public String toString() {
        return "Book ID: " + id + ", Title: " + title + ", Author: " + author + ", Available: " + isAvailable;
    }
}

class User implements Serializable {
    private final int userId;
    private final String name;

    public User(int userId, String name) {
        this.userId = userId;
        this.name = name;
    }

    public int getUserId() { return userId; }
    public String getName() { return name; }

    @Override
    public String toString() {
        return "User ID: " + userId + ", Name: " + name;
    }
}

class Library {
    private List<Book> books;
    private final List<User> users;

    public Library() {
        this.books = new ArrayList<>();
        this.users = new ArrayList<>();
        loadBooks(); 
    }

    public void addBook(int id, String title, String author) {
        books.add(new Book(id, title, author));
        saveBooks(); 
        System.out.println("Book added successfully!");
    }

    public void displayBooks() {
        for (Book book : books) {
            System.out.println(book);
        }
    }

    public void searchBook(String title) {
        for (Book book : books) {
            if (book.getTitle().equalsIgnoreCase(title)) {
                System.out.println(book);
                return;
            }
        }
        System.out.println("Book not found!");
    }

    public void borrowBook(int bookId, String userId) {
        for (Book book : books) {
            if (book.getId() == bookId && book.isAvailable()) {
                book.borrowBook();
                saveBooks();
                System.out.println("Book borrowed successfully by user: " + userId);
                return;
            }
        }
        System.out.println("Book is not available or invalid book ID.");
    }
    
    public void returnBook(int bookId) {
        for (Book book : books) {
            if (book.getId() == bookId && !book.isAvailable()) {
                book.returnBook();
                saveBooks();
                System.out.println("Book returned successfully!");
                return;
            }
        }
        System.out.println("Invalid book ID or book is not borrowed.");
    }

    public void addUser(int userId, String name) {
        users.add(new User(userId, name));
        System.out.println("User added successfully!");
    }

    private void saveBooks() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("books.dat"))) {
            oos.writeObject(books);
        } catch (IOException e) {
            System.out.println("Error saving book data.");
        }
    }

    @SuppressWarnings("unchecked")
    private void loadBooks() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("books.dat"))) {
            Object obj = ois.readObject();
            if (obj instanceof List<?>) {
                books = (List<Book>) obj;
            } else {
                books = new ArrayList<>();
            }
        } catch (IOException | ClassNotFoundException e) {
            books = new ArrayList<>();
        }
    }
}

public class Main {
    public static void main(String[] args) {
        Library library = new Library();
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\nLibrary Management System");
            System.out.println("1. Add Book");
            System.out.println("2. View Books");
            System.out.println("3. Search Book");
            System.out.println("4. Borrow Book");
            System.out.println("5. Return Book");
            System.out.println("6. Add User");
            System.out.println("7. Exit");
            System.out.print("Choose an option: ");

            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1 -> {
                    System.out.print("Enter book ID: ");
                    int id = sc.nextInt();
                    sc.nextLine(); 
                    System.out.print("Enter book title: ");
                    String title = sc.nextLine();
                    System.out.print("Enter book author: ");
                    String author = sc.nextLine();
                    library.addBook(id, title, author);
                }
                case 2 -> library.displayBooks();
                case 3 -> {
                    System.out.print("Enter book title: ");
                    String searchTitle = sc.nextLine();
                    library.searchBook(searchTitle);
                }
                case 4 -> {
                    System.out.print("Enter book ID: ");
                    int bookId = sc.nextInt();  
                    sc.nextLine();  
                
                    System.out.print("Enter user ID: ");
                    String userId = sc.nextLine(); 
                
                    library.borrowBook(bookId, userId);  
                }                
                case 5 -> {
                    System.out.print("Enter book ID: ");
                    int returnBookId = sc.nextInt();
                    library.returnBook(returnBookId);
                }
                case 6 -> {
                    System.out.print("Enter user ID: ");
                    int userId = sc.nextInt();
                    sc.nextLine(); 
                    System.out.print("Enter user name: ");
                    String userName = sc.nextLine();
                    library.addUser(userId, userName);
                }
                case 7 -> {
                    System.out.println("Exiting...");
                    sc.close();
                    System.exit(0);
                }
                default -> System.out.println("Invalid choice. Try again.");
            }
        }
    }
}