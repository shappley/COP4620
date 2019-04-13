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

    public void setLine(int line) {
        this.line = line;
    }

    public Operation getOperation() {
        return operation;
    }

    public void setOperation(Operation operation) {
        this.operation = operation;
    }

    public String getLeftValue() {
        return leftValue;
    }

    public void setLeftValue(String leftValue) {
        this.leftValue = leftValue;
    }

    public String getRightValue() {
        return rightValue;
    }

    public void setRightValue(String rightValue) {
        this.rightValue = rightValue;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    @Override
    public String toString() {
        return String.format("%-8s%-8s%-8s%-8s%s", getLine(), getOperation(), getLeftValue(), getRightValue(), getDestination());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Quadruple) {
            Quadruple other = (Quadruple) obj;
            return this.getLine() == other.getLine()
                    && this.getOperation() == other.getOperation()
                    && this.getLeftValue().equals(other.getLeftValue())
                    && this.getRightValue().equals(other.getRightValue());
        }
        return false;
    }
}
