import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class TMParser {
    private static final String parseString = "[ {},=]";
    private ArrayList<String> TMDescriptor;

    TMParser(String fileName) {
        TMDescriptor = new ArrayList<>();
        /* use a .tm file to initial parser */
        File file = new File(fileName);
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                if (line.compareTo("") != 0 && !line.startsWith(";")) {
                    String[] parseComment = line.split(";");
                    line = parseComment[0];
                    TMDescriptor.add(line);
                    //System.out.println(line);
                }
            }
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Iterable<String> removeEmpty(String[] parsedElement) {
        ArrayList<String> result = new ArrayList<>();
        for (String s : parsedElement) {
            if (s.compareTo("") != 0)
                result.add(s);
        }
        return result;
    }

    public Iterable<String> getStates() {
        // return all states
        String statesRaw = null;
        for (String s : TMDescriptor) {
            if (s.length() >= 2 && s.charAt(0) == '#' && s.charAt(1) == 'Q') {
                statesRaw = s.substring(2);
            }
        }
        if (statesRaw == null) {
            System.out.println("Parsing error! State set not found!");
            System.exit(0);
        }
        String[] parsedStates = statesRaw.split(parseString);
        return removeEmpty(parsedStates);
    }

    public Iterable<String> getInputSymbols() {
        // return all input symbols
        String inputSymbolsRaw = null;
        for (String s : TMDescriptor) {
            if (s.length() > 2 && s.charAt(0) == '#' && s.charAt(1) == 'S') {
                inputSymbolsRaw = s.substring(2);
            }
        }
        if (inputSymbolsRaw == null) {
            System.out.println("Parsing error! Input symbol set not found!");
            System.exit(0);
        }
        String[] parsedInputSymbols = inputSymbolsRaw.split(parseString);
        return removeEmpty(parsedInputSymbols);
    }

    public Iterable<String> getTapeSymbols() {
        // return all tape symbols
        String tapeSymbolsRaw = null;
        for (String s : TMDescriptor) {
            if (s.length() > 2 && s.charAt(0) == '#' && s.charAt(1) == 'T') {
                tapeSymbolsRaw = s.substring(2);
            }
        }
        if (tapeSymbolsRaw == null) {
            System.out.println("Parsing error! Tape symbol set not found!");
            System.exit(0);
        }
        String[] parsedTapeSymbols = tapeSymbolsRaw.split(parseString);
        return removeEmpty(parsedTapeSymbols);
    }

    public String getInitialState() {
        // return the initial state
        String initialStateRaw = null;
        for (String s : TMDescriptor) {
            if (s.length() > 3 && s.charAt(0) == '#' && s.charAt(1) == 'q' && s.charAt(2) == '0') {
                initialStateRaw = s.substring(3);
            }
        }
        if (initialStateRaw == null) {
            System.out.println("Parsing error! Initial state not found!");
            System.exit(0);
        }
        String[] parsedInitialState = initialStateRaw.split(parseString);
        String initialState = "";
        for (String s : parsedInitialState) {
            if (s.compareTo("") != 0)
                initialState = s;
        }
        return initialState;
    }

    public Iterable<String> getFinalStates() {
        // return all final states
        String finalStatesRaw = null;
        for (String s : TMDescriptor) {
            if (s.length() > 2 && s.charAt(0) == '#' && s.charAt(1) == 'F') {
                finalStatesRaw = s.substring(2);
            }
        }
        if (finalStatesRaw == null) {
            System.out.println("Parsing error! Final state set not found!");
            System.exit(0);
        }
        String[] parsedFinalStates = finalStatesRaw.split(parseString);
        return removeEmpty(parsedFinalStates);
    }

    public Iterable<String[]> getTransitionFunctions() {
        // return all transition functions (in the form of an array consisting of 5 elements)
        ArrayList<String[]> functions = new ArrayList<>();
        String[] function;
        for (String s : TMDescriptor) {
            if (s.charAt(0) != '#') {
                function = s.split(" ");
                if (function.length != 5) {
                    System.out.println("Parsing error! Transition function not complete!");
                    System.exit(0);
                }
                functions.add(function);
            }
        }
        return functions;
    }

    // unit test
    public static void main(String[] args) {
        TMParser tmParser = new TMParser("./TM/palindrome_detector.tm");
        //for (String s : tmParser.getFinalStates())
        //System.out.println(s);
        // System.out.println(tmParser.getInitialState());
        for (String[] test : tmParser.getTransitionFunctions()) {
            for (String s : test)
                System.out.print(s + " ");
            System.out.println();
        }
    }
}
