package COP4620.codegen;

import COP4620.tree.nodes.Node;

import java.util.ArrayList;
import java.util.List;

public class CodeGenerator {
    private Node root;

    public CodeGenerator(Node root) {
        this.root = root;
    }

    public List<Quadruple> getInstructions() {
        return root.getInstructions();
    }
}
