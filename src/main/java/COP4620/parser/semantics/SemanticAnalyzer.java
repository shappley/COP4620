package COP4620.parser.semantics;

public class SemanticAnalyzer {
    private Node root;

    public SemanticAnalyzer(Node root) {
        this.root = root;
    }

    public boolean isValid() {
        return root.isValid();
    }
}
