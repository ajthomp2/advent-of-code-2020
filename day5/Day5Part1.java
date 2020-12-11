import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.stream.Stream;

public class Day5Part1 {

    private static final String DATA_FILE = "day5.txt";
    
    public static void main(String[] args) {
        Path path = Paths.get(DATA_FILE);
        try (Stream<String> lines = Files.lines(path)) {
            int maxSeatId = lines
                .map(line -> Integer.parseInt(line.replaceAll("[BR]", "1")
                                              .replaceAll("[FL]", "0"), 2))
                .max(Comparator.naturalOrder())
                .get();
            System.out.println(maxSeatId);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
