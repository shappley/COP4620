package COP4620.codegen;

public class Quadruple {
    private int line;
    private Operation operation;
    private String leftValue;
    private String rightValue;
    private String destination;

    public Quadruple(int line, Operation operation, String leftValue, String rightValue, String destination) {
        this.line = line;
        this.operation = operation;
        this.leftValue = leftValue;
        this.rightValue = rightValue;
        this.destination = destination;
    }

    public int getLine() {
        return line;
    }

    public Operation getOperation() {
        return operation;
    }

    public String getLeftValue() {
        return leftValue;
    }

    public String getRightValue() {
        return rightValue;
    }

    public String getDestination() {
        return destination;
    }
}
