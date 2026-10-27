
import java.lang.annotation.*;
import java.lang.reflect.*;
import java.util.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@interface NotBlank {
}

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@interface MaxLength {
    int value();
}

class SignupForm {
    @NotBlank
    @MaxLength(30)
    String username;

    @NotBlank
    @MaxLength(50)
    String email;

    @NotBlank
    @MaxLength(20)
    String password;

    SignupForm(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }
}

public class FormValidator {
    static List<String> validate(Object obj)
            throws IllegalAccessException {
        List<String> errors = new ArrayList<>();

        for (Field field : obj.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            Object value = field.get(obj);
            String text = value == null ? "" : value.toString();

            if (field.isAnnotationPresent(NotBlank.class)) {
                if (text.trim().isEmpty()) {
                    errors.add(field.getName() + " must not be blank");
                }
            }

            MaxLength max = field.getAnnotation(MaxLength.class);
            if (max != null && text.length() > max.value()) {
                errors.add(field.getName() + " exceeds maximum length of "
                        + max.value());
            }
        }

        return errors;
    }

    public static void main(String[] args) throws Exception {
        SignupForm form = new SignupForm("", "abc@example.com",
                "thispasswordiswaytoolong123");

        List<String> errors = validate(form);

        if (errors.isEmpty()) {
            System.out.println("Form is valid.");
        } else {
            for (String error : errors) {
                System.out.println(error);
            }
        }
    }
}
