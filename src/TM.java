import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

public class TM {
    private static final String blank = "_";

    private enum Direction {L, R, N}

    private class TransitionInput {
        // inner class for input of transition function
        // i.e. the state and the symbol we read now
        private String currentState;
        private String currentSymbol;

        TransitionInput(String currentState, String currentSymbol) {
            this.currentState = currentState;
            this.currentSymbol = currentSymbol;
        }

        @Override
        public boolean equals(Object object) {
            if (this == object) {
                return true;
            }
            if (!(object instanceof TransitionInput)) {
                return false;
            }
            TransitionInput input = (TransitionInput) object;
            return this.currentState.compareTo(input.currentState) == 0
                    && this.currentSymbol.compareTo(input.currentSymbol) == 0;
        }

        @Override
        public int hashCode() {
            int hash = 17;
            hash = 31 * hash + this.currentState.hashCode();
            hash = 31 * hash + this.currentSymbol.hashCode();
            return hash;
        }
    }

    private class TransitionOutput {
        private String nextState;
        private String newSymbol;
        private Direction direction;

        TransitionOutput(String nextState, String newSymbol, Direction direction) {
            this.nextState = nextState;
            this.newSymbol = newSymbol;
            this.direction = direction;
        }
    }

    /* immutable vars */
    private HashSet<String> stateSet;
    private HashSet<String> inputSymbolSet;
    private HashSet<String> tapeSymbolSet;
    private String initialState;
    private HashSet<String> finalStateSet;
    private HashMap<TransitionInput, TransitionOutput> transitionFunction;

    /* vars that change every time after TM moves */
    private String currentState;
    private LinkedList<String> tape;
    private int step;
    private int headIndex; // the index of the read-write head
    private int startIndex; // the index of the start point

    TM() {
        stateSet = new HashSet<>();
        inputSymbolSet = new HashSet<>();
        tapeSymbolSet = new HashSet<>();
        finalStateSet = new HashSet<>();
        transitionFunction = new HashMap<>();
        currentState = null;
        tape = new LinkedList<>();
        step = 0;
        headIndex = startIndex = 0;
    }

    private void addState(String state) {
        stateSet.add(state);
    }

    private void addInputSymbol(String inputSymbol) {
        inputSymbolSet.add(inputSymbol);
    }

    private void addTapeSymbol(String tapeSymbol) {
        tapeSymbolSet.add(tapeSymbol);
    }

    private void setInitialState(String initialState) {
        this.initialState = initialState;
    }

    private void addFinalState(String finalState) {
        finalStateSet.add(finalState);
    }

    private void addTransitionFunction(String currentState, String currentSymbol,
                                       String newSymbol, String directionStr, String newState) {
        TransitionInput transitionInput = new TransitionInput(currentState, currentSymbol);
        Direction direction = null;
        if (directionStr.compareTo("l") == 0) {
            direction = Direction.L;
        } else if (directionStr.compareTo("r") == 0) {
            direction = Direction.R;
        } else if (directionStr.compareTo("*") == 0) {
            direction = Direction.N;
        } else {
            System.out.println("Unknown direction");
            System.exit(0);
        }
        TransitionOutput transitionOutput = new TransitionOutput(newState, newSymbol, direction);
        transitionFunction.put(transitionInput, transitionOutput);
    }

    private int findFirstNonBlank() {
        // return the index of the first non-blank symbol or the headIndex
        for (int i = 0; i < headIndex; i++) {
            if (tape.get(i).compareTo(blank) != 0)
                return i;
        }
        return headIndex;
    }

    private int findLastNonBlank() {
        // return the index of the last non-blank symbol or the headIndex
        for (int i = tape.size() - 1; i > headIndex; i--) {
            if (tape.get(i).compareTo(blank) != 0)
                return i;
        }
        return headIndex;
    }

    private int howManyDigits(int number) {
        // how many digits in this number
        int digit = 1;
        while (number / (int) Math.pow(10, digit) != 0)
            digit++;
        return digit;
    }

    private void printSpace(int number) {
        // print number spaces on console
        for (int i = 0; i < number; i++)
            System.out.print(" ");
    }

