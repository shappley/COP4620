package COP4620.parser.semantics.nodes;

import COP4620.parser.semantics.Node;

public class VarDeclaration extends Node {
    private TypeSpecifier typeSpecifier;
    private String id, size;
    private Type type;

    private VarDeclaration(TypeSpecifier typeSpecifier, String id, String size, Type type) {
        this.typeSpecifier = typeSpecifier;
        this.id = id;
        this.size = size;
        this.type = type;
    }

    public VarDeclaration(TypeSpecifier typeSpecifier, String id, String size) {
        this(typeSpecifier, id, size, Type.ARRAY);
    }

    public VarDeclaration(TypeSpecifier typeSpecifier, String id) {
        this(typeSpecifier, id, null, Type.SINGLE);
    }

    public enum Type {
        SINGLE, ARRAY
    }
}
