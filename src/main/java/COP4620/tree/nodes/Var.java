package COP4620.tree.nodes;

import COP4620.codegen.CodeGenerator;
import COP4620.codegen.Operation;
import COP4620.codegen.Quadruple;
import COP4620.parser.Scope;
import COP4620.parser.Symbol;
import COP4620.parser.Symbol.Type;

import java.util.ArrayList;
import java.util.List;

public class Var extends Node {
    private String id;
    private Expression expression;
    private String expressionValue;

    public Var(String id) {
        this(id, null);
    }

    public Var(String id, Expression expression) {
        this.id = id;
        this.expression = expression;
    }

    public String getExpressionValue(CodeGenerator gen) {
        if (expressionValue == null) {
            if (expression != null) {
                expressionValue = gen.getNextTempVariable();
            } else {
                expressionValue = getId();
            }
        }
        return expressionValue;
    }

    public String getId() {
        return id;
    }

    @Override
    public boolean isValid(Scope scope) {
        return scope.isInScope(id)
                && (expression == null || expression.evaluateType(scope) == Type.INT)
                && (expression == null || expression.isValid(scope));
    }

    @Override
    public Type evaluateType(Scope scope) {
        Symbol.Type type = scope.getTypeOf(getId());
        if (expression != null) {
            return type == Type.INT_ARRAY ? Type.INT : Type.FLOAT;
        }
        return type;
    }

    @Override
    public List<Quadruple> getInstructions(CodeGenerator gen) {
        List<Quadruple> list = new ArrayList<>();
        if (expression != null) {
            list.addAll(expression.getInstructions(gen));
            list.add(new Quadruple(gen.nextLine(), Operation.MUL, expression.getExpressionValue(gen), "4", gen.getNextTempVariable()));
            list.add(new Quadruple(gen.nextLine(), Operation.DISP, getId(), gen.getLastTempVariable(), gen.getNextTempVariable()));
        }
        return list;
    }
}