    private void printID() {
        System.out.println("Step:  " + step);
        System.out.print("Index: ");
        int first = findFirstNonBlank();
        int last = findLastNonBlank();
        for (int i = first; i <= last; i++) {
            if (i != tape.size() - 1)
                System.out.print(Math.abs(i - startIndex) + " ");
            else
                System.out.print(Math.abs(i - startIndex));
        }
        System.out.println();
        System.out.print("Tape:  ");
        for (int i = first; i <= last; i++) {
            String symbol = tape.get(i);
            if (i != tape.size() - 1) {
                System.out.print(symbol);
                printSpace(howManyDigits(Math.abs(i-startIndex)));
            } else
                System.out.print(symbol);
        }
        System.out.println();
        System.out.print("Head:  ");
        for (int i = first; i <= last; i++) {
            if (i == headIndex) {
                System.out.println("^");
                break;
            } else {
                printSpace(howManyDigits(Math.abs(i - startIndex)) + 1);
            }
        }
        System.out.println("State: " + currentState);
    }

    private boolean oneMove() {
        // make one move
        // first, find transition function
        step++;
        String currentSymbol = tape.get(headIndex);
        TransitionInput input = new TransitionInput(currentState, currentSymbol);
        TransitionOutput output = transitionFunction.get(input);
        if (output == null) {
            // try wildcard
            input = new TransitionInput(currentState, "*");
            output = transitionFunction.get(input);
            if (output == null)
                return false; // can't move further
        }
        // second, move accordingly
        currentState = output.nextState; // change state
        if (output.newSymbol.compareTo("*") != 0)
            tape.set(headIndex, output.newSymbol); // write symbol on tape
        if (output.direction == Direction.L)
            headIndex--;
        else if (output.direction == Direction.R)
            headIndex++;
        if (headIndex >= tape.size())
            tape.addLast(blank); // add a blank symbol
        else if (headIndex < 0) {
            tape.addFirst(blank); // add a blank symbol
            startIndex++;
            headIndex = 0;
        }
        return true;
    }

    public void createFromFile(String filename) {
        // create a TM from file (.tm)
        TMParser tmParser = new TMParser(filename);
        // state set
        for (String state : tmParser.getStates())
            addState(state);
        // input symbol set
        for (String symbol : tmParser.getInputSymbols())
            addInputSymbol(symbol);
        // tape symbol set
        for (String symbol : tmParser.getTapeSymbols())
            addTapeSymbol(symbol);
        // initial state
        setInitialState(tmParser.getInitialState());
        // final states
        for (String state : tmParser.getFinalStates())
            addFinalState(state);
        // transition function
        for (String[] function : tmParser.getTransitionFunctions()) {
            addTransitionFunction(function[0], function[1], function[2], function[3], function[4]);
        }
    }

    public void run(String input) {
        boolean illegal = false;
        System.out.println("Input: " + input);
        String delimOne = "====================";
        String delimTwo = "---------------------------------------------";
        // whether this input is illegal or not
        char[] inputArray = input.toCharArray();
        for (char c : inputArray) {
            if (!inputSymbolSet.contains(String.valueOf(c))) {
                System.out.println(delimOne + " ERR " + delimOne);
                System.out.println("The input \"" + input + "\" is illegal");
                illegal = true;
                System.out.println(delimOne + " END " + delimOne);
            }
        }
        if (!illegal) {
            // we now know the input is legal
            currentState = initialState;
            for (char c : inputArray)
                tape.add(String.valueOf(c));
            if (inputArray.length == 0)
                tape.add(blank);
            step = 0;
            headIndex = startIndex = 0;
            System.out.println(delimOne + "RUN" + delimOne);
            printID();
            System.out.println(delimTwo);
            while (oneMove()) {
                printID();
                System.out.println(delimTwo);
            }
            System.out.print("Result: ");
            for (String symbol : tape)
                if (symbol.compareTo(blank) != 0)
                    System.out.print(symbol);
            System.out.println();
            System.out.println(delimOne + "END" + delimOne);
        }
    }

    public static void main(String[] args) {
        TM tm = new TM();
        //tm.createFromFile("./TM/palindrome_detector.tm");
        tm.createFromFile("./TM/palindrome_detector.tm");
        tm.run("");
        //tm.run("1001");
    }
}
