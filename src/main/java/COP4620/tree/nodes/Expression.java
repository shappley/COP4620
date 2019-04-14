package COP4620.tree.nodes;

import COP4620.codegen.CodeGenerator;
import COP4620.codegen.Operation;
import COP4620.codegen.Quadruple;
import COP4620.parser.Scope;
import COP4620.parser.Symbol;

import java.util.ArrayList;
import java.util.List;

public class Expression extends Node {
    private Var var;
    private Expression expression;
    private SimpleExpression simpleExpression;

    public Expression(Var var, Expression expression) {
        this.var = var;
        this.expression = expression;
    }

    public Expression(SimpleExpression expression) {
        this.simpleExpression = expression;
    }

    public String getExpressionValue(CodeGenerator gen) {
        if (simpleExpression != null) {
            return simpleExpression.getExpressionValue(gen);
        }
        return expression.getExpressionValue(gen);
    }

    @Override
    public boolean isValid(Scope scope) {
        if (var != null) {
            return var.isValid(scope)
                    && var.evaluateType(scope) == expression.evaluateType(scope)
                    && expression.isValid(scope);
        }
        return simpleExpression.isValid(scope);
    }

    @Override
    public Symbol.Type evaluateType(Scope scope) {
        Symbol.Type type;
        if (var != null) {
            type = expression.evaluateType(scope);
        } else {
            type = simpleExpression.evaluateType(scope);
        }
        return type;
    }

    @Override
    public List<Quadruple> getInstructions(CodeGenerator gen) {
        if (simpleExpression != null) {
            return new ArrayList<>(simpleExpression.getInstructions(gen));
        }
        List<Quadruple> list = new ArrayList<>(expression.getInstructions(gen));
        Quadruple instruction = new Quadruple(gen.nextLine(), Operation.ASGN, gen.getLastTempVariable(), "", var.getId());
        if (list.isEmpty()) {
            instruction.setLeftValue(getExpressionValue(gen));
        }
        list.add(instruction);
        return list;
    }
}
