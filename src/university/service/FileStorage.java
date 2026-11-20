package university.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

/**
 * SERVICE - Shared text-file storage used by every service.
 * One record per line, fields separated by commas, UTF-8 encoded.
 * Lines starting with '#' and blank lines are ignored.
 */
public final class FileStorage {

    private FileStorage() {
    }

    public static List<String> readLines(String filePath) {
        List<String> lines = new ArrayList<>();
        Path path = Paths.get(filePath);

        try {
            createParent(path);
            if (!Files.exists(path)) {
                Files.createFile(path);
                return lines;
            }

            try (BufferedReader reader =
                         Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
                String line;
                while ((line = reader.readLine()) != null) {
                    line = line.trim();
                    if (!line.isEmpty() && !line.startsWith("#")) {
                        lines.add(line);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return lines;
    }

    public static void writeLines(String filePath, List<String> lines) {
        Path path = Paths.get(filePath);

        try {
            createParent(path);
            try (BufferedWriter writer = Files.newBufferedWriter(
                    path,
                    StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING,
                    StandardOpenOption.WRITE)) {

                for (String line : lines) {
                    writer.write(line);
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void appendLine(String filePath, String line) {
        Path path = Paths.get(filePath);

        try {
            createParent(path);
            try (BufferedWriter writer = Files.newBufferedWriter(
                    path,
                    StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND)) {

                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String join(String... fields) {
        return String.join(",", fields);
    }

    private static void createParent(Path path) throws IOException {
        Path parent = path.getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }
    }
}
