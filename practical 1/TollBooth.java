import java.util.Scanner;

public class TollBooth {

    record Vehicle(String number, String type) { }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int totalToll = 0;
        int bikes = 0, cars = 0, trucks = 0;

        System.out.println("Enter vehicles. Type 'done' for the number to stop.");

        while (true) {
            System.out.print("Vehicle number (or 'done'): ");
            String number = sc.next();

            if (number.equalsIgnoreCase("done"))
                break;

            System.out.print("Type (bike/car/truck): ");
            String type = sc.next().toLowerCase();

            Vehicle v = new Vehicle(number, type);

            int toll = switch (v.type()) {
                case "bike" -> {
                    bikes++;
                    yield 20;
                }
                case "car" -> {
                    cars++;
                    yield 50;
                }
                case "truck" -> {
                    trucks++;
                    yield 150;
                }
                default -> 0;
            };

            totalToll += toll;
            System.out.println(v.number() + " (" + v.type() + ") pays " + toll);
        }

        String mostFrequent;

        if (bikes == 0 && cars == 0 && trucks == 0) {
            mostFrequent = "null";
        } else {
            int max = Math.max(bikes, Math.max(cars, trucks));

            if (bikes == max && cars == max && trucks == max)
                mostFrequent = "bike, car, truck";
            else if (bikes == max && cars == max)
                mostFrequent = "bike, car";
            else if (bikes == max && trucks == max)
                mostFrequent = "bike, truck";
            else if (cars == max && trucks == max)
                mostFrequent = "car, truck";
            else if (bikes == max)
                mostFrequent = "bike";
            else if (cars == max)
                mostFrequent = "car";
            else
                mostFrequent = "truck";
        }

        System.out.println("Most frequent: " + mostFrequent);
        sc.close();
    }
}