package COP4620.parser.semantics.nodes;

import COP4620.parser.Scope;

public class ExpressionStmt extends Statement {
    private Expression expression;

    public ExpressionStmt() {
        this(null);
    }

    public ExpressionStmt(Expression expression) {
        this.expression = expression;
    }

    @Override
    public boolean isValid(Scope scope) {
        return expression == null || expression.isValid(scope);
    }
}
