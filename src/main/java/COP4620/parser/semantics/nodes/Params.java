package COP4620.parser.semantics.nodes;

import COP4620.parser.FunctionSymbol;
import COP4620.parser.Scope;

public class Params extends Node {
    private ParamList paramList;
    private FunctionSymbol function;

    public Params(ParamList paramList) {
        this.paramList = paramList;
    }

    public Params() {
        this(null);
    }

    public void forFunction(FunctionSymbol function) {
        this.function = function;
    }

    @Override
    public boolean isValid(Scope scope) {
        if (paramList != null) {
            paramList.forFunction(function);
        }
        return paramList == null || paramList.isValid(scope);
    }
}
