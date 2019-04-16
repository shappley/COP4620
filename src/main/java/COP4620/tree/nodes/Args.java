package COP4620.tree.nodes;

import COP4620.codegen.CodeGenerator;
import COP4620.codegen.Quadruple;
import COP4620.parser.Scope;

import java.util.ArrayList;
import java.util.List;

public class Args extends Node {
    private ArgList args;

    public Args(ArgList args) {
        this.args = args;
    }

    public int getArgsCount() {
        if (args == null) {
            return 0;
        }
        return args.getArgsCount();
    }

    @Override
    public boolean isValid(Scope scope) {
        if (args != null) {
            args.forFunction(getFunction());
        }
        return args == null || args.isValid(scope);
    }

    @Override
    public List<Quadruple> getInstructions(CodeGenerator gen) {
        List<Quadruple> list = new ArrayList<>();
        if (args != null) {
            list.addAll(args.getInstructions(gen));
        }
        return list;
    }
}
