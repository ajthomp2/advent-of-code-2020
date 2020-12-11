import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class Day2Part2 {

    private static final String DATA_FILE = "day2.txt";
    private static final Pattern pattern = Pattern.compile("^(\\d+)-(\\d+) ([a-z]): ([a-z]+)$");

    public static void main(String[] args) {
        Path path = Paths.get(DATA_FILE);
        try (Stream<String> lines = Files.lines(path)) {
            long count = lines.filter(Day2Part2::passwordIsValid).count();
            System.out.println(count);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static boolean passwordIsValid(String line) {
        Matcher matcher = pattern.matcher(line);
        if (matcher.find()) {
            int ind1 = Integer.parseInt(matcher.group(1));
            int ind2 = Integer.parseInt(matcher.group(2));
            char c = matcher.group(3).charAt(0);
            String password = matcher.group(4);

            return (password.charAt(ind1 - 1) == c && password.charAt(ind2 - 1) != c)
                || (password.charAt(ind1 - 1) == c && password.charAt(ind2 - 1) == c);
        } else {
            throw new IllegalArgumentException("Invalid input format: " + line);
        }
    }
}
