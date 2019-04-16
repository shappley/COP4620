package COP4620.tree.nodes;

import COP4620.codegen.CodeGenerator;
import COP4620.codegen.Operation;
import COP4620.codegen.Quadruple;
import COP4620.parser.Symbol;

import java.util.ArrayList;
import java.util.List;

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

    @Override
    public List<Quadruple> getInstructions(CodeGenerator gen) {
        List<Quadruple> list = new ArrayList<>();
        list.add(new Quadruple(gen.nextLine(), Operation.PARAM, "", "", id));
        list.add(new Quadruple(gen.nextLine(), Operation.ALLOC, "4", "", id));
        return list;
    }
}
