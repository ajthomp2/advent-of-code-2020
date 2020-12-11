import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day6Part1 {

    private static final String DATA_FILE = "day6.txt";
    
    public static void main(String[] args) {
        Path path = Paths.get(DATA_FILE);
        try (Stream<String> lines = Files.lines(path)) {
            List<String> ls = lines.collect(Collectors.toList());
            int total = 0, m = 0, count = 0;
            for (String line: ls) {
                if (line.isEmpty()) {
                    total += count;
                    m = count = 0;
                }
                for (int c : line.toCharArray()) {
                    int i = 1 << (c - 97);
                    if ((m & i) == 0) {
                        count++;
                        m |= i;
                    }
                }
            }

            // add last count
            total += count;
            System.out.println(total);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
