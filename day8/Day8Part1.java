import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day8Part1 {
    
    private static final String DATA_FILE = "day8.txt";

    private static final Pattern INSTRUCTION_PATTERN = Pattern.compile("^(nop|acc|jmp) ([+-]\\d+)$");

    public static void main(String[] args) {
        Instant start = Instant.now();
        int output = 0;
        for(int i = 0; i < 100; i++) {
            Instant s = Instant.now();
            Path path = Paths.get(DATA_FILE);
            try (Stream<String> lines = Files.lines(path)) {
                List<String> l = lines.collect(Collectors.toList());
                int acc = 0;
                Map<Integer, String> seen = new HashMap<>();
                for (int j = 0; j < l.size(); j++) {
                    if (seen.containsKey(j)) {
                        break;
                    }
                    Matcher m = INSTRUCTION_PATTERN.matcher(l.get(j));
                    if (m.find()) {
                        seen.put(j, m.group(1));
                        if (m.group(1).equals("nop")) {
                            continue;
                        } else if (m.group(1).equals("acc")) {
                            acc += Integer.parseInt(m.group(2));
                        } else {
                            // -1 because i will be incremented again from for loop
                            j += Integer.parseInt(m.group(2)) - 1;
                        }
                    } else {
                        throw new IllegalArgumentException();
                    }
                }
                output = acc;
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(1);
            }
        }
        System.out.println("Output: " + output);
        System.out.println("100 cycles avg Duration: " +
                           Duration.between(start, Instant.now()).toMillis() / 100.0 + " ms");
    }
}
