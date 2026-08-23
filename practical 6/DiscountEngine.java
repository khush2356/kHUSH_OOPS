import java.util.Scanner;

interface DiscountRule {
    double apply(double price);
}

public class DiscountEngine {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        double[] prices = {100, 200, 300};

        System.out.println("1: 10% Discount");
        System.out.println("2: 20% Discount");
        System.out.print("Choose discount: ");

        int choice = sc.nextInt();

        DiscountRule rule;

        if (choice == 1) {
            rule = price -> price - (price * 0.10);
        } else {
            rule = price -> price - (price * 0.20);
        }

        System.out.println("\nPrices after discount:");

        for (double price : prices) {
            System.out.println("Original: " + price
                    + " Final: " + rule.apply(price));
        }

        sc.close();
    }
}