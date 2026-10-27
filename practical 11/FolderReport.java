
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Instant;

public class FolderReport {
    public static void main(String[] args) {
        Path root = Paths.get("logs");
        Path report = Paths.get("folder-report.txt");

        try {
            Files.createDirectories(root);

            Path file1 = root.resolve("log1.txt");
            Path file2 = root.resolve("log2.txt");

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

            final int[] count = {0};

            Files.walkFileTree(root, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(
                        Path file, BasicFileAttributes attrs)
                        throws IOException {

                    String details =
                        "Name: " + file.getFileName()
                        + " | Size: " + attrs.size() + " bytes"
                        + " | Modified: "
                        + Instant.ofEpochMilli(
                            attrs.lastModifiedTime().toMillis());

                    System.out.println(details);
                    Files.write(report,
                        java.util.Arrays.asList(details),
                        StandardOpenOption.CREATE,
                        StandardOpenOption.APPEND);

                    count[0]++;
                    return FileVisitResult.CONTINUE;
                }
            });

            System.out.println("Report created: " + report);
            System.out.println("Files listed: " + count[0]);

        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
