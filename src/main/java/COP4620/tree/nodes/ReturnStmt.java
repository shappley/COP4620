package COP4620.tree.nodes;

import COP4620.codegen.CodeGenerator;
import COP4620.codegen.Operation;
import COP4620.codegen.Quadruple;
import COP4620.parser.Scope;
import COP4620.parser.Symbol;

import java.util.ArrayList;
import java.util.List;

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

    @Override
    public List<Quadruple> getInstructions(CodeGenerator gen) {
        List<Quadruple> list = new ArrayList<>();
        if (expression != null) {
            list.addAll(expression.getInstructions(gen));
            list.add(new Quadruple(gen.nextLine(), Operation.RETURN, "", "", expression.getExpressionValue(gen)));
        }
        return list;
    }
}
