package COP4620.tree.nodes;

import COP4620.codegen.CodeGenerator;
import COP4620.codegen.Quadruple;
import COP4620.parser.Scope;

import java.util.List;

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
        return (scope.depth() <= 1 || !scope.hasFunction("main")) && value.isValid(scope);
    }

    @Override
    public List<Quadruple> getInstructions(CodeGenerator gen) {
        return value.getInstructions(gen);
    }
}
