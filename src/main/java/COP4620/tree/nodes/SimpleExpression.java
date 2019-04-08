package COP4620.tree.nodes;

import COP4620.codegen.CodeGenerator;
import COP4620.codegen.Quadruple;
import COP4620.parser.Scope;
import COP4620.parser.Symbol;

import java.util.List;

public class SimpleExpression extends Node {
    private AdditiveExpression left, right;
    private Relop relop;

    public SimpleExpression(AdditiveExpression expression) {
        this(expression, null, null);
    }

    public SimpleExpression(AdditiveExpression left, Relop relop, AdditiveExpression right) {
        this.left = left;
        this.relop = relop;
        this.right = right;
    }

    @Override
    public boolean isValid(Scope scope) {
        if (right != null) {
            if (left.evaluateType(scope) != right.evaluateType(scope)) {
                return false;
            }
        }
        return left.isValid(scope) && (right == null || right.isValid(scope));
    }

    @Override
    public Symbol.Type evaluateType(Scope scope) {
        if (relop != null) {
            return Symbol.Type.INT;
        }
        return evaluateType(scope, left, right);
    }

    @Override
    public List<Quadruple> getInstructions(CodeGenerator gen) {
        return super.getInstructions(gen);
    }
}
