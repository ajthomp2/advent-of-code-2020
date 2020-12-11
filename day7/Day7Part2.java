import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day7Part2 {
    
    private static final String DATA_FILE = "day7.txt";
    private static final String TARGET = "shiny gold";

    public static void main(String[] args) {
        Instant start = Instant.now();
        int output = 0;
        for(int i = 0; i < 100; i++) {
            Instant s = Instant.now();
            Path path = Paths.get(DATA_FILE);
            try (Stream<String> lines = Files.lines(path)) {
                Map<String, Bag> bags = lines
                    .map(Bag::parse)
                    .collect(Collectors.toMap(Bag::getBagName, Function.identity()));
                Bag shineyGold = bags.values().stream()
                    .filter(b -> b.getBagName().equals(TARGET))
                    .findAny()
                    .get();
                output = countBags(bags, new HashMap<>(), shineyGold.getSubBags());
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(1);
            }
        }
        System.out.println("Output: " + output);
        System.out.println("100 cycles avg Duration: " + Duration.between(start, Instant.now()).toMillis() / 100 + " ms");
    }

    private static int countBags(Map<String, Bag> bags, Map<String, Integer> cache, List<BagNameAndCount> bagsToTraverse) {
        if (bagsToTraverse.isEmpty()) {
            return 0;
        }
        int total = 0, bagCount = 0;
        for(BagNameAndCount bc : bagsToTraverse) {
            if (cache.containsKey(bc.getName())) {
                bagCount = bc.getCount() + bc.getCount() * cache.get(bc.getName());
            } else {
                int countOfSubBags = countBags(bags, cache, bags.get(bc.getName()).getSubBags());
                cache.put(bc.getName(), countOfSubBags);
                bagCount = bc.getCount() + bc.getCount() * countOfSubBags;
            }
            
            total += bagCount;
            bagCount = 0;
        }

        return total;
    }

    private static class Bag {

        private static final String NO_BAGS = "no other bags";

        private static final Pattern linePattern = Pattern.compile("^(\\w+ \\w+) bags contain ([\\w ,]+)\\.$");
        private static final Pattern bagsPattern = Pattern.compile("^(\\d) (\\w+ \\w+) bag[s]*$");
        
        private final String bagName;
        private final List<BagNameAndCount> subBags;
        private boolean containsTargetBag;

        public Bag(String bagName, List<BagNameAndCount> subBags, boolean containsTargetBag) {
            this.bagName = bagName;
            this.subBags = subBags;
            this.containsTargetBag = containsTargetBag;
        }

        public static Bag parse(String line) {
            Matcher m = linePattern.matcher(line);
            if (m.find()) {
                String bagName = m.group(1);
                List<BagNameAndCount> sb = Arrays.stream(m.group(2).split(", "))
                    .filter(b -> !b.equals(NO_BAGS))
                    .map(b -> {
                        Matcher m2 = bagsPattern.matcher(b);
                        if (m2.find()) {
                            return new BagNameAndCount(m2.group(2), Integer.parseInt(m2.group(1)));
                        } else {
                            throw new IllegalArgumentException();
                        }
                    })
                    .collect(Collectors.toList());
                return new Bag(bagName, sb, sb.contains(TARGET));
            } else {
                System.out.println(line);
                throw new IllegalArgumentException();
            }
        }

        public String getBagName() {
            return bagName;
        }

        public List<BagNameAndCount> getSubBags() {
            return subBags;
        }

        public boolean getContainsTargetBag() {
            return containsTargetBag;
        }

        public void setContainsTargetBag(boolean containsTargetBag) {
            this.containsTargetBag = containsTargetBag;
        }
    }

    private static class BagNameAndCount {

        private final String name;
        private final int count;
        
        public BagNameAndCount(String name, int count) {
            this.name = name;
            this.count = count;
        }

        public String getName() {
            return name;
        }

        public int getCount() {
            return count;
        }
    }
}

