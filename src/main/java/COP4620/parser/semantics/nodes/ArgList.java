package COP4620.parser.semantics.nodes;

import COP4620.parser.FunctionSymbol;
import COP4620.parser.Scope;

public class ArgList extends Node {
    private Expression expression;
    private ArgList argList;
    private FunctionSymbol function;

    public ArgList(Expression expression, ArgList argList) {
        this.expression = expression;
        this.argList = argList;
    }

    public void forFunction(FunctionSymbol function) {
        this.function = function;
    }

    @Override
    public boolean isValid(Scope scope) {
        if (argList != null) {
            argList.forFunction(function);
        }
        return expression.isValid(scope)
                && function.matchParameter(expression.evaluateType(scope))
                && (argList == null || argList.isValid(scope));
    }
}
