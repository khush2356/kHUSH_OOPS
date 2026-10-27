
import java.lang.annotation.*;
import java.lang.reflect.*;
import java.util.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@interface Id {
}

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@interface Positive {
    String message() default "must be > 0";
}

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@interface MaxLength {
    int value();
}

@FunctionalInterface
interface WithdrawRule {
    boolean canWithdraw(double balance, double amount);
}

class Account {
    @Id
    private long accountNumber;

    @Positive
    private double balance;

    @MaxLength(10)
    private String owner;

    Account(long accountNumber, double balance, String owner) {
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.owner = owner;
    }

    @Override
    public String toString() {
        return "Account{accountNumber=" + accountNumber
                + ", balance=" + balance
                + ", owner='" + owner + "'}";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Account)) {
            return false;
        }
        Account other = (Account) obj;
        return accountNumber == other.accountNumber;
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountNumber);
    }
}

class AnnotationValidator {
    public static String[] validate(Object obj) {
        List<String> errors = new ArrayList<>();

        if (obj == null) {
            return new String[]{"Object must not be null"};
        }

        for (Field field : obj.getClass().getDeclaredFields()) {
            field.setAccessible(true);

            try {
                Object value = field.get(obj);

                if (field.isAnnotationPresent(Positive.class)) {
                    Positive annotation = field.getAnnotation(Positive.class);

                    if (!(value instanceof Number)
                            || ((Number) value).doubleValue() <= 0) {
                        errors.add(field.getName() + ": " + annotation.message());
                    }
                }

                if (field.isAnnotationPresent(MaxLength.class)) {
                    MaxLength annotation = field.getAnnotation(MaxLength.class);

                    if (value instanceof String
                            && ((String) value).length() > annotation.value()) {
                        errors.add(field.getName() + ": maximum length is "
                                + annotation.value());
                    }
                }
            } catch (IllegalAccessException e) {
                errors.add(field.getName() + ": unable to validate field");
            }
        }

        return errors.toArray(new String[0]);
    }
}

public class MiniBank {
    public static void main(String[] args) {
        Account invalidAccount = new Account(1001, -100, "Rahul");
        Account validAccount = new Account(1002, 500, "Amit");
        Account longNameAccount = new Account(1003, 200, "Christopher");

        System.out.println("Invalid Account:");
        System.out.println(invalidAccount);

        for (String error : AnnotationValidator.validate(invalidAccount)) {
            System.out.println(error);
        }

        System.out.println("\nValid Account:");
        System.out.println(validAccount);

        String[] validErrors = AnnotationValidator.validate(validAccount);
        if (validErrors.length == 0) {
            System.out.println("No validation errors.");
        } else {
            for (String error : validErrors) {
                System.out.println(error);
            }
        }

        System.out.println("\nLong Name Account:");
        for (String error : AnnotationValidator.validate(longNameAccount)) {
            System.out.println(error);
        }

        WithdrawRule rule = (balance, amount) ->
                amount > 0 && amount <= balance;

        System.out.println("\nCan withdraw 200 from balance 500: "
                + rule.canWithdraw(500, 200));
    }
}
