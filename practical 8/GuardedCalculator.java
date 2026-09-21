
class DivideByZeroException extends Exception {

    DivideByZeroException(String message) {
        super(message);
    }
}

public class GuardedCalculator {

    static void calculate(double a, double b, char op)
            throws DivideByZeroException {
        if (op == '/' && b == 0) {
            throw new DivideByZeroException("Cannot divide by zero!");
        }
        double result = 0;
        if (op == '+') {
            result = a + b;
        } else if (op == '-') {
            result = a - b;
        } else if (op == '*') {
            result = a * b;
        } else if (op == '/') {
            result = a / b;
        }
        System.out.println("Result = " + result);
    }

    public static void main(String[] args) {
        double a = 10;
        double b = 0;
        char op = '/';
        boolean success = false;
        while (!success) {
            try {
                calculate(a, b, op);
                success = true;
            } catch (DivideByZeroException e) {
                System.out.println(e.getMessage());
                b = 2;
            } finally {
                System.out.println("Calculation attempt logged.");
            }
        }
        System.out.println("Calculation successful.");
    }
}
