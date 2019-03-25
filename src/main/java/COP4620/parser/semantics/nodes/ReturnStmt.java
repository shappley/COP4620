package COP4620.parser.semantics.nodes;

import COP4620.parser.Scope;

public class ReturnStmt extends Statement {
    private Expression expression;

    public ReturnStmt() {
        this(null);
    }

    public ReturnStmt(Expression expression) {
        this.expression = expression;
    }

    @Override
    public boolean isValid(Scope scope) {
        return expression == null || expression.isValid(scope);
    }
}
