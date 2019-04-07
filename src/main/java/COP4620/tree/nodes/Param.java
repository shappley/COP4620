package COP4620.tree.nodes;

import COP4620.parser.Symbol;

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

    public Symbol.Type getSymbolType() {
        if (getType() == Type.SINGLE) {
            if (getTypeSpecifier().getType() == TypeSpecifier.Type.INT) {
                return Symbol.Type.INT;
            } else if (getTypeSpecifier().getType() == TypeSpecifier.Type.FLOAT) {
                return Symbol.Type.FLOAT;
            }
        } else if (getType() == Type.ARRAY) {
            if (getTypeSpecifier().getType() == TypeSpecifier.Type.INT) {
                return Symbol.Type.INT_ARRAY;
            } else if (getTypeSpecifier().getType() == TypeSpecifier.Type.FLOAT) {
                return Symbol.Type.FLOAT_ARRAY;
            }
        }
        return null;
    }
}
