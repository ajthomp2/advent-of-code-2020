import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day6Part2 {
    
    private static final String DATA_FILE = "day6.txt";
    
    public static void main(String[] args) {
        Path path = Paths.get(DATA_FILE);
        try (Stream<String> lines = Files.lines(path)) {
            List<String> ls = lines.collect(Collectors.toList());
            // ~0 is a mask of all 1s
            int total = 0, m = ~0;
            for (String line: ls) {
                if (line.isEmpty()) {
                    total += determineCount(m);
                    m = ~0;
                    continue;
                }
                // determine which questions were answered for each line by ORing
                // each char and then AND each line to get only the questions that
                // everyone answered
                int lm = 0;
                for (int c : line.toCharArray()) {
                    lm |= (1 << (c - 97));
                }
                m &= lm;
            }

            // add last count
            total += determineCount(m);
            System.out.println(total);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static int determineCount(int m) {
        // this algorithm decreases the number of loops needed to count
        // the number of 1s from the number of bits in an int (32) to
        // just the number of 1s in the int
        int count = 0;
        while (m > 0) {
            m = m & (m - 1);
            count++;
        }
        return count;
    }
}
