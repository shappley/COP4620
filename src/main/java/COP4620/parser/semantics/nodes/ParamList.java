package COP4620.parser.semantics.nodes;

import COP4620.parser.Scope;

public class ParamList extends Node {
    private Param param;
    private ParamList paramList;

    public ParamList(Param param, ParamList paramList) {
        this.param = param;
        this.paramList = paramList;
    }

    @Override
    public boolean isValid(Scope scope) {
        return param.isValid(scope) && (paramList == null || paramList.isValid(scope));
    }
}
