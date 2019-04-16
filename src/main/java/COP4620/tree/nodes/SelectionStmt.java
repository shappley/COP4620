package COP4620.tree.nodes;

import COP4620.codegen.CodeGenerator;
import COP4620.codegen.Operation;
import COP4620.codegen.Quadruple;
import COP4620.parser.Scope;

import java.util.ArrayList;
import java.util.List;

public class SelectionStmt extends Statement {
    private Expression condition;
    private Statement body, elseBody;

    public SelectionStmt(Expression condition, Statement body, Statement elseBody) {
        this.condition = condition;
        this.body = body;
        this.elseBody = elseBody;
    }

    public SelectionStmt(Expression condition, Statement body) {
        this(condition, body, null);
    }

    @Override
    public boolean isValid(Scope scope) {
        body.forFunction(getFunction());
        if (elseBody != null) {
            elseBody.forFunction(getFunction());
        }
        scope.addScope();
        boolean b = condition.isValid(scope) && body.isValid(scope) && (elseBody == null || elseBody.isValid(scope));
        scope.removeScope();
        return b;
    }

    @Override
    public List<Quadruple> getInstructions(CodeGenerator gen) {
        List<Quadruple> list = new ArrayList<>(condition.getInstructions(gen));
        Relop relop = condition.getSimpleExpression().getRelop();
        Quadruple conditionBranch = new Quadruple(-1, null, null, "", null);
        if (relop != null) {
            conditionBranch.setOperation(Operation.getNegatedRelop(relop.getValue()));
            conditionBranch.setLeftValue(condition.getExpressionValue(gen));
        } else {
            list.add(new Quadruple(gen.nextLine(), Operation.COMPR, condition.getExpressionValue(gen), "0", gen.getNextTempVariable()));
            conditionBranch.setOperation(Operation.BDE);
            conditionBranch.setLeftValue(gen.getLastTempVariable());
        }

        conditionBranch.setLine(gen.nextLine());
        list.add(conditionBranch);
        list.addAll(body.getInstructions(gen));
        conditionBranch.setDestination(String.valueOf(list.get(list.size() - 1).getLine() + 1));

        if (elseBody != null) {
            conditionBranch.setDestination(String.valueOf(list.get(list.size() - 1).getLine() + 1));
            Quadruple unconditionalBranch = new Quadruple(gen.nextLine(), Operation.BR, "", "", null);
            list.add(unconditionalBranch);
            list.addAll(elseBody.getInstructions(gen));
            unconditionalBranch.setDestination(String.valueOf(list.get(list.size() - 1).getLine() + 1));
        }

        return list;
    }
}
