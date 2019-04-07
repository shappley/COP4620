package COP4620.tree.nodes;

public class TypeSpecifier extends Node {
    private Type type;
    private String value;

    public TypeSpecifier(Type type, String value) {
        this.type = type;
        this.value = value;
    }

    public Type getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    public enum Type {
        FLOAT, INT, VOID
    }
}
