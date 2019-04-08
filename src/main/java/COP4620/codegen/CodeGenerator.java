package COP4620.codegen;

import COP4620.tree.nodes.Node;

import java.util.List;

public class CodeGenerator {
    private Node root;
    private int tempVariable = 1;

    public CodeGenerator(Node root) {
        this.root = root;
    }

    public List<Quadruple> getInstructions() {
        return root.getInstructions(this);
    }

    public String getNextTempVariable() {
        return "t" + tempVariable++;
    }
}
