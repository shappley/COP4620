package COP4620.parser.semantics.nodes;

import COP4620.parser.Scope;
import COP4620.parser.Symbol;

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
        getFunction().hasReturn(true);
        if (expression == null && getFunction().getType() != Symbol.Type.VOID) {
            return false;
        } else if (expression != null && getFunction().getType() != expression.evaluateType(scope)) {
            return false;
        }
        return expression == null || expression.isValid(scope);
    }
}
