package COP4620.parser.semantics.nodes;

import static COP4620.util.StringUtil.isInteger;

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

    @Override
    public boolean isValid() {
        return typeSpecifier.isValid() && (type == Type.SINGLE || (size != null && isInteger(size)));
    }
}
