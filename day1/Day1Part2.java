import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day1Part2 {

    private static final int TARGET = 2020;

    public static void main(String[] args) {
        Path path = Paths.get("day1.txt");
        try (Stream<String> lines = Files.lines(path)){
            List<Integer> expenses = lines
                .map(Integer::parseInt)
                .sorted()
                .collect(Collectors.toList());

            for (int i = 0, j = i + 1, k = expenses.size() - 1; ; ) {
                int smallest = expenses.get(i);
                int middle = expenses.get(j);
                int largest = expenses.get(k);

                if (smallest + middle + largest == TARGET) {
                    System.out.println(smallest * middle * largest);
                    System.exit(0);
                    return;
                } else if (smallest + middle + largest > TARGET) {
                    k--;
                } else {
                    // if j can't be incremented any more without hitting k,
                    // then increment i
                    if (j == k - 1) {
                        i++;
                    } else {
                        j++;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
