import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day3Part2 {

    private static final String DATA_FILE = "day3.txt";

    public static void main(String[] args) {
        Path path = Paths.get(DATA_FILE);
        try (Stream<String> lines = Files.lines(path)) {
            List<String> ls = lines.collect(Collectors.toList());
            long a = 0, b = 0, c = 0, d = 0, e = 0;
            for (int i = 1; i < ls.size(); i++) {
                String l = ls.get(i);
                a += l.charAt(i % l.length()) == '#' ? 1 : 0;
                b += l.charAt((i * 3) % l.length()) == '#' ? 1 : 0;
                c += l.charAt((i * 5) % l.length()) == '#' ? 1 : 0;
                d += l.charAt((i * 7) % l.length()) == '#' ? 1 : 0;
                e += i > 1 && i % 2 == 0 ? l.charAt(i/2 % l.length()) == '#' ? 1 : 0 : 0;
            }
            System.out.println(a * b * c * d * e);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
