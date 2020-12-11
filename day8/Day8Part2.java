import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day8Part2 {

    private static final String DATA_FILE = "day8.txt";

    private static final Pattern INSTRUCTION_PATTERN = Pattern.compile("^(nop|acc|jmp) ([+-]\\d+)$");

    public static void main(String[] args) {
        Instant start = Instant.now();
        int output = 0;
        for(int i = 0; i < 100; i++) {
            Instant s = Instant.now();
            Path path = Paths.get(DATA_FILE);
            try (Stream<String> lines = Files.lines(path)) {
                List<Instruction> instructions = createInstructions(lines);
                instructions = markInstructionsInCycles(instructions);
                instructions = fixIncorrectInstruction(instructions);
                output = getOutput(instructions);
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(1);
            }
        }
        System.out.println("Output: " + output);
        System.out.println("100 cycles avg Duration: " +
                           Duration.between(start, Instant.now()).toMillis() / 100.0 + " ms");
    }

    private static List<Instruction> fixIncorrectInstruction(List<Instruction> instructions) {
        for (int i = 0; i < instructions.size(); i++) {
            Instruction instruction = instructions.get(i);
            if (instruction.isInInitialCycle()) {
                if (instruction.getOp().equals("jmp") &&
                    jmpIsValid(i, 1, instructions.size()) &&
                    instructions.get(i + 1).isTarget()) {
                    instruction.setOp("nop");
                    return instructions;
                } else if (instruction.getOp().equals("nop") &&
                            jmpIsValid(i, instruction.getArg(), instructions.size()) &&
                            instructions.get(i + instruction.getArg()).isTarget()) {
                    instruction.setOp("jmp");
                    return instructions;
                }
            }
        }
        throw new IllegalArgumentException();
    }

    private static boolean jmpIsValid(int currIndex, int arg, int listSize) {
        return currIndex + arg < listSize && currIndex + arg > 0;
    }

    private static List<Instruction> createInstructions(Stream<String> lines) {
        return lines
            .map(l2 -> {
                Matcher m = INSTRUCTION_PATTERN.matcher(l2);
                if (m.find()) {
                    return new Instruction(m.group(1), m.group(2));
                } else {
                    throw new IllegalArgumentException();
                }
            })
            .collect(Collectors.toList());
    }

    private static List<Instruction> markInstructionsInCycles(List<Instruction> instructions) {
        int cycle = 0;
        for (int i = 0; i < instructions.size(); i++) {
            Instruction instruction = instructions.get(i);
            // if is last instruction
            if (i == instructions.size() - 1) {
                instruction.setCycle(cycle);
                setInstructionsAsTarget(instructions, cycle);
                i = findFirstNonCycleInstruction(instructions) - 1;
                // no more instructions that haven't been marked
                if (i < 0) {
                    break;
                }
                cycle++;
            } else if (instruction.isInCycle()) {
                if (instruction.isTarget()) {
                    setInstructionsAsTarget(instructions, cycle);
                }
                i = findFirstNonCycleInstruction(instructions) - 1;
                // no more instructions that haven't been marked
                if (i < 0) {
                    break;
                }
                cycle++;
            } else {
                instruction.setCycle(cycle);
                if (instruction.getOp().equals("jmp")) {
                    // -1 because i will be incremented again from for loop
                    i += instruction.getArg() - 1;
                }
            }
        }

        return instructions;
    }

    private static void setInstructionsAsTarget(List<Instruction> instructions, int cycle) {
        for (Instruction instruction: instructions) {
            if (instruction.getCycle() == cycle) {
                instruction.setIsTarget();
            }
        }
    }

    private static int findFirstNonCycleInstruction(List<Instruction> instructions) {
        for (int i = 0; i < instructions.size(); i++) {
            if (!instructions.get(i).isInCycle()) {
                return i;
            }
        }

        return -1;
    }

    private static int getOutput(List<Instruction> instructions) {
        int acc = 0;
        for (int i = 0; i < instructions.size(); i++) {
            Instruction instruction = instructions.get(i);
            if (instruction.getOp().equals("acc")) {
                acc += instruction.getArg();
            } else if (instruction.getOp().equals("jmp")) {
                i += instruction.getArg() - 1;
            }
        }

        return acc;
    }

    private static class Instruction {

        private String op;
        private final int arg;
        private int cycle = -1;
        private boolean target = false;

        public Instruction(String op, String arg) {
            this.op = op;
            this.arg = Integer.parseInt(arg);
        }

        public String getOp() {
            return this.op;
        }

        public void setOp(String op) {
            this.op = op;
        }

        public int getArg() {
            return this.arg;
        }

        public int getCycle() {
            return this.cycle;
        }

        public void setCycle(int cycle) {
            this.cycle = cycle;
        }

        public boolean isInCycle() {
            return this.cycle > -1;
        }

        public boolean isInInitialCycle() {
            return this.cycle == 0;
        }

        public void resetCycle() {
            this.cycle = -1;
        }

        public boolean isTarget() {
            return this.target;
        }

        public void setIsTarget() {
            this.target = true;
        }
    }
}
