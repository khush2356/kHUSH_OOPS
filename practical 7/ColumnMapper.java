
import java.lang.annotation.*;
import java.lang.reflect.*;
import java.util.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@interface Column {
    String name();
}

class Student {
    @Column(name = "student_id")
    int id;

    @Column(name = "student_name")
    String name;

    @Column(name = "student_age")
    int age;

    public String toString() {
        return "ID: " + id + ", Name: " + name + ", Age: " + age;
    }
}

public class ColumnMapper {
    static Student populate(String[] headers, String[] data)
            throws Exception {
        if (headers.length != data.length) {
            throw new IllegalArgumentException(
                    "Header and data column counts must match.");
        }

        Student student = new Student();
        Map<String, String> values = new HashMap<>();

        for (int i = 0; i < headers.length; i++) {
            values.put(headers[i].trim(), data[i].trim());
        }

        for (Field field : Student.class.getDeclaredFields()) {
            Column column = field.getAnnotation(Column.class);

            if (column == null) {
                continue;
            }

            String value = values.get(column.name());

            if (value == null) {
                throw new IllegalArgumentException(
                        "Missing column: " + column.name());
            }

            field.setAccessible(true);

            if (field.getType() == int.class) {
                field.setInt(student, Integer.parseInt(value));
            } else if (field.getType() == String.class) {
                field.set(student, value);
            } else {
                throw new IllegalArgumentException(
                        "Unsupported field type: " + field.getName());
            }
        }

        return student;
    }

    public static void main(String[] args) throws Exception {
        String[] headers = {
            "student_name", "student_age", "student_id"
        };

        String[] data = {
            "Rahul", "20", "101"
        };

        Student student = populate(headers, data);
        System.out.println(student);
    }
}
