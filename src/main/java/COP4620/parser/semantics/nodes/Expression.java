package COP4620.parser.semantics.nodes;

import COP4620.parser.Scope;
import COP4620.parser.Symbol;

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
}
