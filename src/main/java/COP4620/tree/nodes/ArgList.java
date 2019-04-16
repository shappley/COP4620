package COP4620.tree.nodes;

import COP4620.codegen.CodeGenerator;
import COP4620.codegen.Operation;
import COP4620.codegen.Quadruple;
import COP4620.parser.Scope;

import java.util.ArrayList;
import java.util.List;

public class ArgList extends Node {
    private Expression expression;
    private ArgList argList;

    public ArgList(Expression expression, ArgList argList) {
        this.expression = expression;
        this.argList = argList;
    }

    public int getArgsCount() {
        if (argList == null) {
            return 1;
        }
        return 1 + argList.getArgsCount();
    }

    @Override
    public boolean isValid(Scope scope) {
        if (argList != null) {
            argList.forFunction(getFunction());
        }
        return expression.isValid(scope)
                && getFunction().matchParameter(expression.evaluateType(scope))
                && (argList != null || getFunction().allParametersMatched())
                && (argList == null || argList.isValid(scope));
    }

    @Override
    public List<Quadruple> getInstructions(CodeGenerator gen) {
        List<Quadruple> list = new ArrayList<>(expression.getInstructions(gen));
        list.add(new Quadruple(gen.nextLine(), Operation.ARG, "", "", expression.getExpressionValue(gen)));
        if (argList != null) {
            list.addAll(argList.getInstructions(gen));
        }
        return list;
    }
}
