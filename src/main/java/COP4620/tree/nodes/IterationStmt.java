package COP4620.tree.nodes;

import COP4620.codegen.CodeGenerator;
import COP4620.codegen.Operation;
import COP4620.codegen.Quadruple;
import COP4620.parser.Scope;

import java.util.ArrayList;
import java.util.List;

public class IterationStmt extends Statement {
    private Expression condition;
    private Statement body;

    public IterationStmt(Expression condition, Statement body) {
        this.condition = condition;
        this.body = body;
    }

    @Override
    public boolean isValid(Scope scope) {
        body.forFunction(getFunction());
        return condition.isValid(scope) && body.isValid(scope);
    }

    @Override
    public List<Quadruple> getInstructions(CodeGenerator gen) {

        Quadruple loop = new Quadruple(-1, Operation.BR, "", "", String.valueOf(gen.getLineNumber() + 1));

        List<Quadruple> list = new ArrayList<>(condition.getInstructions(gen));
        Relop relop = condition.getSimpleExpression().getRelop();
        Quadruple conditionBranch = new Quadruple(-1, null, null, "", null);
        if (relop != null) {
            conditionBranch.setOperation(Operation.getNegatedRelop(relop.getValue()));
            conditionBranch.setLeftValue(condition.getExpressionValue(gen));
        } else {
            list.add(new Quadruple(gen.nextLine(), Operation.COMPR, condition.getExpressionValue(gen), "0", gen.getNextTempVariable()));
            conditionBranch.setOperation(Operation.BRNEQ);
            conditionBranch.setLeftValue(gen.getLastTempVariable());
        }

        conditionBranch.setLine(gen.nextLine());
        list.add(conditionBranch);
        list.addAll(body.getInstructions(gen));
        conditionBranch.setDestination(String.valueOf(list.get(list.size() - 1).getLine() + 2));

        loop.setLine(gen.nextLine());
        list.add(loop);

        return list;
    }
}
