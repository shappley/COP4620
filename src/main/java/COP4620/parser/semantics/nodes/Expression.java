package COP4620.parser.semantics.nodes;

import COP4620.parser.Scope;

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
        //TODO evaluate expressions for type checking
        if (var != null) {
            return var.isValid(scope) && expression.isValid(scope);
        }
        return simpleExpression.isValid(scope);
    }
}
