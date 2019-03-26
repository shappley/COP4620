package COP4620.parser.semantics.nodes;

import COP4620.parser.FunctionSymbol;
import COP4620.parser.Scope;
import COP4620.parser.Symbol;

public class ParamList extends Node {
    private Param param;
    private ParamList paramList;

    public ParamList(Param param, ParamList paramList) {
        this.param = param;
        this.paramList = paramList;
    }

    @Override
    public boolean isValid(Scope scope) {
        if (paramList != null) {
            paramList.forFunction(getFunction());
        }
        return param.isValid(scope) && scope.addDeclaration(param.getId(), param.getSymbolType())
                && getFunction().addParameter(new Symbol(param.getId(), param.getSymbolType()))
                && (paramList == null || paramList.isValid(scope));
    }
}
