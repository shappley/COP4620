package COP4620.tree.nodes;

import COP4620.codegen.Operation;
import COP4620.codegen.Quadruple;
import COP4620.parser.Scope;
import COP4620.parser.Symbol;

import java.util.ArrayList;
import java.util.List;

import static COP4620.util.StringUtil.isInteger;
import static java.lang.Integer.parseInt;

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
        } else if (scope.isInLocalScope(getId())) {
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

    @Override
    public List<Quadruple> getInstructions() {
        List<Quadruple> list = new ArrayList<>();
        int size = 4;
        if (getType() == Type.ARRAY) {
            size = size * parseInt(getSize());
        }
        list.add(new Quadruple(-1, Operation.ALLOC, String.valueOf(size), "", getId()));
        return list;
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
