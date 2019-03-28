package COP4620.parser.semantics.nodes;

import COP4620.parser.Scope;

public class IterationStmt extends Statement {
    private Expression expression;
    private Statement statement;

    public IterationStmt(Expression expression, Statement statement) {
        this.expression = expression;
        this.statement = statement;
    }

    @Override
    public boolean isValid(Scope scope) {
        statement.forFunction(getFunction());
        return expression.isValid(scope) && statement.isValid(scope);
    }
}
