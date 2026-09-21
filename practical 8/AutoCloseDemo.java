
class FileResource implements AutoCloseable {

    public void open() {
        System.out.println("Resource opened");
    }

    public void close() {
        System.out.println("Resource closed");
    }
}

public class AutoCloseDemo {

    public static void main(String[] args) {

        try (FileResource r = new FileResource()) {

            r.open();

            System.out.println("Doing some work...");

            // Exception occurs
            throw new Exception("Something went wrong!");

        } catch (Exception e) {

            System.out.println("Error: " + e.getMessage());
        }
    }
}
