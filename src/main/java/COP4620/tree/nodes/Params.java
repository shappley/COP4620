package COP4620.tree.nodes;

import COP4620.codegen.CodeGenerator;
import COP4620.codegen.Quadruple;
import COP4620.parser.Scope;

import java.util.ArrayList;
import java.util.List;

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
        if (paramList != null) {
            paramList.forFunction(getFunction());
        }
        return paramList == null || paramList.isValid(scope);
    }

    public int getParameterCount() {
        if (paramList == null) {
            return 0;
        }
        return paramList.getParameterCount();
    }

    @Override
    public List<Quadruple> getInstructions(CodeGenerator gen) {
        List<Quadruple> list = new ArrayList<>();
        if (paramList != null) {
            list.addAll(paramList.getInstructions(gen));
        }
        return list;
    }
}
