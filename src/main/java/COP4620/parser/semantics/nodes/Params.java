package COP4620.parser.semantics.nodes;

import COP4620.parser.Scope;

public class Params extends Node {
    private ParamList paramList;

    public Params(ParamList paramList) {
        this.paramList = paramList;
    }

    public Params() {
        this(null);
    }

    @Override
    public boolean isValid(Scope scope) {
        return paramList == null || paramList.isValid(scope);
    }
}
