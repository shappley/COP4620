package COP4620.parser.semantics.nodes;

public class TypeSpecifier extends Node {
    private Type type;
    private String value;

    public TypeSpecifier(Type type, String value) {
        this.type = type;
        this.value = value;
    }

    public enum Type {
        FLOAT, INT, VOID
    }
}
