import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day4Part2 {
    
    private static final String DATA_FILE = "day4.txt";

    private static final String BIRTH_YEAR_KEY = "byr";
    private static final String ISSUE_YEAR_KEY = "iyr";
    private static final String EXPIRATION_YEAR_KEY = "eyr";
    private static final String HEIGHT_KEY = "hgt";
    private static final String HAIR_COLOR_KEY = "hcl";
    private static final String EYE_COLOR_KEY = "ecl";
    private static final String PASSPORT_ID_KEY = "pid";
    private static final String[] KEYS = {
        BIRTH_YEAR_KEY,
        ISSUE_YEAR_KEY,
        EXPIRATION_YEAR_KEY,
        HEIGHT_KEY,
        HAIR_COLOR_KEY,
        EYE_COLOR_KEY,
        PASSPORT_ID_KEY
    };

    private static final Pattern YEAR_PATTERN = Pattern.compile("^(\\d{4})$");
    private static final Pattern HEIGHT_CM_PATTERN = Pattern.compile("^(\\d{3})cm$");
    private static final Pattern HEIGHT_IN_PATTERN = Pattern.compile("^(\\d{2})in$");
    private static final Pattern HAIR_COLOR_PATTERN = Pattern.compile("^#[0-9a-f]{6}$");
    private static final Pattern EYE_COLOR_PATTERN = Pattern.compile("^(amb|blu|brn|gry|grn|hzl|oth)$");
    private static final Pattern PASSPORT_ID_PATTERN = Pattern.compile("^\\d{9}$");

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

        return isBirthYearValid(passport)
            && isIssueYearValid(passport)
            && isExpirationYearValid(passport)
            && isHeightValid(passport)
            && isHairColorValid(passport)
            && isEyeColorValid(passport)
            && isPassportIdValid(passport);
    }

    private static boolean isBirthYearValid(Map<String, String> passport) {
        String birthYear = passport.get(BIRTH_YEAR_KEY);
        if (birthYear == null) {
            return false;
        }
        Matcher birthYearMatcher = YEAR_PATTERN.matcher(birthYear);
        if (birthYearMatcher.find()) {
            int by = Integer.parseInt(birthYearMatcher.group(1));
            if (by < 1920 || by > 2002) {
                return false;
            }
        } else {
            return false;
        }

        return true;
    }

    private static boolean isIssueYearValid(Map<String, String> passport) {
        String issueYear = passport.get(ISSUE_YEAR_KEY);
        if (issueYear == null) {
            return false;
        }
        Matcher issueYearMatcher = YEAR_PATTERN.matcher(issueYear);
        if (issueYearMatcher.find()) {
            int iy = Integer.parseInt(issueYearMatcher.group(1));
            if (iy < 2010 || iy > 2020) {
                return false;
            }
        } else {
            return false;
        }

        return true;
    }

    private static boolean isExpirationYearValid(Map<String, String> passport) {
        String expirationYear = passport.get(EXPIRATION_YEAR_KEY);
        if (expirationYear == null) {
            return false;
        }
        Matcher expirationYearMatcher = YEAR_PATTERN.matcher(expirationYear);
        if (expirationYearMatcher.find()) {
            int ey = Integer.parseInt(expirationYearMatcher.group(1));
            if (ey < 2020 || ey > 2030) {
                return false;
            }
        } else {
            return false;
        }

        return true;
    }

    private static boolean isHeightValid(Map<String, String> passport) {
        String height = passport.get(HEIGHT_KEY);
        if (height == null) {
            return false;
        }
        Matcher heightCmMatcher = HEIGHT_CM_PATTERN.matcher(height);
        Matcher heightInMatcher = HEIGHT_IN_PATTERN.matcher(height);
        if (heightCmMatcher.find()) {
            int h = Integer.parseInt(heightCmMatcher.group(1));
            if (h < 150 || h > 193) {
                return false;
            }
        } else if (heightInMatcher.find()) {
            int h = Integer.parseInt(heightInMatcher.group(1));
            if (h < 59 || h > 76) {
                return false;
            }
        } else {
            return false;
        }

        return true;
    }

    private static boolean isHairColorValid(Map<String, String> passport) {
        String hairColor = passport.get(HAIR_COLOR_KEY);
        if (hairColor == null) {
            return false;
        }
        Matcher hairColorMatcher = HAIR_COLOR_PATTERN.matcher(hairColor);
        if (!hairColorMatcher.matches()) {
            return false;
        }

        return true;
    }

    private static boolean isEyeColorValid(Map<String, String> passport) {
        String eyeColor = passport.get(EYE_COLOR_KEY);
        if (eyeColor == null) {
            return false;
        }
        Matcher eyeColorMatcher = EYE_COLOR_PATTERN.matcher(eyeColor);
        if (!eyeColorMatcher.matches()) {
            return false;
        }
        
        return true;
    }

    private static boolean isPassportIdValid(Map<String, String> passport) {
        String passportId = passport.get(PASSPORT_ID_KEY);
        if (passportId == null) {
            return false;
        }
        Matcher passportIdMatcher = PASSPORT_ID_PATTERN.matcher(passportId);
        if (!passportIdMatcher.matches()) {
            return false;
        }

        return true;
    }
}
