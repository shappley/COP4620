package COP4620.tree.nodes;

import COP4620.codegen.CodeGenerator;
import COP4620.codegen.Operation;
import COP4620.codegen.Quadruple;
import COP4620.parser.Scope;
import COP4620.parser.Symbol;

import java.util.List;

public class SimpleExpression extends Node {
    private AdditiveExpression left, right;
    private Relop relop;
    private String expressionValue;

    public SimpleExpression(AdditiveExpression expression) {
        this(expression, null, null);
    }

    public SimpleExpression(AdditiveExpression left, Relop relop, AdditiveExpression right) {
        this.left = left;
        this.relop = relop;
        this.right = right;
    }

    public String getExpressionValue(CodeGenerator gen) {
        if (expressionValue == null) {
            if (relop != null) {
                expressionValue = gen.getNextTempVariable();
            } else {
                expressionValue = left.getExpressionValue(gen);
            }
        }
        return expressionValue;
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
        List<Quadruple> list = left.getInstructions(gen);
        if (relop != null) {
            list.addAll(right.getInstructions(gen));
            list.add(new Quadruple(gen.nextLine(), Operation.COMPR, null, null, null));
        }
        return list;
    }
}
