import java.util.Scanner;

public class ChatFilter {

    public static void main(String[] args) {

        String[] logs = {
            "10:05 alice Hello there",
            "10:06 bob How are you?",
            "10:07 Charlie Good morning",
            "malformed line"
        };

        Scanner input = new Scanner(System.in);

        System.out.print("Enter keyword: ");
        String keyword = input.nextLine().toLowerCase();

        StringBuilder report = new StringBuilder();
        int matches = 0;

        for (String line : logs) {

            String[] parts = line.split(" ", 3);

            if (parts.length < 3) {
                continue;
            }

            String time = parts[0];
            String user = parts[1];
            String message = parts[2];

            if (message.toLowerCase().contains(keyword)) {
                matches++;
                report.append(time)
                      .append(" ")
                      .append(user)
                      .append(": ")
                      .append(message)
                      .append("\n");
            }
        }

        System.out.println("Matches: " + matches);
        System.out.println(report);

        input.close();
    }
}