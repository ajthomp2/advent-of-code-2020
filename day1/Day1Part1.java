import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day1Part1 {

    private static final int TARGET = 2020;

	public static void main(String[] args) {
        Path path = Paths.get("day1.txt");
        try (Stream<String> lines = Files.lines(path)){
            List<Integer> expenses = lines
                .map(Integer::parseInt)
                .sorted()
                .collect(Collectors.toList());

            for (int i = 0, j = expenses.size() - 1; i < j;) {
                int smaller = expenses.get(i);
                int larger = expenses.get(j);
                if (smaller + larger == TARGET) {
                    System.out.println(smaller * larger);
                    System.exit(0);
                    return;
                } else if (smaller + larger > TARGET) {
                    j--;
                } else {
                    i++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
