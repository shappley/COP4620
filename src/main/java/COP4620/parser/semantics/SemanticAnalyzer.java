package COP4620.parser.semantics;

import COP4620.parser.Scope;
import COP4620.parser.semantics.nodes.Node;

public class SemanticAnalyzer {
    private Node root;
    private Scope scope;

    public SemanticAnalyzer(Node root) {
        this.root = root;
        this.scope = new Scope();
    }

    public boolean isValid() {
        return root != null && root.isValid(scope);
    }
}
