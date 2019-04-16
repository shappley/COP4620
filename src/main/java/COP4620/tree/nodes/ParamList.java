package COP4620.tree.nodes;

import COP4620.codegen.CodeGenerator;
import COP4620.codegen.Quadruple;
import COP4620.parser.Scope;
import COP4620.parser.Symbol;

import java.util.ArrayList;
import java.util.List;

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
        return param.isValid(scope)
                && (!getFunction().getId().equals("main") || param.getSymbolType() == Symbol.Type.VOID)
                && scope.addDeclaration(param.getId(), param.getSymbolType())
                && getFunction().addParameter(new Symbol(param.getId(), param.getSymbolType()))
                && (paramList == null || paramList.isValid(scope));
    }

    public int getParameterCount() {
        if (paramList == null) {
            return 1;
        }
        return 1 + paramList.getParameterCount();
    }

    @Override
    public List<Quadruple> getInstructions(CodeGenerator gen) {
        List<Quadruple> list = new ArrayList<>(param.getInstructions(gen));
        if (paramList != null) {
            list.addAll(paramList.getInstructions(gen));
        }
        return list;
    }
}
