package COP4620.parser.semantics.nodes;

public class Param extends Node {
    private TypeSpecifier typeSpecifier;
    private String id;
    private Type type;

    public Param(TypeSpecifier typeSpec, String id, Type type) {
        this.typeSpecifier = typeSpec;
        this.id = id;
        this.type = type;
    }

    public enum Type {
        SINGLE, ARRAY
    }

    public TypeSpecifier getTypeSpecifier() {
        return typeSpecifier;
    }

    public String getId() {
        return id;
    }

    public Type getType() {
        return type;
    }
}
