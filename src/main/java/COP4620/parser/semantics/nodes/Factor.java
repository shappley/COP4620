package COP4620.parser.semantics.nodes;

import COP4620.parser.Scope;

public class Factor extends Node {
    private Node child;

    public Factor(Node child) {
        this.child = child;
    }

    @Override
    public boolean isValid(Scope scope) {
        return child.isValid(scope);
    }
}
