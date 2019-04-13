package COP4620.codegen;

import COP4620.tree.nodes.Node;

import java.util.List;

public class CodeGenerator {
    private Node root;
    private int tempVariable = 0;
    private int lineNumber = 0;

    public CodeGenerator(Node root) {
        this.root = root;
    }

    public List<Quadruple> getInstructions() {
        return root.getInstructions(this);
    }

    public String getPreviousTempVariable() {
        return "t" + (tempVariable - 1);
    }

    public String getLastTempVariable() {
        return "t" + tempVariable;
    }

    public String getNextTempVariable() {
        return "t" + ++tempVariable;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public int nextLine() {
        return ++lineNumber;
    }
}
