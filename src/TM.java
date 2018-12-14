import java.util.HashMap;
import java.util.HashSet;

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
    private String[] tape;
    private int step;
    private int headIndex; // the index of the read-write head

    TM() {
        stateSet = new HashSet<>();
        inputSymbolSet = new HashSet<>();
        tapeSymbolSet = new HashSet<>();
        finalStateSet = new HashSet<>();
        transitionFunction = new HashMap<>();
        currentState = null;
        tape = null;
        step = -1;
        headIndex = -1;
    }

    public void addState(String state) {
        stateSet.add(state);
    }

    public void addInputSymbol(String inputSymbol) {
        inputSymbolSet.add(inputSymbol);
    }

    public void addTapeSymbol(String tapeSymbol) {
        tapeSymbolSet.add(tapeSymbol);
    }

    public void setInitialState(String initialState) {
        this.initialState = initialState;
    }

    public void addFinalState(String finalState) {
        finalStateSet.add(finalState);
    }

    public void addTransitionFunction(String currentState, String currentSymbol,
                                      String newSymbol, String directionStr, String newState) {
        TransitionInput transitionInput = new TransitionInput(currentState, currentSymbol);
        Direction direction = null;
        if (directionStr.compareTo("L") == 0) {
            direction = Direction.L;
        } else if (directionStr.compareTo("R") == 0) {
            direction = Direction.R;
        } else if (directionStr.compareTo("N") == 0) {
            direction = Direction.N;
        } else {
            System.out.println("Unknown direction");
            System.exit(0);
        }
        TransitionOutput transitionOutput = new TransitionOutput(newState, newSymbol, direction);
        transitionFunction.put(transitionInput, transitionOutput);
    }
}
