import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day7Part1 {
    
    private static final String DATA_FILE = "day7.txt";
    private static final String TARGET = "shiny gold";
    
    public static void main(String[] args) {
        Path path = Paths.get(DATA_FILE);
        try (Stream<String> lines = Files.lines(path)) {
            Map<String, Bag> bags = lines
                .map(Bag::parse)
                .collect(Collectors.toMap(Bag::getBagName, Function.identity()));
            int newAdditions = 0;
            do {
                newAdditions = 0;
                for (Map.Entry<String, Bag> e : bags.entrySet()) {
                    if (e.getValue().getContainsTargetBag()) {
                        continue;
                    }
                    for (String n : e.getValue().getSubBags()) {
                        Bag b = bags.get(n);
                        if (b != null && b.getContainsTargetBag()) {
                            e.getValue().setContainsTargetBag(true);
                            newAdditions++;
                        }
                    }
                }
            } while (newAdditions != 0);
            long count = bags.values().stream()
                .filter(b -> b.getContainsTargetBag())
                .collect(Collectors.counting());
            System.out.println(count);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static class Bag {

        private static final String NO_BAGS = "no other bags";

        private static final Pattern linePattern = Pattern.compile("^(\\w+ \\w+) bags contain ([\\w ,]+)\\.$");
        private static final Pattern bagsPattern = Pattern.compile("^\\d (\\w+ \\w+) bag[s]*$");
        
        private final String bagName;
        private final List<String> subBags;
        private boolean containsTargetBag;

        public Bag(String bagName, List<String> subBags, boolean containsTargetBag) {
            this.bagName = bagName;
            this.subBags = subBags;
            this.containsTargetBag = containsTargetBag;
        }

        public static Bag parse(String line) {
            Matcher m = linePattern.matcher(line);
            if (m.find()) {
                String bagName = m.group(1);
                List<String> sb = Arrays.stream(m.group(2).split(", "))
                    .filter(b -> !b.equals(NO_BAGS))
                    .map(b -> {
                        Matcher m2 = bagsPattern.matcher(b);
                        if (m2.find()) {
                            return m2.group(1);
                        } else {
                            throw new IllegalArgumentException();
                        }
                    })
                    .collect(Collectors.toList());
                return new Bag(bagName, sb, sb.contains(TARGET));
            } else {
                throw new IllegalArgumentException();
            }
        }

        public String getBagName() {
            return bagName;
        }

        public List<String> getSubBags() {
            return subBags;
        }

        public boolean getContainsTargetBag() {
            return containsTargetBag;
        }

        public void setContainsTargetBag(boolean containsTargetBag) {
            this.containsTargetBag = containsTargetBag;
        }
    }
}
