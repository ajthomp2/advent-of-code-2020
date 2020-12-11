import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day5Part2 {

    private static final String DATA_FILE = "day5.txt";
    
    public static void main(String[] args) {
        Path path = Paths.get(DATA_FILE);
        try (Stream<String> lines = Files.lines(path)) {
            Integer[] seatIds = lines
                .map(line -> Integer.parseInt(line.replaceAll("[BR]", "1")
                                              .replaceAll("[FL]", "0"), 2))
                .sorted()
                .toArray(Integer[]::new);
            int currSeatId = seatIds[1], prevSeatId = seatIds[0];
            for (int i = 2; i < seatIds.length; i++) {
                if (currSeatId != prevSeatId + 1) {
                    System.out.println(prevSeatId + 1);
                    break;
                }
                prevSeatId = currSeatId;
                currSeatId = seatIds[i];
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
