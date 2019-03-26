package COP4620.parser.semantics.nodes;

import COP4620.parser.FunctionSymbol;
import COP4620.parser.Scope;

public class ArgList extends Node {
    private Expression expression;
    private ArgList argList;

    public ArgList(Expression expression, ArgList argList) {
        this.expression = expression;
        this.argList = argList;
    }

    @Override
    public boolean isValid(Scope scope) {
        if (argList != null) {
            argList.forFunction(getFunction());
        }
        return expression.isValid(scope)
                && getFunction().matchParameter(expression.evaluateType(scope))
                && (argList == null || argList.isValid(scope));
    }
}
