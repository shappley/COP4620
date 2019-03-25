package COP4620.parser.semantics.nodes;

import COP4620.parser.Scope;

public class Declaration extends Node {
    private Type type;
    private Node value;

    public Declaration(Type type, Node value) {
        this.type = type;
        this.value = value;
    }

    public Node getValue() {
        return value;
    }

    public Type getType() {
        return type;
    }

    public enum Type {
        VARIABLE, FUNCTION
    }

    @Override
    public boolean isValid(Scope scope) {
        return value.isValid(scope);
    }
}
