public class Driver {

    public static boolean hasLength(String pw) {
        return pw.length() >= 8;
    }

    public static boolean hasUppercase(String pw) {
        return pw.matches(".*[A-Z].*");
    }

    public static boolean hasDigit(String pw) {
        return pw.matches(".*[0-9].*");
    }

    public static boolean hasSpecial(String pw) {
        return pw.matches(".*[^a-zA-Z0-9].*");
    }

    public static String strength(String pw) {
        int count = 0;

        if (hasLength(pw)) count++;
        if (hasUppercase(pw)) count++;
        if (hasDigit(pw)) count++;
        if (hasSpecial(pw)) count++;

        if (count <= 1)
            return "Weak";
        else if (count <= 3)
            return "Medium";
        else
            return "Strong";
    }

    public static void main(String[] args) {

        String[] passwords = {"abc", "abcdefg1", "Abcdefgh", "Abcd1234!"};

        for (String pw : passwords) {
            System.out.println("Password: " + pw);
            System.out.println("Length: " + hasLength(pw));
            System.out.println("Uppercase: " + hasUppercase(pw));
            System.out.println("Digit: " + hasDigit(pw));
            System.out.println("Special: " + hasSpecial(pw));
            System.out.println("Strength: " + strength(pw));
            System.out.println();
        }
    }
}