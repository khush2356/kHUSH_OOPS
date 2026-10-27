
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.stream.Stream;

public class LogAnalyzer {
    public static void main(String[] args) {
        Path folder = Paths.get("logs");
        String keyword = "ERROR";

        try {
            Files.createDirectories(folder);

            Path file1 = folder.resolve("log1.txt");
            Path file2 = folder.resolve("log2.txt");

            if (!Files.exists(file1)) {
                Files.write(file1, java.util.Arrays.asList(
                    "INFO Server started",
                    "ERROR Connection failed",
                    "INFO User logged in"
                ));
            }

            if (!Files.exists(file2)) {
                Files.write(file2, java.util.Arrays.asList(
                    "INFO Request received",
                    "ERROR File not found",
                    "WARNING Low memory"
                ));
            }

            int totalLines = 0;
            int totalMatches = 0;

            try (Stream<Path> files = Files.list(folder)) {
                for (Path file : (Iterable<Path>)
                        files.filter(Files::isRegularFile)::iterator) {

                    BasicFileAttributes attr =
                        Files.readAttributes(file,
                            BasicFileAttributes.class);

                    int linesCount = 0;
                    int matches = 0;

                    for (String line : Files.readAllLines(file)) {
                        linesCount++;
                        if (line.contains(keyword)) {
                            matches++;
                        }
                    }

                    totalLines += linesCount;
                    totalMatches += matches;

                    System.out.println("File: " + file.getFileName());
                    System.out.println("Size: " + attr.size() + " bytes");
                    System.out.println("Lines: " + linesCount);
                    System.out.println("Keyword matches: " + matches);
                    System.out.println();
                }
            }

            System.out.println("Total lines: " + totalLines);
            System.out.println("Total ERROR matches: " + totalMatches);

        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
