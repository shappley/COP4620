package COP4620.parser.semantics.nodes;

import COP4620.parser.Scope;
import COP4620.parser.Symbol;

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

    public String getId() {
        return id;
    }

    public Type getType() {
        return type;
    }

    public String getSize() {
        return size;
    }

    public TypeSpecifier getTypeSpecifier() {
        return typeSpecifier;
    }

    public enum Type {
        SINGLE, ARRAY
    }

    @Override
    public boolean isValid(Scope scope) {
        if (type == Type.ARRAY && !isInteger(size)) {
            //can't declare array variable with anything except an integer
            return false;
        } else if (scope.isInScope(getId())) {
            //already declared
            return false;
        } else if (getTypeSpecifier().getType() == TypeSpecifier.Type.VOID) {
            //can't declare void variables
            return false;
        } else if (scope.depth() == 1 && scope.hasFunction("main")) {
            //can't declare anything after main
            return false;
        }
        //looks good, add it to the symbol table
        scope.addDeclaration(id, getSymbolType());
        return true;
    }

    private Symbol.Type getSymbolType() {
        if (getType() == VarDeclaration.Type.SINGLE) {
            if (getTypeSpecifier().getType() == TypeSpecifier.Type.INT) {
                return Symbol.Type.INT;
            } else if (getTypeSpecifier().getType() == TypeSpecifier.Type.FLOAT) {
                return Symbol.Type.FLOAT;
            }
        } else if (getType() == VarDeclaration.Type.ARRAY) {
            if (getTypeSpecifier().getType() == TypeSpecifier.Type.INT) {
                return Symbol.Type.INT_ARRAY;
            } else if (getTypeSpecifier().getType() == TypeSpecifier.Type.FLOAT) {
                return Symbol.Type.FLOAT_ARRAY;
            }
        }
        return null;
    }
}
