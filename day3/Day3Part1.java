import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Day3Part1 {

    private static final String DATA_FILE = "day3.txt";

    public static void main(String[] args) {
        Path path = Paths.get(DATA_FILE);
        try (Stream<String> lines = Files.lines(path)) {
            List<String> ls = lines.collect(Collectors.toList());
            long count = IntStream.range(1, ls.size())
                .filter(i -> {
                    String l = ls.get(i);
                    return l.charAt((i * 3) % l.length()) == '#';
                })
                .count();
            System.out.println(count);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
