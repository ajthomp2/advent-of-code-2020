import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day4Part1 {

    private static final String DATA_FILE = "day4.txt";
    private static final String[] KEYS = { "byr", "iyr", "eyr", "hgt", "hcl", "ecl", "pid" };

    public static void main(String[] args) {
        Path path = Paths.get(DATA_FILE);
        try (Stream<String> lines = Files.lines(path)) {
            List<String> ls = lines.collect(Collectors.toList());
            int count = 0;
            Map<String, String> fs = new HashMap<>();
            for (String l: ls) {
                if (l.isEmpty()) {
                    if (isPassportValid(fs)) {
                        count++;
                    }
                    fs.clear();
                    continue;
                }

                String[] fields = l.split(" ");
                for (String f : fields) {
                    String[] kv = f.split(":");
                    fs.put(kv[0], kv[1]);
                }
            }

            // once more to check last passport
            if (isPassportValid(fs)) {
                count++;
            }

            System.out.println(count);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static boolean isPassportValid(Map<String, String> passport) {
        if (passport.size() < KEYS.length) {
            return false;
        }
        for (String key : KEYS) {
            if (!passport.containsKey(key)) {
                return false;
            }
        }
        return true;
    }
}
