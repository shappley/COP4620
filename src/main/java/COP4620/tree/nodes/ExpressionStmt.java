package COP4620.tree.nodes;

import COP4620.codegen.CodeGenerator;
import COP4620.codegen.Quadruple;
import COP4620.parser.Scope;

import java.util.List;

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

    @Override
    public List<Quadruple> getInstructions(CodeGenerator gen) {
        if (expression != null) {
            return expression.getInstructions(gen);
        }
        return super.getInstructions(gen);
    }
}
