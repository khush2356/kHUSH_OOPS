
import java.util.*;

public class Leaderboard {
    public static void main(String[] args) {
        Map<String, Integer> scores = new HashMap<>();

        scores.put("Rahul", 95);
        scores.put("Amit", 88);
        scores.put("Priya", 95);
        scores.put("Neha", 76);
        scores.put("Karan", 88);

        List<Map.Entry<String, Integer>> players =
            new ArrayList<>(scores.entrySet());

        players.sort(
            Comparator.comparing(Map.Entry<String, Integer>::getValue)
                .reversed()
                .thenComparing(Map.Entry::getKey)
        );

        int topN = 3;

        System.out.println("Top " + topN + " Players:");

        for (int i = 0; i < Math.min(topN, players.size()); i++) {
            Map.Entry<String, Integer> player = players.get(i);

            System.out.println(
                player.getKey() + " : " + player.getValue()
            );
        }
    }
}
