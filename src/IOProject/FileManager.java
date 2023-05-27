package IOProject;

import java.util.Date;
import java.util.Scanner;
import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;

public class FileManager {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter a directory path.");
        String userPath = scanner.nextLine();
        boolean stop = false;

        try {
            Path directoryPath = Paths.get(userPath);
            if (!Files.isDirectory(directoryPath)) {
                System.out.println("Invalid path. Please try again.");
                return;
            }
            //options
            while (!stop) {
                System.out.println("What would you like to do? (Input the number for your selected option)");
                System.out.println("1. Display contents");
                System.out.println("2. Copy file");
                System.out.println("3. Move file");
                System.out.println("4. Delete file");
                System.out.println("5. Create directory");
                System.out.println("6. Delete directory");
                System.out.println("7. Search for file");
                System.out.println("8. Exit");

                int selection = scanner.nextInt();
                scanner.nextLine(); //consumes /n

                switch (selection) {
                    case 1:
                        displayContent(userPath);
                        break;
                    case 2:
                        copyFile(userPath);
                        break;
                    case 3:
                        moveFile(userPath);
                        break;
                    case 4:
                        deleteFile(userPath);
                        break;
                    case 5:
                        createDirectory(userPath);
                        break;
                    case 6:
                        deleteDirectory(new File(userPath));
                        break;
                    case 7:
                        searchFile(userPath);
                        break;
                    case 8:
                        stop = true;
                        break;
                    default:
                        System.out.println("Invalid option. Please try again.");
                        break;
                }
            }
        } catch (InvalidPathException e) {
            System.out.println("Invalid path: " + e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void displayContent(String directoryPath) {
        try {
            DirectoryStream<Path> directoryStream = Files.newDirectoryStream(Paths.get(directoryPath));
            System.out.println("Content of " + directoryPath + ":");
            for (Path path : directoryStream) {
                BasicFileAttributes attributes = Files.readAttributes(path, BasicFileAttributes.class);
                String fileName = path.getFileName().toString();
                String size = String.valueOf(attributes.size());
                String lastModified = dateFormat.format(new Date(attributes.lastModifiedTime().toMillis()));
                System.out.println("Name: " + fileName + ", Size: " + size + ", Last Modified: " + lastModified);
            }
            directoryStream.close();
        } catch (IOException e) {
            System.out.println("A display error occurred: " + e.getMessage());
        }
    }

    private static void copyFile(String directoryPath) {
        System.out.print("Enter the file name you want to copy: ");
        Scanner scanner = new Scanner(System.in);
        String sourceFileName = scanner.nextLine();
        System.out.print("Enter a new name for the copy file: ");
        String destinationFileName = scanner.nextLine();

        Path sourcePath = Paths.get(directoryPath, sourceFileName);
        Path destinationPath = Paths.get(directoryPath, destinationFileName);

        try {
            Files.copy(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("File copied successfully.");
        } catch (IOException e) {
            System.out.println("Copy error: " + e.getMessage());
        }
    }

    private static void moveFile(String directoryPath) {
        System.out.print("Enter the file name you would like to move: ");
        Scanner scanner = new Scanner(System.in);
        String sourceFileName = scanner.nextLine();
        System.out.print("Enter the path you would like to move the file to: ");
        String destinationDirectoryPath = scanner.nextLine();

        Path sourcePath = Paths.get(directoryPath, sourceFileName);
        Path destinationPath = Paths.get(destinationDirectoryPath, sourceFileName);

        try {
            Files.move(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("File moved successfully.");
        } catch (IOException e) {
            System.out.println("Moving error: " + e.getMessage());
        }
    }

    private static void deleteFile(String directoryPath) {
        System.out.print("Enter the name of the file you would to delete: ");
        Scanner scanner = new Scanner(System.in);
        String fileName = scanner.nextLine();

        Path filePath = Paths.get(directoryPath, fileName);

        try {
            Files.delete(filePath);
            System.out.println("File deleted successfully.");
        } catch (IOException e) {
            System.out.println("Deletion error: " + e.getMessage());
        }
    }

    private static void createDirectory(String directoryPath) {
        System.out.print("Enter the name of the directory you would like to create: ");
        Scanner scanner = new Scanner(System.in);
        String directoryName = scanner.nextLine();

        Path directoryPathToCreate = Paths.get(directoryPath, directoryName);

        try {
            Files.createDirectory(directoryPathToCreate);
            System.out.println("Directory created successfully.");
        } catch (IOException e) {
            System.out.println("Directory creation error: " + e.getMessage());
        }
    }

    private static void deleteDirectory(File directory) {
        if (!directory.exists()) {
            return;
        }

        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    deleteDirectory(file); // Recursively delete nested directories
                } else {
                    file.delete(); // Delete files
                }
            }
        }

        directory.delete(); // Delete the directory itself
    }

    private static void searchFile(String directoryPath) throws IOException {
        System.out.println("Choose the search option:");
        System.out.println("1. Search by file name");
        System.out.println("2. Search by file extension");
        System.out.print("Enter your choice: ");
        Scanner scanner = new Scanner(System.in);
        int searchOption = scanner.nextInt();
        scanner.nextLine(); // consume the newline character

        System.out.print("Enter the search term: ");
        String searchTerm = scanner.nextLine();

        Path directoryPathObj = Paths.get(directoryPath);
        if (searchOption == 1) {
            searchFilesByName(directoryPathObj, searchTerm);
        } else if (searchOption == 2) {
            searchFilesByExtension(directoryPathObj, searchTerm);
        } else {
            System.out.println("Invalid search option.");
        }
    }

    //search by name
    private static void searchFilesByName(Path dir, String fileName) {
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir, fileName)) {
            System.out.println("Search by name results:");
            for (Path entry : stream) {
                System.out.println(entry.getFileName());
            }
        } catch (IOException e) {
            System.out.println("Unable to find file by name: " + e.getMessage());
        }
    }

    // Search by extension
    private static void searchFilesByExtension(Path dir, String fileExtension) {
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir, "*." + fileExtension)) {
            System.out.println("Search by extension results:");
            for (Path entry : stream) {
                System.out.println(entry.getFileName());
            }
        } catch (IOException e) {
            System.out.println("Unable to find file by extension: " + e.getMessage());
        }
    }
}