package COP4620.parser.semantics;

import COP4620.parser.semantics.nodes.Node;

public class SemanticAnalyzer {
    private Node root;

    public SemanticAnalyzer(Node root) {
        this.root = root;
    }

    public boolean isValid() {
        return root != null && root.isValid();
    }
}
