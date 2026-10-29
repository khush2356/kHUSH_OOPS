
import java.util.regex.Pattern;

class Validator {

    private static final Pattern MOBILE
            = Pattern.compile("[6-9][0-9]{9}");

    private static final Pattern EMAIL
            = Pattern.compile("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}");

    private static final Pattern PAN
            = Pattern.compile("[A-Z]{5}[0-9]{4}[A-Z]");

    private static final Pattern IFSC
            = Pattern.compile("[A-Z]{4}0[A-Z0-9]{6}");

    public static boolean isValidMobile(String value) {
        return value != null && MOBILE.matcher(value).matches();
    }

    public static boolean isValidEmail(String value) {
        return value != null && EMAIL.matcher(value).matches();
    }

    public static boolean isValidPan(String value) {
        return value != null && PAN.matcher(value).matches();
    }

    public static boolean isValidIfsc(String value) {
        return value != null && IFSC.matcher(value).matches();
    }

    public static boolean isValidAmount(String value) {
        return value != null && value.matches("[1-9][0-9]*");
    }
}

enum TransactionType {
    DEPOSIT, WITHDRAW, TRANSFER
}

record Command(TransactionType type, String accountNumber, long amount) {}

class CommandParser {

    public static Command parse(String line) {
        if (line == null) {
            throw new IllegalArgumentException("Input cannot be null");
        }

        String[] parts = line.trim().split("\\s+");

        if (parts.length != 3) {
            throw new IllegalArgumentException(
                    "Enter command as: TYPE ACCOUNT_NUMBER AMOUNT");
        }

        try {
            TransactionType type
                    = TransactionType.valueOf(parts[0].toUpperCase());

            if (!parts[1].matches("AC[0-9]{4}")) {
                throw new IllegalArgumentException(
                        "Invalid account number");
            }

            if (!Validator.isValidAmount(parts[2])) {
                throw new IllegalArgumentException(
                        "Amount must be a positive whole number");
            }

            long amount = Long.parseLong(parts[2]);

            return new Command(type, parts[1], amount);

        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Amount is too large");
        }
    }
}

class Account {

    private final String accountNumber;
    private String ownerName;
    private long balance;
    private boolean active;

    public Account(String accountNumber, String ownerName, long balance) {
        this.accountNumber = accountNumber;
        this.ownerName = ownerName;
        this.balance = balance;
        this.active = true;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public long getBalance() {
        return balance;
    }

    public boolean isActive() {
        return active;
    }
}

class StatementFormatter {

    public static String buildStatement(Account account) {
        StringBuilder sb = new StringBuilder();

        sb.append("===== ACCOUNT STATEMENT =====\n");
        sb.append("Account Number: ")
                .append(account.getAccountNumber()).append("\n");
        sb.append("Owner Name: ")
                .append(account.getOwnerName()).append("\n");
        sb.append("Balance: Rs. ")
                .append(account.getBalance()).append("\n");
        sb.append("Active: ")
                .append(account.isActive()).append("\n");
        sb.append("=============================");

        return sb.toString();
    }
}

public class MiniBank {

    public static void main(String[] args) {

        System.out.println("MOBILE VALIDATION");
        System.out.println(Validator.isValidMobile("9876543210"));
        System.out.println(Validator.isValidMobile("12345"));

        System.out.println("\nEMAIL VALIDATION");
        System.out.println(Validator.isValidEmail("abc@xyz.com"));
        System.out.println(Validator.isValidEmail("abcxyz.com"));

        System.out.println("\nPAN VALIDATION");
        System.out.println(Validator.isValidPan("ABCDE1234F"));
        System.out.println(Validator.isValidPan("12345"));

        System.out.println("\nIFSC VALIDATION");
        System.out.println(Validator.isValidIfsc("SBIN0001234"));
        System.out.println(Validator.isValidIfsc("12345"));

        System.out.println("\nCOMMAND PARSING");
        Command command
                = CommandParser.parse("DEPOSIT AC0001 500");

        System.out.println("Type: " + command.type());
        System.out.println("Account Number: "
                + command.accountNumber());
        System.out.println("Amount: " + command.amount());

        Account account = new Account("AC0001", "Riya", 5000);

        System.out.println("\n"
                + StatementFormatter.buildStatement(account));
    }
}
