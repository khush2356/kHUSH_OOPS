
import java.io.*;

class Student implements Serializable {
    int id;
    String name;
    transient String password;

    Student(int id, String name, String password) {
        this.id = id;
        this.name = name;
        this.password = password;
    }
}

public class SerializationDemo {
    public static void main(String[] args) {
        Student[] students = {
            new Student(1, "Rahul", "abc123"),
            new Student(2, "Amit", "xyz456")
        };

        try {
            ObjectOutputStream out = new ObjectOutputStream(
                new FileOutputStream("students.dat")
            );

            out.writeObject(students);
            out.close();

            System.out.println("Objects saved successfully.");

            ObjectInputStream in = new ObjectInputStream(
                new FileInputStream("students.dat")
            );

            Student[] loaded = (Student[]) in.readObject();
            in.close();

            for (Student s : loaded) {
                System.out.println("ID: " + s.id);
                System.out.println("Name: " + s.name);
                System.out.println("Password: " + s.password);
                System.out.println();
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }
}
